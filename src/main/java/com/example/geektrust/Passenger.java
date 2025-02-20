package com.example.geektrust;

import java.math.BigDecimal;

public class Passenger {
    private BigDecimal walletBalance;
    private int journeyCnt;
    private StationName lastStation;
    private final String name;
    public Passenger(String name, BigDecimal walletBalance) {
        this.walletBalance = walletBalance;
        this.name = name;
    }
    public BigDecimal getWalletBalance() {
        return walletBalance;
    }
    public int getJourneyCnt() {
        return journeyCnt;
    }
    public StationName getLastStation() {
        return lastStation;
    }
    public void setLastStation(StationName lastStation) {
        this.lastStation = lastStation;
    }
    public void setJourneyCnt(int journeyCnt) {
        this.journeyCnt = journeyCnt;
    }
    public void setWalletBalance(BigDecimal walletBalance) {
        this.walletBalance = walletBalance;
    }
    public String getName() {
        return name;
    }
}
