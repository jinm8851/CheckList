package org.study2.checklist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "room_memo")
 class RoomMemo {

   @PrimaryKey(autoGenerate = true) // no 에 값이 없을때 자동증가된 숫자값을 db에 입력해준다
    @ColumnInfo
    var no: Int = 0
    @ColumnInfo
    var time: String ="시간"
    @ColumnInfo
    var select: String = "양"
    @ColumnInfo(name = "date")
    var date: String = "날짜"

   constructor(time:String, select:String, date: String) {


      this.select = select
      this.time = time
       this.date= date
   }

}
