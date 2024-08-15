package pl.hexlin.quest;

public class Country {
    public String messageId;
    public final String countryName;
    public final String capitalName;
    public final String flagURL;
    public final String countryTag;
    public final String flagDir;

    public Country(String messageId, String countryName, String capitalName, String flagURL, String countryTag, String flagDir) {
        this.messageId = messageId;
        this.countryName = countryName;
        this.capitalName = capitalName;
        this.flagURL = flagURL;
        this.countryTag = countryTag;
        this.flagDir = flagDir;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCapitalName() {
        return capitalName;
    }
    public String getFlagURL() {
        return flagURL;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
