package com.example.gojack.gojack.ModelClasses;

/**
 * Created by Im033 on 3/21/2017.
 */

public class AccountsModel {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getTotalrides() {
        return totalrides;
    }

    public void setTotalrides(String totalrides) {
        this.totalrides = totalrides;
    }


    private String date;
    private String commission;
    private String amt;
    private String totalrides;


}
