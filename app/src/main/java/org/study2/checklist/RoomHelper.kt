package org.study2.checklist

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(RoomMemo::class), version = 1,exportSchema = false)
abstract class RoomHelper : RoomDatabase() {

    abstract fun roomMemoDao(): RoomMemoDAO
}