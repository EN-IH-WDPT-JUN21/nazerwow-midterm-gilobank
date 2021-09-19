package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.service.interfaces.ITransactionService;
import com.ironhack.gilobank.utils.FraudDetection;
import com.ironhack.gilobank.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private FraudDetection fraudDetection;

//    public void createTransactionLog(Account account, BigDecimal amount, Optional<LocalDate> date) {
//        LocalDate transactionDate;
//        Money moneyAmount = new Money(amount);
//        String transactionName;
//        if(date.isPresent()) {
//            transactionDate = date.get();
//        } else { transactionDate = LocalDate.now();
//        }
//        if(amount.longValue() >= 0) {
//            transactionName = moneyAmount + " credited on: " + transactionDate;
//        }
//        else {
//            transactionName = moneyAmount + " debited on: " + transactionDate;
//        }
//        transactionRepository.save(new Transaction(account, transactionName, amount, account.getBalance(), transactionDate));
//    }

    public Transaction createTransactionLog(Account account, TransactionDTO transactionDTO) {
        LocalDateTime transactionDate = LocalDateTime.now();
        if(transactionDTO.getTimeOfTrns() != null) transactionDate = transactionDTO.getTimeOfTrns();
        Money moneyAmount = new Money(transactionDTO.getAmount());
        String transactionName = moneyAmount + transactionDTO.getType().toString();
        Transaction transaction = new Transaction(account, transactionName, transactionDTO.getAmount(), account.getBalance(), transactionDTO.getType(), transactionDate);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction createTransactionLogCredit(Account account, BigDecimal amount) {
        LocalDateTime transactionDate = LocalDateTime.now();
        String transactionName;
        Money moneyAmount = new Money(amount);
        transactionName = moneyAmount + " credit";
        Transaction transaction = new Transaction(account, transactionName, amount, account.getBalance(), TransactionType.CREDIT, transactionDate);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction createTransactionLogCredit(Account account, BigDecimal amount, LocalDateTime date) {
        Money moneyAmount = new Money(amount);
        String transactionName;
        transactionName = moneyAmount + " credit";
        Transaction transaction = new Transaction(account, transactionName, amount, account.getBalance(), TransactionType.CREDIT, date);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction createTransactionLogDebit(Account account, BigDecimal amount) {
        LocalDateTime transactionDate = LocalDateTime.now();
        String transactionName;
        Money moneyAmount = new Money(amount);
        transactionName = moneyAmount + " debit";
        Transaction transaction = new Transaction(account, transactionName, amount, account.getBalance(), TransactionType.DEBIT, transactionDate);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction createTransactionLogDebit(Account account, BigDecimal amount, LocalDateTime date) {
        Money moneyAmount = new Money(amount);
        String transactionName;
        transactionName = moneyAmount + " debit";
        Transaction transaction = new Transaction(account, transactionName, amount, account.getBalance(), TransactionType.DEBIT, date);
        transactionRepository.save(transaction);
        return transaction;
    }

    public List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount) {
        LocalDateTime transactionDate = LocalDateTime.now();
        Money moneyAmount = new Money(amount);
        String debitName = moneyAmount + " Transfer to Account Number: " + creditAccount.getAccountNumber();
        String creditName = moneyAmount + " Transfer from Account Number: " + debitAccount.getAccountNumber();
        Transaction debit = new Transaction(debitAccount, debitName, amount, debitAccount.getBalance(), TransactionType.TRANSFER_DEBIT, transactionDate);
        Transaction credit = new Transaction(debitAccount, debitName, amount, debitAccount.getBalance(), TransactionType.TRANSFER_CREDIT, transactionDate);
        transactionRepository.saveAll(List.of(debit, credit));
        return List.of(debit, credit);
    }

    public List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount, LocalDateTime transactionDate) {
        Money moneyAmount = new Money(amount);
        String debitName = moneyAmount + " Transfer to Account Number: " + creditAccount.getAccountNumber();
        String creditName = moneyAmount + " Transfer from Account Number: " + debitAccount.getAccountNumber();
        Transaction debit = new Transaction(debitAccount, debitName, amount, debitAccount.getBalance(), TransactionType.TRANSFER_DEBIT, transactionDate);
        Transaction credit = new Transaction(creditAccount, creditName, amount, creditAccount.getBalance(), TransactionType.TRANSFER_CREDIT, transactionDate);
        transactionRepository.saveAll(List.of(debit, credit));
        return List.of(debit, credit);
    }

    public List<Transaction> findByDateTimeBetween(Account accountNumber, LocalDateTime startPoint, LocalDateTime endPoint) throws ResponseStatusException {
        List<Transaction> transactionList = transactionRepository.findByTimeOfTrnsBetween(accountNumber, startPoint, endPoint);
        if (transactionList.isEmpty())
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, "No transactions found between: " + startPoint + " and " + endPoint);
        return transactionList;
    }

        public Transaction creditFunds(TransactionDTO transactionDTO) {
        Account creditAccount = findAccountTypeAndReturn(transactionDTO.getCreditAccountNumber());
        checkAccountStatus(creditAccount);
        creditAccount.credit(transactionDTO.getAmount());
        findAccountTypeAndSave(creditAccount);
        return createTransactionLog(creditAccount, transactionDTO);
    }


    public Transaction debitFunds(TransactionDTO transactionDTO) {
        Account debitAccount = findAccountTypeAndReturn(transactionDTO.getDebitAccountNumber());
        checkForFraud(transactionDTO);
        checkAccountStatus(debitAccount);
        debitAccount.debit(transactionDTO.getAmount());
        findAccountTypeAndSave(debitAccount);
        return createTransactionLog(debitAccount, transactionDTO);
    }


    public Transaction transferBetweenAccounts(TransactionDTO transactionDTO) {
        Account debitAccount = findAccountTypeAndReturn(transactionDTO.getDebitAccountNumber());
        Account creditAccount = findAccountTypeAndReturn(transactionDTO.getCreditAccountNumber());
        checkForFraud(transactionDTO);
        checkAccountStatus(debitAccount);
        checkAccountStatus(creditAccount);
        debitAccount.debit(transactionDTO.getAmount());
        creditAccount.credit(transactionDTO.getAmount());
        findAccountTypeAndSave(debitAccount);
        findAccountTypeAndSave(creditAccount);
        return createTransactionLogTransfer(debitAccount, transactionDTO.getAmount(), creditAccount).get(0);
    }

    // Converts LocalDate to LocalDateTime - Gets transaction from start of startDate to end of endDate
    public List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate) {
        Account checkingAccount = findAccountTypeAndReturn(accountNumber);
        LocalDateTime convertedStartDate = startDate.atStartOfDay();
        LocalDateTime convertedEndDate = endDate.atTime(23, 59, 59);
        return findByDateTimeBetween(checkingAccount, convertedStartDate, convertedEndDate);
    }

    public void checkForFraud(TransactionDTO transactionDTO) {
        Account account = findAccountTypeAndReturn(transactionDTO.getDebitAccountNumber());
        if (fraudDetection.fraudDetector(account, transactionDTO.getAmount())) {
            account.freezeAccount();
            findAccountTypeAndSave(account);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction Denied: Please contact us for details:");
        }
    }

    public void checkAccountStatus(Account checkingAccount) {
        if (checkingAccount.getStatus() == Status.FROZEN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction Denied: Please contact us for details:");
        }
    }

    public void findAccountTypeAndSave(Account account) {
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(account.getAccountNumber());
        if (checkingAccount.isPresent()) {
            checkingAccount.get().setBalance(penaltyCheck(account,
                    checkingAccount.get().getMinimumBalance(),
                    checkingAccount.get().getPenaltyFee()));
            checkingAccount.get().setStatus(account.getStatus());
            checkingAccountRepository.save(checkingAccount.get());
        }
        Optional<SavingsAccount> savingsAccount = savingsAccountRepository.findById(account.getAccountNumber());
        if (savingsAccount.isPresent()) {
            savingsAccount.get().setBalance(penaltyCheck(account,
                    savingsAccount.get().getMinimumBalance(),
                    savingsAccount.get().getPenaltyFee()));
            savingsAccount.get().setStatus(account.getStatus());
            savingsAccountRepository.save(savingsAccount.get());
        }
        Optional<StudentAccount> studentAccount = studentAccountRepository.findById(account.getAccountNumber());
        if (studentAccount.isPresent()) {
            studentAccount.get().setBalance(account.getBalance());
            studentAccount.get().setStatus(account.getStatus());
            studentAccountRepository.save(studentAccount.get());
        }
        Optional<CreditCard> creditCard = creditCardRepository.findById(account.getAccountNumber());
        if (creditCard.isPresent()) {
            creditCard.get().setBalance(account.getBalance());
            creditCard.get().setStatus(account.getStatus());
            creditCardRepository.save(creditCard.get());
        }
    }

    public Account findAccountTypeAndReturn(Long accountNumber) {
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(accountNumber);
        if (checkingAccount.isPresent()) {
            return checkingAccount.get();
        }
        Optional<SavingsAccount> savingsAccount = savingsAccountRepository.findById(accountNumber);
        if (savingsAccount.isPresent()) {
            return savingsAccount.get();
        }
        Optional<StudentAccount> studentAccount = studentAccountRepository.findById(accountNumber);
        if (studentAccount.isPresent()) {
            return studentAccount.get();
        }
        Optional<CreditCard> creditCard = creditCardRepository.findById(accountNumber);
        if (creditCard.isPresent()) {
            return creditCard.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found with Account Number: " + accountNumber);
        }
    }

    public void checkAvailableFunds(Account account, BigDecimal amount){
        if(account.getBalanceAsMoney().decreaseAmount(amount).compareTo(new BigDecimal("0")) < 0){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient Funds");
        }
    }

    public BigDecimal penaltyCheck(Account account, BigDecimal minimumBalance, BigDecimal penaltyFee){
        if(account.getBalance().compareTo(minimumBalance) < 0){
            account.debit(penaltyFee);
            TransactionDTO transactionDTO = new TransactionDTO(penaltyFee, null, TransactionType.PENALTY_FEE);
            return account.getBalance();
        }
        return account.getBalance();
    }

//    public BigDecimal interestCheck(Account account, BigDecimal interestRate){
//        if()
//    }

}
