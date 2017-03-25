package cc.lupine.quickbid;

/**
 * Created by maciej on 25/03/2017.
 */

public class AuctionModel {
    private String id;
    private String name;
    private Boolean buyNow;
    private Boolean auction;
    private double bidPrice;
    private double buyNowPrice;
    private long secondsLeft;
    private long endingTime;
    private Boolean tillOnStock;
    private Boolean allegroStandard;
    private int bidsCount;
    private String imageSmall;
    private String imageMedium;
    private String imageLarge;
    private String sellerName;

    public AuctionModel(String id, String name, Boolean buyNow, Boolean auction, double bidPrice, double buyNowPrice, long secondsLeft, long endingTime, Boolean tillOnStock, Boolean allegroStandard, int bidsCount, String imageSmall, String imageMedium, String imageLarge, String sellerName) {
        this.id = id;
        this.name = name;
        this.buyNow = buyNow;
        this.auction = auction;
        this.bidPrice = bidPrice;
        this.buyNowPrice = buyNowPrice;
        this.secondsLeft = secondsLeft;
        this.endingTime = endingTime;
        this.tillOnStock = tillOnStock;
        this.allegroStandard = allegroStandard;
        this.bidsCount = bidsCount;
        this.imageSmall = imageSmall;
        this.imageMedium = imageMedium;
        this.imageLarge = imageLarge;
        this.sellerName = sellerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBuyNow() {
        return buyNow;
    }

    public void setBuyNow(Boolean buyNow) {
        this.buyNow = buyNow;
    }

    public Boolean getAuction() {
        return auction;
    }

    public void setAuction(Boolean auction) {
        this.auction = auction;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(double buyNowPrice) {
        this.buyNowPrice = buyNowPrice;
    }

    public long getSecondsLeft() {
        return secondsLeft;
    }

    public void setSecondsLeft(long secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    public long getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(long endingTime) {
        this.endingTime = endingTime;
    }

    public Boolean getTillOnStock() {
        return tillOnStock;
    }

    public void setTillOnStock(Boolean tillOnStock) {
        this.tillOnStock = tillOnStock;
    }

    public Boolean getAllegroStandard() {
        return allegroStandard;
    }

    public void setAllegroStandard(Boolean allegroStandard) {
        this.allegroStandard = allegroStandard;
    }

    public int getBidsCount() {
        return bidsCount;
    }

    public void setBidsCount(int bidsCount) {
        this.bidsCount = bidsCount;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    public String getImageMedium() {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium) {
        this.imageMedium = imageMedium;
    }

    public String getImageLarge() {
        return imageLarge;
    }

    public void setImageLarge(String imageLarge) {
        this.imageLarge = imageLarge;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
