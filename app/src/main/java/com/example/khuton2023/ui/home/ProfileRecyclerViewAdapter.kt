package com.example.khuton2023.ui.home

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
import com.example.khuton2023.databinding.ProfileItemBinding


class ProfileRecyclerViewAdapter : ListAdapter<StudyMate, ProfileRecyclerViewAdapter.MyViewHolder>(diffUtil) {
    class MyViewHolder(private val binding: ProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StudyMate) {
            if(item.profileImage != null){
                binding.selectProfileButton.apply {
                    this.setImageBitmap(
                        Bitmap.createScaledBitmap(
                            item.profileImage,
                            500,
                            700,
                            true
                        )
                    )
                    this.background = getDrawable(binding.selectProfileButton.context,R.drawable.border_profile)
                    this.clipToOutline = true
                    this.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }

            binding.profileName.text = item.name.toString()
            binding.profileYear.text = "${2023 - item.year}세"
            binding.profileMbti.text = item.mbti.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<StudyMate>() {
            override fun areItemsTheSame(oldItem: StudyMate, newItem: StudyMate): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StudyMate, newItem: StudyMate): Boolean {
                return oldItem == newItem
            }
        }
    }

}