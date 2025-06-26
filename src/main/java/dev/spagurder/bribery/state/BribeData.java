package dev.spagurder.bribery.state;

public class BribeData {

    public boolean isBribed = false;
    public long bribedAt = 0L;
    public int bribeCredits = 0;

    public boolean isRejected = false;
    public long rejectedAt = 0L;

    public boolean isCoolingDown = false;

    public boolean isExtortionist = false;
    public boolean isExtorting = false;
    public long extortedAt = 0L;
    public int extortionBalance = 0;
    public int largestBribe = 0;

}
