package pl.hexlin.radio.station;

import java.util.HashMap;
import java.util.Map;

public class StationManager {
    public final Map<String, Station> stationMap;

    public StationManager() {
        this.stationMap = new HashMap<>();
    }

    public void addStation(String name, Station station) {
        stationMap.put(name, station);
    }

    public Station getStation(String name) {
        return stationMap.get(name);
    }
}
