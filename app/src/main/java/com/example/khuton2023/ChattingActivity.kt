package com.example.khuton2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khuton2023.data.model.ChatRoom
import com.example.khuton2023.databinding.ActivityChattingBinding
import com.example.khuton2023.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.auth.User

class ChattingActivity : AppCompatActivity() {

    lateinit var binding: ActivityChattingBinding
    lateinit var btn_exit: ImageButton
    lateinit var btn_submit: Button
    lateinit var txt_title: TextView
    lateinit var edt_message: EditText
    lateinit var firebaseDatabase: DatabaseReference
    lateinit var recycler_talks: RecyclerView
    lateinit var chatRoom: ChatRoom
    lateinit var opponentUser: User
    lateinit var chatRoomKey: String
    lateinit var myUid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = RecyclerMessagesAdapter(this)
        binding.recyclerView.adapter
    }
}