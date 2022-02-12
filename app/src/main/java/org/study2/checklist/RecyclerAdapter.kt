package org.study2.checklist


import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.study2.checklist.databinding.ItemRecyclerBinding


class RecyclerAdapter(val roomMemoList: List<RoomMemo>, var onDeleteListener: OnDeleteListener) :
    RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 1.사용할 데이터를 꺼내고
        val memo = roomMemoList.get(position)

        // 2.홀더에 데이터를 전달
        holder.setMemo(memo, roomMemoList)
        holder.itemView.setOnLongClickListener {

            onDeleteListener.onDeleteListener(memo)
            return@setOnLongClickListener true

        }

    }

    override
    fun getItemCount(): Int {
        return roomMemoList.size
    }

    // 3.홀더는 받은데이터를 화면에 출력한다
    class Holder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {


        fun setMemo(roomMemo: RoomMemo, roomMemoList: List<RoomMemo>) {


            with(binding) {


                no.text = "${roomMemoList.size}"  //total개수 만들때사용하면됨


//                no.text = "${roomMemo.no}"
                timeRe.text = roomMemo.time
                weight.text = roomMemo.select
                date.text = roomMemo.date


//                val sdf = SimpleDateFormat("hh:mm")
//                timeRe.text = sdf.format(roomMemo.time)

            }

        }
    }
}