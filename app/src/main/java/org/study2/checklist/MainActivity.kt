package org.study2.checklist

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.View

import android.widget.AdapterView
import android.widget.ArrayAdapter


import androidx.recyclerview.widget.LinearLayoutManager

import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.study2.checklist.databinding.ActivityMainBinding


import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), OnDeleteListener {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val memoList = mutableListOf<RoomMemo>()


    lateinit var helper: RoomHelper
    lateinit var memoAdapter: RecyclerAdapter
    lateinit var memoDAO: RoomMemoDAO


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

                val content = select.text.toString()
                if (content.isNotEmpty()) {


                    val time = time.text.toString()

                    var date = datesearch.text.toString()

                    val memo = RoomMemo(time, content, date)

                    insertMemo(memo)


                }


            }
            search.setOnClickListener {

                serchrefreshAdapter()
                timeDateSet()

            }
        }





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
            var date = binding.datesearch.text.toString()
            memoList.clear()
            memoList.addAll(memoDAO.getDate(date))
            withContext(Dispatchers.Main) {
                memoAdapter.notifyDataSetChanged()

            }
        }

    }

    fun serchrefreshAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            var date = binding.datesearch.text.toString()
            Log.d("테스트","${date}")
            memoList.clear()
            memoList.addAll(memoDAO.getDate(date))

            withContext(Dispatchers.Main) {
                memoAdapter.notifyDataSetChanged()

            }
        }
    }


// 스피너코드
    fun spinerCode() {

        val data: List<String> = listOf( "선택하세요","많음", "약간많음", "중간", "조금적음", "적음")

        val adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, data)

        with(binding) {

            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val selected = data[0]
                    binding.select.text = selected
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = data[position]
                    binding.select.text = selected

                }
            }
        }
    }
    //스피너코드

    // 시간 날짜 입력코드
    fun timeDateSet() {
        val now = System.currentTimeMillis()
        val date = Date(now)

        val sdf = SimpleDateFormat("YYYY-MM-dd")
        val timestamp: String = sdf.format(date)
        binding.datesearch.setText(timestamp)

        val stf = SimpleDateFormat("HH:mm")
        val getTimes = stf.format(date)
        binding.time.setText(getTimes)


    }

    override fun onDeleteListener(memo: RoomMemo) {
        deleteMemo(memo)
    }
    // 시간 날짜 입력코드


}
