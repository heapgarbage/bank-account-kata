package com.exteam.bankaccount.service;

import com.exteam.bankaccount.exception.AccountServiceException;
import com.exteam.bankaccount.model.Account;
import com.exteam.bankaccount.model.Operation;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Service class for {@link Account}.
 */
@NoArgsConstructor
public class AccountService {

    public static final String DEPOSIT_EXCEPTION_MESSAGE_TEMPLATE = "The amount %s cannot be deposited. Please ensure the amount is positive";
    public static final String WITHDRAWAL_EXCEPTION_MESSAGE_TEMPLATE = "The amount %s cannot be withdraw. Your balance is %s.";

    /**
     * Adds add an amount to the current balance and a new operation to the history.
     *
     * @param amount
     *             The amount to add to the current balance.
     *
     * @throws AccountServiceException
     *             If the makeDeposit amount is not positive.
     */
    public void makeDeposit(long amount) throws AccountServiceException {
        if(amount <= 0) {
            String message = String.format(DEPOSIT_EXCEPTION_MESSAGE_TEMPLATE, amount);
            throw new AccountServiceException(message);
        }
        updateAccount(amount, Operation.OperationType.DEPOSIT);
    }

    /**
     * Removes an amount from the current balance and add a new operation to the history.
     *
     * @param amount
     *             The amount to remove from the current balance.
     *
     * @throws AccountServiceException
     *             If the amount is not greater than the balance.
     */
    public void makeWithdrawal(long amount) throws AccountServiceException {
        long currentBalance = Account.getInstance().getBalance();
        if(amount > currentBalance) {
            String message = String.format(WITHDRAWAL_EXCEPTION_MESSAGE_TEMPLATE, amount, currentBalance);
            throw new AccountServiceException(message);
        }
        updateAccount(amount, Operation.OperationType.WITHDRAWAL);
    }

    private void updateAccount(long amount, Operation.OperationType operationType) {
        Account account = Account.getInstance();
        if (operationType == Operation.OperationType.DEPOSIT) {
            account.setBalance(account.getBalance() + amount);
        } else {
            account.setBalance(account.getBalance() - amount);
        }
        account.getHistory().add(new Operation(Instant.now(), amount, operationType));
    }

    /**
     * Return the operations of the account.
     */
    public List<Operation> checkOperations() {
        return Account.getInstance().getHistory();
    }
}
