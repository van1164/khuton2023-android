package com.example.khuton2023.ui.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.khuton2023.ChattingActivity
import com.example.khuton2023.R
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.databinding.ChatContentItemBinding
import com.google.firebase.storage.FirebaseStorage

data class Message(
    val studyMate: StudyMate,
    val message: String,
    val count: Int,
    val oppo:Boolean,
)

class ChatRecyclerViewAdapter :
    ListAdapter<Message, ChatRecyclerViewAdapter.MyViewHolder>(diffUtil) {
    class MyViewHolder(private val binding: ChatContentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            if (item.studyMate.profileImageUri != null) {
                binding.selectProfileButton.apply {
                    FirebaseStorage.getInstance().reference.child(item.studyMate.profileImageUri).downloadUrl.addOnSuccessListener {
                        Glide.with(this.context).load(it.toString()).into(this)
                    }

                    this.background =
                        getDrawable(binding.selectProfileButton.context, R.drawable.border_profile)
                    this.clipToOutline = true
                    this.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
            binding.chatName.text = item.studyMate.name.toString()
            binding.chatContent.text = item.message.toString()
            binding.root.setOnClickListener {
                binding.root.context.startActivity(
                    Intent(binding.root.context, ChattingActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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