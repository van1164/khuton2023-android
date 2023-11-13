package com.example.khuton2023.ui.dashboard

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.khuton2023.ChattingActivity
import com.example.khuton2023.R
import com.example.khuton2023.data.model.ChatRoomList
import com.example.khuton2023.data.model.Message
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.databinding.ChatContentItemBinding
import com.google.firebase.storage.FirebaseStorage


class ChatRecyclerViewAdapter :
    ListAdapter<ChatRoomList, ChatRecyclerViewAdapter.MyViewHolder>(diffUtil) {
    inner class MyViewHolder(private val binding: ChatContentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: ChatRoomList) {
            if (item.profileImage != null) {
                binding.selectProfileButton.apply {
                    this.setImageBitmap(
                        Bitmap.createScaledBitmap(
                            item.profileImage!!,
                            500,
                            700,
                            true
                        )
                    )

                    this.background =
                        getDrawable(binding.selectProfileButton.context, R.drawable.border_profile)
                    this.clipToOutline = true
                    this.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
            binding.chatName.text = item.name.toString()
            binding.chatContent.text = item.lastMessage
            binding.root.setOnClickListener {
                binding.root.context.startActivity(
                    Intent(binding.root.context, ChattingActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("studyMateId",
                            item.studyMateId
                        )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            ChatContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatRoomList>() {
            override fun areItemsTheSame(oldItem: ChatRoomList, newItem: ChatRoomList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatRoomList, newItem: ChatRoomList): Boolean {
                return oldItem == newItem
            }
        }
    }

}