package com.example.gojack.gojack.ModelClasses;

/**
 * Created by IM0033 on 8/17/2016.
 */
public class CancelReason {
    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String reasonId;
    private String reason;
}
