package com.giussepr.zemoga.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.giussepr.zemoga.data.database.dao.PostDao

@Database(entities = [], version = 1, exportSchema = true)
abstract class ZemogaDatabase : RoomDatabase() {

  abstract fun postDao(): PostDao
}
