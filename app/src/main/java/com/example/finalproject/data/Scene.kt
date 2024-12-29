package com.example.finalproject.data

import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Scene(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val locationName: String,
    val locationAddress: String,
    val imageUrl: String,
    val locationDescription: String,
    val sendLocation: String
)
