package pl.hexlin.radio;

import de.sfuhrm.radiobrowser4j.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class RadioBrowserManager {
    public RadioBrowser radioBrowser;
    public ArrayList<Station> stationSearchCache;

    public RadioBrowserManager() {
        Optional<String> endpoint = null;
        try {
            endpoint = new EndpointDiscovery("Demo agent/1.0").discover();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.stationSearchCache = new ArrayList<>();
        this.radioBrowser = new RadioBrowser(
                ConnectionParams.builder().apiUrl(endpoint.get()).userAgent("Demo agent/1.0").timeout(5000).build());
    }

    public void loadListByCountry(String CountryParameter) {
        stationSearchCache.clear();
        radioBrowser.listStationsBy(SearchMode.BYCOUNTRYCODEEXACT, CountryParameter, ListParameter.create().order(FieldName.NAME))
                .limit(100)
                .forEach(station -> {
                    stationSearchCache.add(station);
                });
    }

    public void loadListByNames(String NameParameter) {
        stationSearchCache.clear();
        radioBrowser.listStationsBy(SearchMode.BYNAME, NameParameter, ListParameter.create().order(FieldName.NAME))
                .limit(10)
                .forEach(station -> {
                    stationSearchCache.add(station);
                });
    }

    public Station getStationByName(String StationName) {
        return radioBrowser.listStationsBy(SearchMode.BYNAME, StationName, ListParameter.create().order(FieldName.NAME)).findFirst().get();
    }
}