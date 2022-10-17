package com.nikhilece.ad_suraksha;

class Userprofil {

    private String email;
    private String name;
    private String address;
    private String phone;
    private String aadhar;
    private String imageurl;

    public Userprofil() {
    }

    public Userprofil(String fname, String email, String addres, String phone,String aadhar,String imageurl) {
        this.name = fname;
        this.email = email;
        this.address = addres;
        this.phone = phone;
        this.aadhar=aadhar;
        this.imageurl=imageurl;

    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }
}