package com.nikhilece.ad_suraksha;

class dUser {
    private String email;
    private String name;
    private  String phone;
    private String address;
    private  String pin;
    private  String gendar;
    private String aadhar;
    public dUser(){

    }

    public dUser(String   name, String email, String phone, String address, String pin, String gender, String aadhar) {
        this.email=email;
        this.name=name;
        this.phone=phone;
        this.address=address;
        this.pin=pin;
        this.gendar=gender;
        this.aadhar=aadhar;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getGendar() {
        return gendar;
    }

    public void setGendar(String gendar) {
        this.gendar = gendar;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }
}
