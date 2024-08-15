package pl.hexlin.finance;

import org.bson.Document;
import pl.hexlin.Instance;

import java.math.BigDecimal;

public class Investment {
    public Instance instance;
    public String companyTag;
    public double currentCompanyPrice;
    public int investmentPrice;
    public double companyPriceWhenInvested;
    public double priceChangePercent;

    public Investment(Instance instance, String companyTag, BigDecimal currentCompanyPrice, int investmentPrice, BigDecimal companyPriceWhenInvested, BigDecimal priceChangePercent) {
        this.instance = instance;
        this.companyTag = companyTag;
        this.currentCompanyPrice = currentCompanyPrice.doubleValue();
        this.investmentPrice = investmentPrice;
        this.companyPriceWhenInvested = companyPriceWhenInvested.doubleValue();
        this.priceChangePercent = priceChangePercent.doubleValue();
    }

    public Investment(Document document) {
        companyTag = document.getString("companyTag");
        currentCompanyPrice = document.getInteger("companyPrice");
        investmentPrice = document.getInteger("investmentPrice");
        companyPriceWhenInvested = document.getInteger("companyPriceThen");
        priceChangePercent = document.getInteger("priceChange");
    }
}
