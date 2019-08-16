package com.jtalics.bootes;

import com.fasterxml.jackson.annotation.*;

public class RecaptchaResponse {

    private String success;
    private String challenge_ts;
    private String hostname;
    private double score;
    private String action;
    @JsonProperty("error-codes")
    private String[] errorCodes;

    public void setSuccess(String success) {
        this.success = success;
    }    
    
    public String getSuccess() {
        return this.success;
    }
    public void setChallenge_ts(String challenge_ts) {
        this.challenge_ts =challenge_ts ;
    }    
    
    public String getChallenge_ts() {
        return this.challenge_ts;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }    
    
    public String getHostname() {
        return this.hostname;
    }

    public void setaction(String action) {
        this.action = action;
    }    
    
    public String getAction() {
        return this.action;
    }
    
    public void setScore(double score) {
        this.score = score;
    }    
    
    public double getScore() {
        return this.score;
    }

    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }    
    
    public String[] getErrorCodes() {
        return this.errorCodes;
    }
}