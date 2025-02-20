package com.example.geektrust;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PrintSummaryCommand implements Command {
    private Map<StationName, Station> stations;
    public PrintSummaryCommand(Map<StationName, Station> stations) {
        this.stations = stations;
    }
    @Override
    public void execute() {
        printSummary(stations);
    }

    public void printSummary(Map<StationName, Station> stations) {
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
