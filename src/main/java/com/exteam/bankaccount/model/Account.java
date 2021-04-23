package com.exteam.bankaccount.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Account class.
 */

@Getter
@Setter
@EqualsAndHashCode
public class Account {

    private static Account uniqueInstance;

    private long balance;
    private List<Operation> history = new ArrayList<>();

    private Account(){}

    public static Account getInstance(){
        if(uniqueInstance == null) {
            uniqueInstance = new Account();
        }
        return uniqueInstance;
    }

}
