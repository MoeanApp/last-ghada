package com.example.android.wearable.datalayer.Model;

public class User {
    private String firstName, Lastname, role,Email,Password,uid;

    public User(){

    }
    public User(String firstName, String Lastname, String role, String Email, String Password, String uid) {
        this.firstName = firstName;
        this.Lastname = Lastname;
        this.role = role;
        this.Email=Email;
        this.Password=Password;
        this.uid=uid;
    }

    public String getRole() {
        return role;
    }
    public String getuid() {
        return uid;
    }



    public String getfirstName() {
        return firstName;
    }

    public String getLastname() {
        return Lastname;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }


}

