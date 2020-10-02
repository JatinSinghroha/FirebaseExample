package com.technologyend.firebaseexample;

/**
 * @author jatin
 * @date 028, 28 Sep, 2020
 */

public class FriendlyUser {

    private String username;
    private String email;
    private String phonenumber;
    private String signupDate;
    private String lastSignIN;
    public FriendlyUser() {
    }

    public FriendlyUser(String username, String email, String phonenumber, String signupDate, String lastSignIN) {
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.signupDate = signupDate;
        this.lastSignIN = lastSignIN;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(String signupDate) {
        this.signupDate = signupDate;
    }

    public String getLastSignIN() {
        return lastSignIN;
    }

    public void setLastSignIN(String lastSignIN) {
        this.lastSignIN = lastSignIN;
    }
}
