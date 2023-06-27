package com.zemoso.seeder.util;

public enum CashKickStatus {

    PENDING("Pending"), APPROVED("Approved");

    private final String text;

    CashKickStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
