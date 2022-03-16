
package aar.websockets.model;

public class Device {

    private int id;
    private String currency1;
    private String currency2;
    private String date_max;
    private String date;
    private String price_max;
    private String price;
    
    public Device() {
    }
    
    public int getId() {
        return id;
    }
    
    public String getCurrency1() {
        return currency1;
    }

    public String getCurrency2() {
        return currency2;
    }
    
    public String getDate_Max() {
        return date_max;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getPrice_Max() {
        return price_max;
    }
    
    public String getPrice() {
        return price;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setCurrency1(String currency) {
        currency1 = currency;
    }

    public void setCurrency2(String currency) {
    	currency2 = currency;
    }
    
    public void setDate_Max(String date_max) {
    	this.date_max = date_max;
    }
    
    public void setDate(String date) {
    	this.date = date;
    }
    
    public void setPrice_Max(String price_max) {
    	this.price_max = price_max;
    }
    
    public void setPrice(String price) {
    	this.price = price;
    }
    
}
