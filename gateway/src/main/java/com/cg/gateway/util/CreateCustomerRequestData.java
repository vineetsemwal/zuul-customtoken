package com.cg.gateway.util;

import javax.validation.constraints.Size;

public class CreateCustomerRequestData {
    @Size(min = 1)
    private String name;

    @Size(min = 1)
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

    @Size(min = 1)
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
