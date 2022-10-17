package com.nikhilece.ad_suraksha;

class tUser  {
    private String name;
    private String aadhar;
    private String phone;
    private String email;
    private String address;
    private String profession;
    private String company;
    private String lat;
    private String lng;
    private String destination;

    public tUser(String fname, String faadhar, String fphone, String femail, String fpaddress, String fprofession, String fcompany, String flat, String flng, String fdestination) {
        this.name = fname;
        this.aadhar = faadhar;
        this.phone = fphone;
        this.email = femail;
        this.address = fpaddress;
        this.profession = fprofession;
        this.company = fcompany;
        this.lat = flat;
        this.lng = flng;
        this.destination = fdestination;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
