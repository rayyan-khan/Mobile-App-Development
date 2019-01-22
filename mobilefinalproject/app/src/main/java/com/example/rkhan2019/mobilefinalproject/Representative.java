package com.example.rkhan2019.mobilefinalproject;

public class Representative {

    public String name, status, contact;

    public Representative(){
    }

    public Representative(String n, String s, String c){
        name = n;
        status = s;
        contact = c;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
