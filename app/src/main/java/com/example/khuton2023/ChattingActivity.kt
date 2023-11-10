package com.example.khuton2023

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khuton2023.data.model.Mbti
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.databinding.ActivityChattingBinding
import com.example.khuton2023.ui.dashboard.Message
import okhttp3.internal.notify

class ChattingActivity : AppCompatActivity() {

    lateinit var binding: ActivityChattingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val adapter = ChatRecyclerViewAdapter()
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = adapter

//        val list = listOf(Message(
//            StudyMate("카리나",2000,1,1,
//                Mbti.ENFJ,"images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"),"test",true))
//        adapter.submitList(list)
        val adapter = RecyclerMessagesAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this).apply {
            this.stackFromEnd = true	// 가장 최근의 대화를 표시하기 위해 맨 아래로 정렬.
        }
            val messagList = mutableListOf(
                Message(
                    StudyMate(
                        "카리나", 2000, 1, 1,
                        Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                    ), "용우야, \n 밥먹었어?", true
                ),
                Message(
                    StudyMate(
                        "카리나", 2000, 1, 1,
                        Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                    ), "test", true
                ),
                Message(
                    StudyMate(
                        "카리나", 2000, 1, 1,
                        Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                    ), "test", true
                ),
                Message(
                    StudyMate(
                        "카리나", 2000, 1, 1,
                        Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                    ), "test", true
                ),
                Message(
                    StudyMate(
                        "카리나", 2000, 1, 1,
                        Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                    ), "test", true
                ),
                Message(
                    StudyMate(
                        "카리나", 2000, 1, 1,
                        Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                    ), "싫어", false
                )
            )
            adapter.submitList(messagList)

            binding.sendButton.setOnClickListener {
                messagList.add(
                    Message(
                        StudyMate(
                            "카리나", 2000, 1, 1,
                            Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                        ), binding.editTextText.text.toString(), false
                    )
                )
                binding.editTextText.text = Editable.Factory.getInstance().newEditable("")
                Log.d("XXXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXXXXXX")
                adapter.submitList(messagList)
                adapter.notifyDataSetChanged()
                binding.recyclerView.scrollToPosition(messagList.size-1)
            }
        }
    }