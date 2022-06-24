package com.spe.iiitbcms.JWT;

public class JwtRequest {
    String password;
    String email;

    public JwtRequest() {

    }

    public JwtRequest(String password,String email) {
        this.password = password;
        this.email = email;
    }

//    public JwtRequest(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    @Override
//    public String toString() {
//        return "JwtRequest{" +
//                "username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                '}';
//    }
}

