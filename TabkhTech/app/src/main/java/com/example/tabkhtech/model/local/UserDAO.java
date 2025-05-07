//package com.example.tabkhtech.model.local;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import com.example.tabkhtech.model.pojos.User;
//
//@Dao
//public interface UserDAO {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertUser(User user);
//
//    @Update
//    void updateUser(User user);
//
//    @Query("SELECT * FROM users WHERE id = :userId")
//    LiveData<User> getUserById(String userId);
//
//    @Query("DELETE FROM users WHERE id = :userId")
//    void deleteUser(String userId);
//}