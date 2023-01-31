package com.example.progetto;

import java.io.Serializable;


public class Person implements Serializable {
    private String username, password, email, vehicle;
    private boolean adminFlag = false;
    private boolean isRootFlag = false;
    private Integer userId;
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
        this.adminFlag=false;
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

    public boolean getAdmin() {
        return adminFlag;
    }

    public void setAdmin(boolean adminFlag) {
        this.adminFlag = adminFlag;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public Integer getUserId(){
        return userId;
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
    public boolean equals(Object obj){
        if(obj instanceof Person){
            Person p = (Person) obj;

            return p.getUsername().equals(this.getUsername()) && p.getPassword().equals(this.getPassword());
        }

        return false;
    }

    @Override
    public int hashCode(){
        return this.username.hashCode() + this.password.hashCode();
    }


}
