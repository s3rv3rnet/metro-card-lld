package com.example.geektrust;

import java.math.BigDecimal;

public enum PassengerType {
    ADULT(new BigDecimal("200")),
    SENIOR_CITIZEN(new BigDecimal("100")),
    KID(new BigDecimal("50"));
    private final BigDecimal charge;
    private PassengerType(BigDecimal charge) {
        this.charge = charge;
    }
    public BigDecimal getCharge() {
        return charge;
    }
}
