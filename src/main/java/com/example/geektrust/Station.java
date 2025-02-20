package com.example.geektrust;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Station {
    private BigDecimal totalAmount;
    private final StationName name;
    private Map<PassengerType, Integer> passengerCnt;
    private BigDecimal totalDiscount;
    private final int serviceFee;
    public Station(StationName name, int serviceFee) {
        this.totalAmount = BigDecimal.ZERO;
        this.name = name;
        this.serviceFee = serviceFee;
        this.totalDiscount = BigDecimal.ZERO;
        this.passengerCnt = new HashMap<>();
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Map<PassengerType, Integer> getPassengerCnt() {
        return passengerCnt;
    }
    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void onBoardPassenger(Passenger passenger, PassengerType passengerType) {
        //incrementing the passenger count by 1 for that passenger type.
        this.passengerCnt.merge(passengerType, 1, Integer::sum);

        BigDecimal walletBalance = passenger.getWalletBalance();
        BigDecimal charge = passengerType.getCharge();

        if(passenger.getLastStation() != this.name && passenger.getJourneyCnt()%2 != 0) {
            charge = charge.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            this.totalDiscount = this.totalDiscount.add(charge);
        }

        passenger.setLastStation(this.name);
        passenger.setJourneyCnt(passenger.getJourneyCnt()+1);

        if(walletBalance.compareTo(charge) > 0) {
            this.totalAmount = this.totalAmount.add(charge);
            passenger.subtractWalletBalance(charge);
        } else {
            this.totalAmount = this.totalAmount.add(charge);
            this.totalAmount = this.totalAmount.add(charge.subtract(walletBalance).
                    multiply(BigDecimal.valueOf(this.serviceFee)).
                    divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            passenger.subtractWalletBalance(walletBalance);
        }
    }
}
