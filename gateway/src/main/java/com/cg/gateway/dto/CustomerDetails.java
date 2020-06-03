package com.cg.gateway.dto;

public class CustomerDetails {

    private int id;

    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    private String role;

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role=role;
    }

}
