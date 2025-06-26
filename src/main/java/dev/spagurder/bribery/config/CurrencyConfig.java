package dev.spagurder.bribery.config;

public class CurrencyConfig {

    public float bribeCredit;
    public float rejectionChanceModifier;

    public CurrencyConfig(float bribeCredit, float rejectionChanceModifier) {
        this.bribeCredit = bribeCredit;
        this.rejectionChanceModifier = rejectionChanceModifier;
    }

}
