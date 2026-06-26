package com.example.exercice13.model;

import java.math.BigDecimal;

public class Account {

    private String number;
    private String holder;
    private BigDecimal balance;

    public Account() {
    }

    public Account(String number, String holder, BigDecimal balance) {
        this.number = number;
        this.holder = holder;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
