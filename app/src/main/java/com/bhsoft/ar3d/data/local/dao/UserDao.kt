package com.bhsoft.ar3d.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bhsoft.ar3d.data.model.User
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

@Dao
interface UserDao {
    @Query(value = "SELECT * FROM User WHERE User.id=:userId")
    fun findUserById(userId: Int) : User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(item: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListUser(item: MutableList<User>)

    @Query(value = "SELECT * FROM User")
    fun findAll(): MutableList<User>
}