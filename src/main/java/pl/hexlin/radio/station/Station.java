package pl.hexlin.radio.station;

public class Station {
    public final String stationName;
    public final String stationURL;

    public Station(String stationName, String stationURL) {
        this.stationName = stationName;
        this.stationURL = stationURL;
    }

    public String getStationName() {
        return stationName;
    }

    public String getStationURL() {
        return stationURL;
    }
}
