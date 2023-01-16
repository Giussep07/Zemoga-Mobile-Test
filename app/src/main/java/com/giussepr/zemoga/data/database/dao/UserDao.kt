package com.giussepr.zemoga.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giussepr.zemoga.data.database.entity.UserEntity

@Dao
interface UserDao {

  @Query("SELECT * FROM user WHERE user_id = :userId")
  suspend fun getUserById(userId: Int): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(user: UserEntity)
}
