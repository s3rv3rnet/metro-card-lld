package com.example.geektrust;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Driver {
    Map<String, Passenger> passengers = new HashMap<>();
    Map<StationName, Station> stations = new HashMap<>();
    String inputFile;

    public Driver(String inputFile) {
        passengers = new HashMap<>();
        stations = new HashMap<>();
        this.inputFile = inputFile;
    }

    public Map<StationName, Station> getStations() {
        return stations;
    }

    public Map<String, Passenger> getPassengers() {
        return passengers;
    }

    public void execute() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(this.inputFile))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                // every line is a combination of command and arguments.
                String[] request = line.split(" ");
                Command command = null;
                switch (request[0]) {
                    case "BALANCE":
                        command = new BalanceCommand(this, request[1], new BigDecimal(request[2]));
                        break;
                    case "CHECK_IN":
                        command = new CheckInCommand(this, request[1], PassengerType.valueOf(request[2]), StationName.valueOf(request[3]));
                        break;
                    case "PRINT_SUMMARY":
                        command = new PrintSummaryCommand(stations);
                }
                command.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
