package com.reserach.movieapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reserach.movieapp.data.local.dao.UserDAO
import com.reserach.movieapp.data.local.model.UserTableModel

@Database(
    entities = [UserTableModel::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun userDao() : UserDAO

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabaseClient(context: Context) : MovieDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context,MovieDatabase::class.java, "MOVIE_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }

        }

    }

}