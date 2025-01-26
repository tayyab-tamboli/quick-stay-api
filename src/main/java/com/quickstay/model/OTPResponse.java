package com.quickstay.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OTPResponse {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Details")
    private String details;

    public OTPResponse(String status, String details) {
        this.status = status;
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
