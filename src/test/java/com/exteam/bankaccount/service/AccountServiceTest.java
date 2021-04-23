package com.exteam.bankaccount.service;

import com.exteam.bankaccount.exception.AccountServiceException;
import com.exteam.bankaccount.model.Account;
import com.exteam.bankaccount.model.Operation;
import org.assertj.core.groups.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests class for {@link AccountService}.
 */
public class AccountServiceTest {

    private AccountService accountService;

    @Before
    public void before() {
        accountService = new AccountService();
    }

    @Test
    public void should_update_account_when_deposit() throws AccountServiceException {
        //Given
        long amount = 500;
        Instant now = Instant.now();

        //when
        accountService.makeDeposit(amount);

        //Then
        assertThat(Account.getInstance())
                .extracting(Account::getBalance).isEqualTo(amount);

        assertThat(Account.getInstance().getHistory())
                .extracting(Operation::getAmount, Operation::getType)
                .containsExactly(Tuple.tuple(amount, Operation.OperationType.DEPOSIT));

        assertThat(Account.getInstance().getHistory())
                .extracting(Operation::getDate)
                .allSatisfy(date -> assertThat(date).isAfterOrEqualTo(now));

    }

    @Test
    public void should_update_account_when_successive_deposits() throws AccountServiceException {
        //Given
        long amount1 = 500;
        long amount2 = 200;
        Instant now = Instant.now();

        //when
        accountService.makeDeposit(amount1);
        accountService.makeDeposit(amount2);

        //Then
        Account account = Account.getInstance();
        assertThat(account)
                .extracting(Account::getBalance).isEqualTo(amount1 + amount2);

        assertThat(account.getHistory())
                .extracting(Operation::getAmount, Operation::getType)
                .containsExactly(Tuple.tuple(amount1, Operation.OperationType.DEPOSIT),
                        Tuple.tuple(amount2, Operation.OperationType.DEPOSIT));

        assertThat(account.getHistory())
                .extracting(Operation::getDate)
                .allSatisfy(date -> assertThat(date).isAfterOrEqualTo(now));

    }

    @Test
    public void should_not_update_account_and_throw_exception_when_deposit_amount_is_not_positive() {
        //Given
        long amount = -5;

        //when, then
        Account account = Account.getInstance();

        assertThat(account)
                .extracting(Account::getBalance).isEqualTo(0L);

        assertThat(account.getHistory()).isEmpty();

        assertThatThrownBy(() -> accountService.makeDeposit(amount))
                .isInstanceOf(AccountServiceException.class)
                .hasMessage(String.format(accountService.DEPOSIT_EXCEPTION_MESSAGE_TEMPLATE, amount));
    }

    @After
    public void after() {
        Account.getInstance().setBalance(0);
        Account.getInstance().setHistory(new ArrayList<>());
    }

}
