package com.exteam.bankaccount.service;

import com.exteam.bankaccount.exception.AccountServiceException;
import com.exteam.bankaccount.model.Account;
import com.exteam.bankaccount.model.Operation;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Service class for {@link Account}.
 */
@NoArgsConstructor
public class AccountService {

    public static final String DEPOSIT_EXCEPTION_MESSAGE_TEMPLATE = "The amount %s cannot be deposited. Please ensure the amount is positive";

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

    private void updateAccount(long amount, Operation.OperationType operationType) {
        Account account = Account.getInstance();
        if (operationType == Operation.OperationType.DEPOSIT) {
            account.setBalance(account.getBalance() + amount);
        }
        account.getHistory().add(new Operation(Instant.now(), amount, operationType));
    }
}
