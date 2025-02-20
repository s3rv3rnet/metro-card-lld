package com.example.geektrust;

import java.util.Map;

public class CheckInCommand implements Command{
    private final int SERVICE_FEE = 2;
    private String passengerName;
    private PassengerType passengerType;
    private StationName stationName;
    private Driver driver;

    CheckInCommand(Driver driver, String passengerName, PassengerType passengerType, StationName stationName) {
        this.passengerName = passengerName;
        this.passengerType = passengerType;
        this.stationName = stationName;
        this.driver = driver;
    }

    @Override
    public void execute() {
        processCheckIn(passengerName, passengerType, stationName);
    }

    public void processCheckIn(String passengerName, PassengerType passengerType, StationName stationName) {
        Map<StationName, Station> stations = driver.getStations();
        Map<String, Passenger> passengers = driver.getPassengers();
        stations.putIfAbsent(stationName, new Station(stationName, SERVICE_FEE));
        Passenger passengerCheckIn = passengers.get(passengerName);
        stations.get(stationName).onBoardPassenger(passengerCheckIn, passengerType);
    }
}
