package org.study2.checklist

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface RoomMemoDAO {

    @Query("select * from room_memo")
    fun getAll() : List<RoomMemo>

    @Insert(onConflict = REPLACE)
    fun insert(memo:RoomMemo)

    @Delete
    fun delete(memo: RoomMemo)

    @Query("SELECT * FROM room_memo WHERE date = :date")
    fun getDate(date : String) : List<RoomMemo>


}