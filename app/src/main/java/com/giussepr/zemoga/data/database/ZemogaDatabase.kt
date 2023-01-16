package com.giussepr.zemoga.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.giussepr.zemoga.data.database.dao.CommentDao
import com.giussepr.zemoga.data.database.dao.PostDao
import com.giussepr.zemoga.data.database.dao.UserDao
import com.giussepr.zemoga.data.database.entity.CommentEntity
import com.giussepr.zemoga.data.database.entity.PostEntity
import com.giussepr.zemoga.data.database.entity.UserEntity

@Database(
  entities = [PostEntity::class, UserEntity::class, CommentEntity::class],
  version = 1,
  exportSchema = true
)
abstract class ZemogaDatabase : RoomDatabase() {

  abstract fun postDao(): PostDao

  abstract fun userDao(): UserDao

  abstract fun commentDao(): CommentDao
}
