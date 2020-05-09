package org.cap.customermgt.dto;

public class AddCustomerRequestData {
    private String name;

    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String role;

    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role=role;
    }
}
