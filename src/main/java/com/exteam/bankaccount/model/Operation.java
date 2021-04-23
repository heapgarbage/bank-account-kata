package com.exteam.bankaccount.model;

import lombok.*;

import java.time.Instant;

/**
 * Operation class.
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Operation {

    private Instant date;
    private long amount;
    private OperationType type;

    /**
     * All available operation types.
     */
    public enum OperationType {
        DEPOSIT, WITHDRAWAL
    }
}
