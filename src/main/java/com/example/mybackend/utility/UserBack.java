package com.example.mybackend.utility;

import javax.persistence.Column;

public class UserBack {
    public Integer id;
    public String username;
    public String userpassword;
    public String usergender;
    public String usermail;
    public String useraddress;
    public String userphone;
    public UserBack(Integer id, String name, String password, String gender,
                    String mail, String address, String phone) {
        this.id = id;
        this.username = name;
        this.userpassword = password;
        this.usergender = gender;
        this.usermail = mail;
        this.useraddress = address;
        this.userphone = phone;
    }
}
