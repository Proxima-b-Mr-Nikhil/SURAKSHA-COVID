package com.nikhilece.ad_suraksha;

class sUser {

    private String name;
    private String aadhar;
    private String phone;
    private String email;
    private String address;
    private String bankacc;
    private String ifsc;
    private String lat;
    private String lng;
    private String description;
    private String uid;
    private String time;
    private String status;
    private String a;



    public sUser(String fname, String faadhar, String fphone, String femail, String fpaddress, String fbankacc, String fifsc, String flat, String flng, String fdescription, String fuid, String ftime, String fstatus, String fa) {

        this.name = fname;
        this.aadhar = faadhar;
        this.phone = fphone;
        this.email = femail;
        this.address = fpaddress;
        this.bankacc = fbankacc;
        this.ifsc = fifsc;
        this.lat = flat;
        this.lng = flng;
        this.description = fdescription;
        this.uid = fuid;
        this.time = ftime;
        this.status = fstatus;
        this.a=fa;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getBankacc() {
        return bankacc;
    }

    public void setBankacc(String bankacc) {
        this.bankacc = bankacc;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
