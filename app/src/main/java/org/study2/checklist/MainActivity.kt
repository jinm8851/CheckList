package org.study2.checklist

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.study2.checklist.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), OnDeleteListener {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val memoList = mutableListOf<RoomMemo>()


    lateinit var helper: RoomHelper
    lateinit var memoAdapter: RecyclerAdapter
    lateinit var memoDAO: RoomMemoDAO
    lateinit var content: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        timeDateSet()
        spinerCode()


        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_memo")
            .fallbackToDestructiveMigration() //스키마(Database)버젼 변경 가능
//            .allowMainThreadQueries() //메인쓰레드에서 쿼리 공부할때만쓴다.
            .build()

        memoDAO = helper.roomMemoDao()


        memoAdapter = RecyclerAdapter(memoList, this)



        refreshAdapter()


        with(binding) {
            recyclerMemo.adapter = memoAdapter
            recyclerMemo.layoutManager = LinearLayoutManager(this@MainActivity)



            save.setOnClickListener {


                if (content.isNotEmpty()) {


                    val time = time.text.toString()

                    val date = datesearch.text.toString()

                    val memo = RoomMemo(time, content, date)

                    insertMemo(memo)


                }

            }
            search.setOnClickListener {

                serchrefreshAdapter()


            }
        }


    }


    override fun onResume() {
        timeDateSet()
        super.onResume()
    }

    fun insertMemo(memo: RoomMemo) {
        CoroutineScope(Dispatchers.IO).launch {
            memoDAO.insert(memo)

            refreshAdapter()

        }
    }

    fun deleteMemo(memo: RoomMemo) {
        CoroutineScope(Dispatchers.IO).launch {
            memoDAO.delete(memo)
            refreshAdapter()
        }
    }

    fun refreshAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            val date = binding.datesearch.text.toString()
            memoList.clear()
            memoList.addAll(memoDAO.getDate(date))
            withContext(Dispatchers.Main) {
                memoAdapter.notifyDataSetChanged()

            }
        }

    }

    fun serchrefreshAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            val date = binding.datesearch.text.toString()

            memoList.clear()
            memoList.addAll(memoDAO.getDate(date))

            withContext(Dispatchers.Main) {
                memoAdapter.notifyDataSetChanged()

            }
        }
    }


    // 스피너코드
    fun spinerCode() {

        val data: List<String> = listOf("선택하세요", "많음", "약간많음", "중간", "조금적음", "적음")

        var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(binding) {

            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    content = parent?.getItemAtPosition(position).toString()
                    //스피너 처음 표시되는 컬러 색상 변경
                    (parent?.getChildAt(0) as? TextView)?.setTextColor(Color.parseColor("#009688"))
                }
            }
        }
    }
    //스피너코드

    // 시간 날짜 입력코드

    private fun timeDateSet() {
//        val now = System.currentTimeMillis()
//        val date = Date(now)
        val currentTime = LocalDateTime.now()

        val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timestamp: String = currentTime.format(sdf)
        binding.datesearch.setText(timestamp)

        val stf = DateTimeFormatter.ofPattern("HH:mm")

        val getTimes = currentTime.format(stf)
        binding.time.setText(getTimes)


    }

    override fun onDeleteListener(memo: RoomMemo) {
        deleteMemo(memo)
    }
    // 시간 날짜 입력코드


}
