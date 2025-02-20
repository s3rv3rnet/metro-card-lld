package com.example.geektrust;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Driver {
    private final int SERVICE_FEE = 2;
    Map<String, Passenger> passengers = new HashMap<>();
    Map<StationName, Station> stations = new HashMap<>();
    String inputFile;

    public Driver(String inputFile) {
        passengers = new HashMap<>();
        stations = new HashMap<>();
        // initialize all stations
        for(StationName stationName : StationName.values()) {
            stations.put(stationName, new Station(stationName, SERVICE_FEE));
        }
        this.inputFile = inputFile;
    }

    public void execute() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(this.inputFile))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                // every line is a combination of command and arguments.
                String[] command = line.split(" ");
                switch (command[0]) {
                    case "BALANCE":
                        String passengerName = command[1];
                        passengers.putIfAbsent(passengerName, new Passenger(passengerName, BigDecimal.ZERO));
                        Passenger passenger = passengers.get(passengerName);
                        passenger.addWalletBalance(BigDecimal.valueOf(Integer.parseInt(command[2])));
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
