package com.nikhilece.ad_suraksha;

class User {

    private String email;
    private String id;
    private String fullName;
    private String address;
    private String phone;

    public User() {
    }

    public User(String fname, String email, String addres, String phone,String id) {
        this.fullName = fname;
        this.email = email;
        this.address = addres;
        this.phone = phone;
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

}
