package com.example.parkify;

import java.io.Serializable;
import java.util.Objects;

public class Person implements Serializable {
    private String username, password, email, vehicle;

    private boolean isRootFlag = false;

    private Integer picId;

    public Person(){
        this.setUsername("");
        this.setPassword("");
        this.setEmail("");
        this.setVehicle("");
        this.setPicId(1);
    }

    public Person(String username, String password, String email, String vehicle){
        this.username = username;
        this.password = password;
        this.email = email;
        this.vehicle = vehicle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public boolean getRootFlag() {
        return isRootFlag;
    }

    public void setRootFlag(boolean rootFlag) {
        isRootFlag = rootFlag;
    }

    public Integer getPicId() {
        return picId;
    }

    public void setPicId(Integer picId) {
        this.picId = picId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return username.equals(person.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
