package com.example.khuton2023


import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.khuton2023.data.dao.StudyMateDao
import com.example.khuton2023.data.database.StudyMateData
import com.example.khuton2023.data.model.Message
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.databinding.MessageMineBinding
import com.example.khuton2023.databinding.MessageOppoBinding
import com.example.khuton2023.ui.dashboard.ChatRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import okhttp3.internal.notify


abstract class MyViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(position: Int)
}

class RecyclerMessagesAdapter(
    val userName : String
) : ListAdapter<Message, MyViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].oppo) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return when (viewType) {
            1 -> {            //메시지가 내 메시지인 경우
                val view = MessageMineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyMessageViewHolder(view)
//                FoodViewHolder(view)
//                val view =
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.message_mine, parent, false)   //내 메시지 레이아웃으로 초기화
//
//                MyMessageViewHolder(MessageMineBinding.bind(view))
            }
            else -> {      //메시지가 상대 메시지인 경우
                val view = MessageOppoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OtherMessageViewHolder(view)
//                val view =
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.message_oppo, parent, false)  //상대 메시지 레이아웃으로 초기화
//                OtherMessageViewHolder(MessageOppoBinding.bind(view))
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!currentList[position].oppo) {       //레이아웃 항목 초기화
            (holder as MyMessageViewHolder).bind(position)
        } else {
            (holder as OtherMessageViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class OtherMessageViewHolder(itemView: MessageOppoBinding) :         //상대 메시지 뷰홀더
        MyViewHolder(itemView) {
        var text = itemView.oppoText
        var name = itemView.oppoNameText
        var image = itemView.selectProfileButton
        var time = itemView.time
        val db = Room.databaseBuilder(
            text.context,
            StudyMateData::class.java, "studymate"
        ).build()

        override fun bind(position: Int) {           //메시지 UI 항목 초기화
            if (currentList[position].profileImage != null) {
                Log.d("PPPPPPPPPPPPPPPPPPPPPPPP",currentList[position].profileImage.toString())
                image.apply {
                    this.setImageBitmap(
                        Bitmap.createScaledBitmap(
                            currentList[position].profileImage!!,
                            500,
                            700,
                            true
                        )
                    )

                    this.background =
                        AppCompatResources.getDrawable(
                            this.context,
                            R.drawable.border_small
                        )
                    this.clipToOutline = true
                    this.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }



            var message = currentList[position]
            text.text = message.message
            name.text = userName
            time.text = "1분전"
//            setShown(position)             //해당 메시지 확인하여 서버로 전송
        }

        fun getDateText(sendDate: String): String {    //메시지 전송 시각 생성

            var dateText = ""
            var timeString = ""
            if (sendDate.isNotBlank()) {
                timeString = sendDate.substring(8, 12)
                var hour = timeString.substring(0, 2)
                var minute = timeString.substring(2, 4)

                var timeformat = "%02d:%02d"

                if (hour.toInt() > 11) {
                    dateText += "오후 "
                    dateText += timeformat.format(hour.toInt() - 12, minute.toInt())
                } else {
                    dateText += "오전 "
                    dateText += timeformat.format(hour.toInt(), minute.toInt())
                }
            }
            return dateText
        }


    }

    inner class MyMessageViewHolder(itemView: MessageMineBinding) :       // 내 메시지용 ViewHolder
        MyViewHolder(itemView) {
        var text = itemView.mineText
        var time = itemView.timeText

        override fun bind(position: Int) {            //메시지 UI 레이아웃 초기화
            var message = currentList[position]
            text.text = message.message
            time.text = "1분전"
        }

        fun getDateText(sendDate: String): String {        //메시지 전송 시각 생성
            var dateText = ""
            var timeString = ""
            if (sendDate.isNotBlank()) {
                timeString = sendDate.substring(8, 12)
                var hour = timeString.substring(0, 2)
                var minute = timeString.substring(2, 4)

                var timeformat = "%02d:%02d"

                if (hour.toInt() > 11) {
                    dateText += "오후 "
                    dateText += timeformat.format(hour.toInt() - 12, minute.toInt())
                } else {
                    dateText += "오전 "
                    dateText += timeformat.format(hour.toInt(), minute.toInt())
                }
            }
            return dateText
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }

}