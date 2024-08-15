package pl.hexlin.finance;

import pl.hexlin.Instance;

import java.util.HashMap;

public class InvestmentManager {
    public final Instance instance;
    public final HashMap<String, Crypto> cryptoMap;

    public InvestmentManager(Instance instance) {
        this.instance = instance;
        this.cryptoMap = new HashMap<>();
    }
}
