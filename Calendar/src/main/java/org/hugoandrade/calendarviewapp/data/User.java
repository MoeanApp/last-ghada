package org.hugoandrade.calendarviewapp.data;

public class User {
    public String Fname, Lastname, role,Email,Password,uid;

    public User(){

    }
    public User(String Fname, String Lastname, String role, String Email, String Password, String uid) {
        this.Fname = Fname;
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
}
