package com.mbtaapp.model;

public class EmailRequest {
    private String name;
    private String emailAddress;
    private String subject;
    private String message;

    public EmailRequest(String name, String emailAddress, String subject, String message){
        this.name = name;
        this.emailAddress = emailAddress;
        this.subject = subject;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
