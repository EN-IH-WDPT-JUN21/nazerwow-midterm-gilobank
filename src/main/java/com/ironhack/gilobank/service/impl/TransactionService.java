package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.BalanceDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Role;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.security.CustomUserDetails;
import com.ironhack.gilobank.security.IAuthenticationFacade;
import com.ironhack.gilobank.service.interfaces.*;
import com.ironhack.gilobank.utils.FraudDetection;
import com.ironhack.gilobank.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ICheckingAccountService checkingAccountService;
    @Autowired
    private ISavingsAccountService savingsAccountService;
    @Autowired
    private IStudentAccountService studentAccountService;
    @Autowired
    private ICreditCardService creditCardService;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired
    private FraudDetection fraudDetection;


    // This will create a transaction Log which includes Account Number, Amount in Dollars and Transaction Type
    // Balance after the transaction and Date
    public Transaction createTransactionLog(Account account, TransactionDTO transactionDTO) {
        LocalDateTime transactionDate = LocalDateTime.now();
        if (transactionDTO.getTimeOfTrns() != null) transactionDate = transactionDTO.getTimeOfTrns();
        String transactionName = transactionDTO.getAmount() + " " + transactionDTO.getType().toString();
        Transaction transaction = new Transaction(account, transactionName, transactionDTO.getAmount(), account.getBalance(), transactionDTO.getType(), transactionDate);
        transactionRepository.save(transaction);
        return transaction;
    }

    // This will create a transaction Log which includes Account Number, Amount in Dollars and Transaction Type
    // Balance after the transaction and Date On Both the Sending and Receiving Accounts
    public List<Transaction> createTransactionLogTransfer(Account debitAccount, Money amount, Account creditAccount) {
        LocalDateTime transactionDate = LocalDateTime.now();
        String debitName = amount + " Transfer to Account Number: " + creditAccount.getAccountNumber();
        String creditName = amount + " Transfer from Account Number: " + debitAccount.getAccountNumber();
        Transaction debit = new Transaction(debitAccount, debitName, amount, debitAccount.getBalance(), TransactionType.TRANSFER_DEBIT, transactionDate);
        Transaction credit = new Transaction(creditAccount, creditName, amount, debitAccount.getBalance(), TransactionType.TRANSFER_CREDIT, transactionDate);
        transactionRepository.saveAll(List.of(debit, credit));
        return List.of(debit, credit);
    }

    // This will create a transaction Log which includes Account Number, Amount in Dollars and Transaction Type
    // Balance after the transaction and Date On Both the Sending and Receiving Accounts
    // This one allows you to input a date if you wish
    public List<Transaction> createTransactionLogTransfer(Account debitAccount, Money amount, Account creditAccount, LocalDateTime transactionDate) {
        String debitName = amount + " Transfer to Account Number: " + creditAccount.getAccountNumber();
        String creditName = amount + " Transfer from Account Number: " + debitAccount.getAccountNumber();
        Transaction debit = new Transaction(debitAccount, debitName, amount, debitAccount.getBalance(), TransactionType.TRANSFER_DEBIT, transactionDate);
        Transaction credit = new Transaction(creditAccount, creditName, amount, creditAccount.getBalance(), TransactionType.TRANSFER_CREDIT, transactionDate);
        transactionRepository.saveAll(List.of(debit, credit));
        return List.of(debit, credit);
    }

    // This will find all transactions between the given date period
    public List<Transaction> findByDateTimeBetween(Account accountNumber, LocalDateTime startPoint, LocalDateTime endPoint) throws ResponseStatusException {
        List<Transaction> transactionList = transactionRepository.findByTimeOfTrnsBetween(accountNumber, startPoint, endPoint);
        if (transactionList.isEmpty())
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, "No transactions found between: " + startPoint + " and " + endPoint);
        return transactionList;
    }

    // This credits funds - It completes a fraud and status check before crediting
    public Transaction creditFunds(TransactionDTO transactionDTO) {
        Account creditAccount = findAccountTypeAndReturn(transactionDTO.getCreditAccountNumber());
        checkForFraud_CreditAccount(transactionDTO);
        checkAccountStatus(creditAccount);
        creditAccount.getBalance().increaseAmount(transactionDTO.getAmount());
        findAccountTypeAndSave(creditAccount);
        return createTransactionLog(creditAccount, transactionDTO);
    }

    // This debits funds - It completes a fraud and status check before debiting
    public Transaction debitFunds(TransactionDTO transactionDTO) {
        Account debitAccount = findAccountTypeAndReturn(transactionDTO.getDebitAccountNumber());
        checkForFraud_DebitAccount(transactionDTO);
        checkAccountStatus(debitAccount);
        checkAvailableFunds(debitAccount, transactionDTO.getAmount());
        debitAccount.getBalance().decreaseAmount(transactionDTO.getAmount().getAmount().abs());
        findAccountTypeAndSave(debitAccount);
        return createTransactionLog(debitAccount, transactionDTO);
    }

    // This Transfers funds - It completes a fraud and status check before transfering
    public Transaction transferBetweenAccounts(TransactionDTO transactionDTO) {
        Account debitAccount = findAccountTypeAndReturn(transactionDTO.getDebitAccountNumber());
        Account creditAccount = findAccountTypeAndReturn(transactionDTO.getCreditAccountNumber());
        checkForFraud_CreditAccount(transactionDTO);
        checkForFraud_DebitAccount(transactionDTO);
        checkAccountStatus(debitAccount);
        checkAccountStatus(creditAccount);
        checkAvailableFunds(debitAccount, transactionDTO.getAmount());
        debitAccount.getBalance().decreaseAmount(transactionDTO.getAmount());
        creditAccount.getBalance().increaseAmount(transactionDTO.getAmount());
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

    // This calls the fraud detector on the credit account number - Will freeze account if detected
    public void checkForFraud_CreditAccount(TransactionDTO transactionDTO) {
        Account creditAccount = findAccountTypeAndReturn(transactionDTO.getCreditAccountNumber());
        if (fraudDetection.fraudDetector(creditAccount, transactionDTO.getAmount().getAmount())) {
            creditAccount.freezeAccount();
            findAccountTypeAndSave(creditAccount);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction Denied: Please contact us for details:");
        }
    }
    // This calls the fraud detector on the Debit account number - Will freeze account if detected
    public void checkForFraud_DebitAccount(TransactionDTO transactionDTO) {
        Account debitAccount = findAccountTypeAndReturn(transactionDTO.getDebitAccountNumber());
        if (fraudDetection.fraudDetector(debitAccount, transactionDTO.getAmount().getAmount())) {
            debitAccount.freezeAccount();
            findAccountTypeAndSave(debitAccount);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction Denied: Please contact us for details:");
        }
    }

    // Checks if account is frozen before proceeding.
    public void checkAccountStatus(Account checkingAccount) {
        if (checkingAccount.getStatus() == Status.FROZEN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction Denied: Please contact us for details:");
        }
    }

    // Finds type of account and then saves it
    public void findAccountTypeAndSave(Account account) {
        Optional<CheckingAccount> checkingAccount = checkingAccountService.findByAccountNumberOptional(account.getAccountNumber());
        if (checkingAccount.isPresent()) {
            // If balance equal or above minimum balance penalty checker will be called
            // If balance already below minimum balance the customer will not be charged again for the new transaction
            if (checkingAccount.get().getBalance().getAmount().compareTo(checkingAccount.get().getMinimumBalance().getAmount()) >= 0) {
                checkingAccount.get().setBalance(penaltyCheck(account,
                        checkingAccount.get().getMinimumBalance(),
                        checkingAccount.get().getPenaltyFee()));
            }
            checkingAccount.get().setStatus(account.getStatus());
            checkingAccountService.saveCheckingAccount(checkingAccount.get());
        }
        Optional<SavingsAccount> savingsAccount = savingsAccountService.findByAccountNumberOptional(account.getAccountNumber());
        if (savingsAccount.isPresent()) {
            // If balance equal or above minimum balance penalty checker will be called
            // If balance already below minimum balance the customer will not be charged again for the new transaction
            if (savingsAccount.get().getBalance().getAmount().compareTo(savingsAccount.get().getMinimumBalance().getAmount()) >= 0) {
                savingsAccount.get().setBalance(penaltyCheck(account,
                        savingsAccount.get().getMinimumBalance(),
                        savingsAccount.get().getPenaltyFee()));
            }
            savingsAccount.get().setStatus(account.getStatus());
            savingsAccountService.saveNewSavingsAccount(savingsAccount.get());
        }
        Optional<StudentAccount> studentAccount = studentAccountService.findByAccountNumberOptional(account.getAccountNumber());
        if (studentAccount.isPresent()) {
            studentAccount.get().setBalance(account.getBalance());
            studentAccount.get().setStatus(account.getStatus());
            studentAccountService.saveNewStudentAccount(studentAccount.get());
        }
        Optional<CreditCard> creditCard = creditCardService.findByAccountNumberOptional(account.getAccountNumber());
        if (creditCard.isPresent()) {
            creditCard.get().setBalance(account.getBalance());
            creditCard.get().setStatus(account.getStatus());
            creditCardService.saveNewCreditCard(creditCard.get());
        }
    }
    // Checks all account repositories and returns the account if found
    public Account findAccountTypeAndReturn(Long accountNumber) {
        Optional<CheckingAccount> checkingAccount = checkingAccountService.findByAccountNumberOptional(accountNumber);
        if (checkingAccount.isPresent()) {
            return checkingAccount.get();
        }
        Optional<SavingsAccount> savingsAccount = savingsAccountService.findByAccountNumberOptional(accountNumber);
        if (savingsAccount.isPresent()) {
            return savingsAccount.get();
        }
        Optional<StudentAccount> studentAccount = studentAccountService.findByAccountNumberOptional(accountNumber);
        if (studentAccount.isPresent()) {
            return studentAccount.get();
        }
        Optional<CreditCard> creditCard = creditCardService.findByAccountNumberOptional(accountNumber);
        if (creditCard.isPresent()) {
            return creditCard.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found with Account Number: " + accountNumber);
        }
    }

    // Checks if the account has sufficient funds before debiting account
    public void checkAvailableFunds(Account account, Money amount) {
        if (account.getBalance().getAmount().abs().subtract(amount.getAmount()).compareTo(new BigDecimal("0.00")) < 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient Funds");
        }
    }
    // Checks if penalty should be applied
    public Money penaltyCheck(Account account, Money minimumBalance, Money penaltyFee) {
        if (account.getBalance().getAmount().compareTo(minimumBalance.getAmount()) < 0) {
            account.getBalance().decreaseAmount(penaltyFee);
            TransactionDTO transactionDTO = new TransactionDTO(penaltyFee, null, TransactionType.PENALTY_FEE);
            return account.getBalance();
        }
        return account.getBalance();
    }

    // Checks if account is eligible for new monthly interest payment - Yes (True) or No (false)
    public boolean interestMonthlyCheck(Long accountNumber) {
        Account account = findAccountTypeAndReturn(accountNumber);
        List<Timestamp> interestPayments = transactionRepository.interestMonth(account);
        if (interestPayments.isEmpty() && account.getOpenDate().isBefore(LocalDate.now().minusMonths(1))) {
            return true;
        } else if (interestPayments.isEmpty()) {
            return false;
        }
        LocalDate timeOfLastPayment = interestPayments.get(interestPayments.size() - 1).toLocalDateTime().toLocalDate();
        return timeOfLastPayment.isBefore(LocalDate.now().minusMonths(1));
    }
    // Checks if account is eligible for new yearly interest payment - Yes (True) or No (false)
    public boolean interestYearlyCheck(Long accountNumber) {
        Account account = findAccountTypeAndReturn(accountNumber);
        List<Timestamp> interestPayments = transactionRepository.interestYear(account);
        if (interestPayments.isEmpty() && account.getOpenDate().isBefore(LocalDate.now().minusYears(1))) {
            return true;
        } else if (interestPayments.isEmpty()) {
            return false;
        }
        LocalDate timeOfLastPayment = interestPayments.get(interestPayments.size() - 1).toLocalDateTime().toLocalDate();
        return timeOfLastPayment.isBefore(LocalDate.now().minusYears(1));
    }

    // Applies yearly interest payment
    public void applyInterestYearly(Long accountNumber, Money balance, BigDecimal interestRate) {
        if (interestYearlyCheck(accountNumber)) {
            Money interestDue = new Money(balance.getAmount().multiply(interestRate));
            if (balance.getAmount().compareTo(new BigDecimal("0")) > 0) {
                TransactionDTO transactionDTO = new TransactionDTO(accountNumber, interestDue, TransactionType.INTEREST_CREDIT);
                creditFunds(transactionDTO);
            } else if (balance.getAmount().compareTo(new BigDecimal("0")) < 0) {
                TransactionDTO transactionDTO = new TransactionDTO(interestDue, accountNumber, TransactionType.INTEREST_DEBIT);
                debitFunds(transactionDTO);
            }
        }
    }

    // Applies monthly interest payment
    public void applyInterestMonthly(Long accountNumber, Money balance, BigDecimal interestRate) {
        if (interestMonthlyCheck(accountNumber)) {
            Money interestDue = new Money(balance.getAmount().multiply((interestRate.divide(new BigDecimal("12"), RoundingMode.HALF_EVEN))));
            if (balance.getAmount().compareTo(new BigDecimal("0")) > 0) {
                TransactionDTO transactionDTO = new TransactionDTO(accountNumber, interestDue, TransactionType.INTEREST_CREDIT);
                creditFunds(transactionDTO);
            } else if (balance.getAmount().compareTo(new BigDecimal("0")) < 0) {
                TransactionDTO transactionDTO = new TransactionDTO(interestDue, accountNumber, TransactionType.INTEREST_DEBIT);
                debitFunds(transactionDTO);
            }
        }
    }

    // Checks if logged in user is authorised to access the data
    public boolean checkAuthentication(Long accountNumber) {
        Account account = findAccountTypeAndReturn(accountNumber);
        Object loggedInUser = authenticationFacade.getPrincipal();
        Long loggedInId;
        if (loggedInUser instanceof CustomUserDetails) {
            if (authenticationFacade.getPrincipalRole() == Role.ADMIN) return true;
            loggedInId = authenticationFacade.getPrincipalId();
            if (Objects.equals(loggedInId, account.getPrimaryHolder().getLoginDetails().getId())) {
                return true;
            } else if (account.getSecondaryHolder() != null &&
                    Objects.equals(loggedInId, account.getSecondaryHolder().getLoginDetails().getId())) {
                return true;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to access this information");
            }
        }
        throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "How did you get here!?");
    }

    // Verifies the logged in third party
    public boolean verifyThirdParty(String hashedKey) {
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);
        Object loggedInUser = authenticationFacade.getPrincipal();
        if (thirdParty.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Hashed Key");
        }
        if (loggedInUser instanceof CustomUserDetails) {
            if (authenticationFacade.getPrincipalRole() == Role.ADMIN) return true;
            if (authenticationFacade.getPrincipalRole() != Role.THIRDPARTY)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Role");
            String loggedInHashedKey = authenticationFacade.getHashedKey();
            return Objects.equals(thirdParty.get().getHashedKey(), loggedInHashedKey);
        }
        return false;
    }

    // Verifies whether inputted secret key matches the accounts secretkey
    public boolean verifySecretKey(String secretKey, Account account) {
        System.out.println(secretKey);
        System.out.println(account.getSecretKey());
        if (Objects.equals(account.getSecretKey(), secretKey)) {
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Secret key: " + secretKey +
                    " does not match account: " + account.getAccountNumber());
        }
    }

    // Creates a balanctDTO and returns it
    public BalanceDTO getBalance(Account account) {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setAccountNumber(account.getAccountNumber());
        balanceDTO.setBalance(account.getBalance());
        return balanceDTO;
    }
}
