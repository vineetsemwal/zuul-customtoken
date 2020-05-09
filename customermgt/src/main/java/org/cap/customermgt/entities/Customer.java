package org.cap.customermgt.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.Set;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String role;

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role=role;
    }

    private String password;

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

}
