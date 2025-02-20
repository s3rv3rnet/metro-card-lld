package com.example.geektrust; 

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.*;
import java.io.*;
import java.util.*;

class Station {
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
           passenger.setWalletBalance(walletBalance.subtract(charge));
        } else {
            this.totalAmount = this.totalAmount.add(charge);
            this.totalAmount = this.totalAmount.add(charge.subtract(walletBalance).
                    multiply(BigDecimal.valueOf(this.serviceFee)).
                    divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            passenger.setWalletBalance(BigDecimal.ZERO);
        }
    }
}
class Driver {
    Map<String, Passenger> passengers = new HashMap<>();
    Map<StationName, Station> stations = new HashMap<>();
    String inputFile;

    public Driver(String inputFile) {
        passengers = new HashMap<>();
        stations = new HashMap<>();
        for(StationName stationName : StationName.values()) {
            stations.put(stationName, new Station(stationName, 2));
        }
        this.inputFile = inputFile;
    }

    public void execute() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(this.inputFile))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                //Add your code here to process input commands
                String[] command = line.split(" ");
                switch (command[0]) {
                    case "BALANCE":
                        String passengerName = command[1];
                        passengers.putIfAbsent(passengerName, new Passenger(passengerName, BigDecimal.ZERO));
                        Passenger passenger = passengers.get(passengerName);
                        passenger.setWalletBalance(passenger.getWalletBalance().
                                add(BigDecimal.valueOf(Integer.parseInt(command[2]))));
                        break;
                    case "CHECK_IN":
                        Passenger passengerCheckIn = passengers.get(command[1]);
                        PassengerType passengerType = PassengerType.valueOf(command[2]);
                        StationName checkInStation = StationName.valueOf(command[3]);
                        stations.get(checkInStation).onBoardPassenger(passengerCheckIn, passengerType);
                        break;
                    case "PRINT_SUMMARY":
                        printSummary(stations);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    private void printSummary(Map<StationName, Station> stations) {
        for(StationName stationName : stations.keySet()) {
            Station station = stations.get(stationName);
            System.out.printf("TOTAL_COLLECTION %s %d %d%n",
                    stationName.toString(), station.getTotalAmount().intValue(), station.getTotalDiscount().intValue());
            System.out.println("PASSENGER_TYPE_SUMMARY");
            Map<PassengerType, Integer> passengerCnt = station.getPassengerCnt();
            // Convert Map to List of Entries
            List<Map.Entry<PassengerType, Integer>> list = new ArrayList<>(passengerCnt.entrySet());

            // Sort by value (descending), then by key name (lexicographically)
            list.sort(Comparator
                    .comparing(Map.Entry<PassengerType, Integer>::getValue, Comparator.reverseOrder())
                    .thenComparing(entry -> entry.getKey().name()));
            for(Map.Entry<PassengerType, Integer> passenger : list) {
                System.out.printf("%s %d%n", passenger.getKey(), passenger.getValue());
            }
        }
    }
}
public class Main {
    public static void main(String[] args) {
        Driver driver = new Driver(args[0]);
        driver.execute();
    }
}
