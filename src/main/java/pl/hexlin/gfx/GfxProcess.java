package pl.hexlin.gfx;

import pl.hexlin.voting.Election;

import java.io.File;

public class GfxProcess {
    public String userId;
    public GfxStage gfxStage;
    public String flagURL;
    public String dictatorURL;
    public String mapURL;
    public String countryName;
    public String rulingParty;
    public String dictatorName;
    public String allianceName;
    public String militaryCount;
    public String ideologyName;
    public File file;

    public GfxProcess(String userId, GfxStage gfxStage, String flagURL, String dictatorURL, String mapURL, String countryName, String rulingParty, String dictatorName, String allianceName, String militaryCount, String ideologyName, File file) {
        this.userId = userId;
        this.gfxStage = gfxStage;
        this.flagURL = flagURL;
        this.dictatorURL = dictatorURL;
        this.mapURL = mapURL;
        this.countryName = countryName;
        this.rulingParty = rulingParty;
        this.dictatorName = dictatorName;
        this.allianceName = allianceName;
        this.militaryCount = militaryCount;
        this.ideologyName = ideologyName;
        this.file = file;
    }
    public void setGfxStage(GfxStage gfxStage) {
        this.gfxStage = gfxStage;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getUserId() {
        return userId;
    }

    public GfxStage getGfxStage() {
        return gfxStage;
    }

    public void setFlagURL(String flagURL) {
        this.flagURL = flagURL;
    }

    public void setDictatorURL(String dictatorURL) {
        this.dictatorURL = dictatorURL;
    }

    public void setMapURL(String mapURL) {
        this.mapURL = mapURL;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setRulingParty(String rulingParty) {
        this.rulingParty = rulingParty;
    }

    public void setDictatorName(String dictatorName) {
        this.dictatorName = dictatorName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    public void setMilitaryCount(String militaryCount) {
        this.militaryCount = militaryCount;
    }

    public void setIdeologyName(String ideologyName) {
        this.ideologyName = ideologyName;
    }

    public String getFlagURL() {
        return flagURL;
    }

    public String getDictatorURL() {
        return dictatorURL;
    }

    public String getMapURL() {
        return mapURL;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getRulingParty() {
        return rulingParty;
    }

    public String getDictatorName() {
        return dictatorName;
    }

    public String getAllianceName() {
        return allianceName;
    }

    public String getMilitaryCount() {
        return militaryCount;
    }

    public String getIdeologyName() {
        return ideologyName;
    }
}
