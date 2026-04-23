package com.bank.online_banking.services.impl;

import com.bank.online_banking.core.generator.PrefixIdGenerator;
import com.bank.online_banking.core.mapper.TransactionMapper;
import com.bank.online_banking.dto.request.CreateTransactionRequest;
import com.bank.online_banking.dto.response.TransactionDetailsResponse;
import com.bank.online_banking.dto.response.TransactionResponse;
import com.bank.online_banking.exceptions.*;
import com.bank.online_banking.model.entity.Account;
import com.bank.online_banking.model.entity.Transaction;
import com.bank.online_banking.model.entity.User;
import com.bank.online_banking.model.enums.TransactionStatus;
import com.bank.online_banking.model.enums.TransactionType;
import com.bank.online_banking.repository.AccountRepository;
import com.bank.online_banking.repository.TransactionRepository;
import com.bank.online_banking.security.CurrentUserService;
import com.bank.online_banking.services.TransactionService;
import com.bank.online_banking.validator.TransactionValidator;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PrefixIdGenerator prefixIdGenerator;
    private final CurrentUserService currentUserService;
    private final TransactionValidator validator;

    @Override
    public TransactionDetailsResponse getTransactionById(UUID transactionId) {
        User currentUser = currentUserService.getCurrentUser();

        Transaction transaction = transactionRepository
                .findAuthorizedTransaction(transactionId, currentUser.getUserId())
                .orElseThrow(() -> new TransactionNotFoundException(transactionId.toString()));

         log.info("Getting transaction with id {}", transactionId);
         return TransactionMapper.toDetailsResponse(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse createTransaction(CreateTransactionRequest request) {

        log.info("Processing transaction request for amount: {}", request.getAmount());
        
        try {
            // Validate input
            validator.validateTransactionRequest(request);
            
            // Get authenticated user
            User currentUser = currentUserService.getCurrentUser();
            log.debug("Transaction initiated by user: {}", currentUser.getEmail());
            
            // Find source account with pessimistic write lock
            Account fromAccount = accountRepository.findByUserEmail(currentUser.getEmail())
                    .orElseThrow(() -> new AccountNotFoundException(
                            "No account found for current user"));
            
            // Find destination account with pessimistic write lock
            Account toAccount = accountRepository.findByAccountIdWithLock(request.getToAccountId())
                    .orElseThrow(() -> new AccountNotFoundException(
                            request.getToAccountId()));
            
            // Security: Verify account ownership
            if (!fromAccount.getUser().getUserId().equals(currentUser.getUserId())) {
                log.warn("Unauthorized transaction attempt - user {} tried to use account {}", 
                        currentUser.getEmail(), fromAccount.getAccountId());
                throw new UnauthorizedTransactionException(
                        "You can only transfer from your own accounts");
            }
            
            // Business logic validations
            if (fromAccount.getId().equals(toAccount.getId())) {
                throw new TransactionProcessingException(
                        "Cannot transfer to the same account");
            }
            
            if (!fromAccount.getCurrency().equals(request.getCurrency())) {
                throw new TransactionProcessingException(
                        "Account currency mismatch. Expected: " + fromAccount.getCurrency());
            }
            
            // Check sufficient balance
            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                log.warn("Insufficient balance - user: {}, available: {}, requested: {}",
                        currentUser.getEmail(), fromAccount.getBalance(), request.getAmount());
                throw new InsufficientBalanceException();
            }
            
            // Perform transaction
            BigDecimal newFromBalance = fromAccount.getBalance().subtract(request.getAmount());
            BigDecimal newToBalance = toAccount.getBalance().add(request.getAmount());
            
            fromAccount.setBalance(newFromBalance);
            toAccount.setBalance(newToBalance);
            fromAccount.setLastTransactionDate(LocalDateTime.now());
            toAccount.setLastTransactionDate(LocalDateTime.now());
            
            accountRepository.saveAll(List.of(fromAccount, toAccount));
            
            // Create transaction record
            Transaction transaction = createTransactionRecord(
                    request, fromAccount, toAccount, newFromBalance);
            
            transactionRepository.save(transaction);
            
            log.info("Transaction completed successfully - ID: {}, Amount: {}, From: {}, To: {}", 
                    transaction.getTransactionId(), request.getAmount(), 
                    fromAccount.getAccountId(), toAccount.getAccountId());
            
            return TransactionMapper.toCreateResponse(transaction);
            
        } catch (BankingException ex) {
            log.error("Banking exception during transaction: {} - {}", ex.getErrorCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during transaction processing", ex);
            throw new TransactionProcessingException(
                    "Transaction processing failed: " + ex.getMessage());
        }
    }

    /**
     * Create transaction entity with all details.
     */
    private Transaction createTransactionRecord(CreateTransactionRequest request, 
            Account fromAccount, Account toAccount, BigDecimal balanceAfter) {
        
        return Transaction.builder()
                .transactionId(prefixIdGenerator.generateTransactionNumber())
                .referenceNumber(generateReferenceNumber())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .notes(request.getNotes())
                .transactionStatus(TransactionStatus.COMPLETED)
                .transactionType(TransactionType.TRANSFER)
                .balanceAfter(balanceAfter)
                .executionDate(LocalDateTime.now())
                .isFlagged(false)
                .build();
    }

    /**
     * Generate unique reference number for transaction tracking.
     */
    private String generateReferenceNumber() {
        return "REF" + UUID.randomUUID().toString()
                .replace("-", "").substring(0, 12).toUpperCase();
    }
}
