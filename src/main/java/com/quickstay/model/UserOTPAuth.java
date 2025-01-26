package com.quickstay.model;


public class UserOTPAuth {

    private String username;
    private String otpSessionId;
    private String otpValue;

    public UserOTPAuth(String username, String otpSessionId, String otpValue) {
        this.username = username;
        this.otpSessionId = otpSessionId;
        this.otpValue = otpValue;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtpSessionId() {
        return otpSessionId;
    }

    public void setOtpSessionId(String otpSessionId) {
        this.otpSessionId = otpSessionId;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }
}
