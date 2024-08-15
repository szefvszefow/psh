package pl.hexlin.http;

import okhttp3.*;
import org.json.JSONObject;
import pl.hexlin.Instance;
import pl.hexlin.finance.Crypto;
import pl.hexlin.quest.Country;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class ApiHandler {
    public final OkHttpClient client;
    public final Instance instance;

    public ApiHandler(Instance instance) {
        this.instance = instance;
        this.client = new OkHttpClient();
    }

    public void retreiveCountry(String countryIso, String countryName, ArrayList<Country> selectedCountryList) throws IOException {
        Request request = new Request.Builder()
                .url("https://restfulcountries.com/api/v1/countries?iso2=" + countryIso)
                .addHeader("Authorization", " Bearer 626|nylglKY9TMTqVQc7SREfVb0T7nC6FSyFqIoPx60q")
                .build();


        Response response = client.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        JSONObject countryObject = jsonObject.getJSONObject("data");

        String capital = countryObject.getString("capital");
        String flagUrl = countryObject.getJSONObject("href").getString("flag");
        String iso2 = countryObject.getString("iso2");

        String random = String.valueOf(new Random().nextInt(3223));
        File downloadedFile;
        try (InputStream in = new URL(flagUrl).openStream()) {
            String fileName = random + ".jpg";
            Path filePath = Paths.get(fileName);
            Files.copy(in, filePath);

            downloadedFile = filePath.toFile();
        }

        selectedCountryList.add(new Country(null, countryName, capital, flagUrl, iso2, downloadedFile.getAbsolutePath()));
    }

    public void retreiveCrypto(String cryptoCode) throws IOException {
        String json = "{\"currency\":\"USD\",\"code\":\"ETH\",\"meta\":true}".replace("code", cryptoCode);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url("https://api.livecoinwatch.com/coins/single")
                .addHeader("x-api-key", "037be6e6-b453-4a72-a790-f7019a817b1c")
                .post(body)
                .build();


        Response response = client.newCall(request).execute();

        if (response.body().string().contains("Unknown coin code")) {
            return;
        }

        JSONObject jsonObject = new JSONObject(response.body().string());

        String cryptoName = jsonObject.getString("name");
        String cryptoRate = jsonObject.getString("rate");
        String iconUrl = jsonObject.getString("png64");
        String hexColor = jsonObject.getString("color");

        instance.getInvestmentManager().cryptoMap.put(cryptoCode, new Crypto(cryptoName, Double.valueOf(cryptoRate), iconUrl, hexColor));
    }
}
