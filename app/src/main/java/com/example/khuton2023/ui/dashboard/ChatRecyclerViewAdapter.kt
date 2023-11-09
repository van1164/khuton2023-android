package com.example.khuton2023.ui.dashboard

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.khuton2023.R
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.databinding.ChatContentItemBinding

data class Message(
    val studyMate: StudyMate,
    val message: String,
    val count: Int,
)

class ChatRecyclerViewAdapter :
    ListAdapter<Message, ChatRecyclerViewAdapter.MyViewHolder>(diffUtil) {
    class MyViewHolder(private val binding: ChatContentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            if (item.studyMate.profileImage != null) {
                binding.selectProfileButton.apply {
                    this.setImageBitmap(
                        Bitmap.createScaledBitmap(
                            item.studyMate.profileImage,
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
            binding.chatName.text = item.studyMate.name.toString()
            binding.chatContent.text = item.message.toString()

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