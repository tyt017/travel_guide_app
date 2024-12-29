package com.example.finalproject

import android.app.Application
import com.example.finalproject.data.SceneRoomDatabase

// for getting a single database instance after launching the application

// Andriod 的架構
//
class SceneListApplication: Application() {
    val database: SceneRoomDatabase by lazy { // initialize database
        SceneRoomDatabase.getDatabase(this)//可以得到資料庫的單一instance
    }
}