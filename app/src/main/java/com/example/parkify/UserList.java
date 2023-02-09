package com.example.parkify;

import java.util.ArrayList;
import java.util.HashMap;

public class UserList{

    private static final Person administrator = new Person("admin", "admin", "", "");
    public static ArrayList<Person> dbUser = new ArrayList<Person>();
    public static ArrayList<Person> arrayResult = new ArrayList<Person>();
    public static boolean initialized = false;


    public static ArrayList<String> getUsernameArrayList(){
        ArrayList<String> usernameArrayList = new ArrayList<>();

        for(int i=0; i< dbUser.size(); i++){
            usernameArrayList.add(dbUser.get(i).getUsername());
        }

        return usernameArrayList;
    }

    public static void initializeDB(){
        administrator.setAdmin(true);
        administrator.setRootFlag(true);
        administrator.setUserId(UserList.dbUser.size());
        administrator.setPicId(1);
        dbUser.add(administrator);
        arrayResult.add(administrator);
        initialized = true;
    }


}
