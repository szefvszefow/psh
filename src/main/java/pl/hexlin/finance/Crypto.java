package pl.hexlin.finance;

public class Crypto {
    public String cryptoName;
    public double cryptoRate;
    public String cryptoIcon;
    public String hexColor;

    public Crypto(String cryptoName, Double cryptoRate, String cryptoIcon, String hexColor) {
        this.cryptoName = cryptoName;
        this.cryptoRate = cryptoRate;
        this.cryptoIcon = cryptoIcon;
        this.hexColor = hexColor;
    }

    public void setCryptoRate(double cryptoRate) {
        this.cryptoRate = cryptoRate;
    }
}
