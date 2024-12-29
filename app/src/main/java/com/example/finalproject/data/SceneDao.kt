package com.example.finalproject.data

import android.content.ClipData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// mapping functions to the SQL
@Dao
interface SceneDao {//Dao Data analyse
    @Query("SELECT * from scene ORDER BY name ASC")
    fun getItems(): Flow<List<Scene>>
    // flow 用串流的方式, 得到一個回傳一個->better way

    @Query("SELECT * from scene WHERE id = :id")
    fun getItem(id: Int): Flow<Scene>//依據給定的id來決定回傳的 item

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(scene: Scene)//在 ViewModel(InventoryViewModel)就呼叫這個
    // 加 suspend 代表是 background function(run在background), 且使用 co-routine
    @Update
    suspend fun update(scene: Scene)

    @Delete
    suspend fun delete(scene: Scene)

}