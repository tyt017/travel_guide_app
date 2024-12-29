package com.example.finalproject.data

import android.content.ClipData
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Scene::class], version = 1, exportSchema = false)
//for this database, one table contain one entity
abstract class SceneRoomDatabase: RoomDatabase() {
    abstract fun sceneDao(): SceneDao

    companion object {
        @Volatile
        private var INSTANCE: SceneRoomDatabase? = null

        fun getDatabase(context: Context): SceneRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SceneRoomDatabase::class.java,
                    "scene_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}