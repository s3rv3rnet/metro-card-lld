package com.example.geektrust;

import java.math.BigDecimal;
import java.util.Map;

public class BalanceCommand implements Command {
    private String passengerName;
    private BigDecimal walletBalance;
    private Driver driver;

    BalanceCommand(Driver driver, String passengerName, BigDecimal walletBalance) {
        this.driver = driver;
        this.walletBalance = walletBalance;
        this.passengerName = passengerName;
    }

    @Override
    public void execute() {
        processBalance(passengerName, walletBalance);
    }

    public void processBalance(String passengerName, BigDecimal walletBalance) {
        Map<String, Passenger> passengers = driver.getPassengers();
        passengers.putIfAbsent(passengerName, new Passenger(passengerName, walletBalance));
    }
}
