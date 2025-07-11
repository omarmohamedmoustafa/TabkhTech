//package com.example.tabkhtech.model.pojos;
//
//import androidx.annotation.NonNull;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "users")
//public class User {
//    @PrimaryKey
//    @NonNull
//    private String id;
//    private String name;
//    private String email;
//    private String password;
//
//    public User() {
//    }
//
//    public User(@NonNull String id, String name, String email, String password) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }
//
//    @NonNull
//    public String getId() {
//        return id;
//    }
//
//    public void setId(@NonNull String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//}