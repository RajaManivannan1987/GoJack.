package com.example.gojack.gojack.ModelClasses;

/**
 * Created by IM0033 on 8/11/2016.
 */
public class VehicleDetails {
    private static VehicleDetails vehicleDetails = new VehicleDetails();

    public static VehicleDetails getVehicleDetails() {
        return vehicleDetails;
    }

    private static String vehicle_make;
    private String vehicle_model;
    private String vehicle_number;
    private String bike_photo;
    private String balance_status;
    private String balance_message;

    public String getVehicle_make() {
        return vehicle_make;
    }

    public void setVehicle_make(String vehicle_make) {
        this.vehicle_make = vehicle_make;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getBike_photo() {
        return bike_photo;
    }

    public void setBike_photo(String bike_photo) {
        this.bike_photo = bike_photo;
    }

    public String getBalance_status() {
        return balance_status;
    }

    public void setBalance_status(String balance_status) {
        this.balance_status = balance_status;
    }

    public String getBalance_message() {
        return balance_message;
    }

    public void setBalance_message(String balance_message) {
        this.balance_message = balance_message;
    }


}
