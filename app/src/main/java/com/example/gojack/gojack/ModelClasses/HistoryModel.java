package com.example.gojack.gojack.ModelClasses;

/**
 * Created by IM0033 on 9/2/2016.
 */
public class HistoryModel {
    public String getRide_id() {
        return ride_id;
    }

    public void setRide_id(String ride_id) {
        this.ride_id = ride_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }

    public String getRide_type() {
        return ride_type;
    }

    public void setRide_type(String ride_type) {
        this.ride_type = ride_type;
    }

    private String ride_id;
    private String date_time;

    public String getDriver_s_address() {
        return driver_s_address;
    }

    public void setDriver_s_address(String driver_s_address) {
        this.driver_s_address = driver_s_address;
    }

    public String getDriver_e_address() {
        return driver_e_address;
    }

    public void setDriver_e_address(String driver_e_address) {
        this.driver_e_address = driver_e_address;
    }

    private String driver_s_address;
    private String driver_e_address;
    private String final_amount;
    private String ride_type;
}
