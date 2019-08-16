package com.jtalics.bootes;

public class User {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String comment;
    private String recaptchaResponse;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getLastName() {
        return this.lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public void setRecaptchaResponse(String recaptchaResponse) {
        this.recaptchaResponse = recaptchaResponse;
    }
    
    public String getRecaptchaResponse() {
        return this.recaptchaResponse;
    }
    
    public String toString() {
        return "{firstName="+getFirstName()
            +";lastName="+getLastName()
            +";emailAddress="+getEmailAddress()
            +";comment="+getComment()
            +";recaptchaResponse="+getRecaptchaResponse()+"}";
    }
}