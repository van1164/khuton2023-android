package com.example.khuton2023.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.khuton2023.data.database.ChattingRoomListDatabase
import com.example.khuton2023.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val myUid = FirebaseAuth.getInstance().currentUser!!.uid
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val recyclerChatRoom = binding.chatRecycerView
        val adapter = ChatRecyclerViewAdapter()
        recyclerChatRoom.layoutManager = LinearLayoutManager(requireContext())
        recyclerChatRoom.adapter = adapter
        val db = Room.databaseBuilder(
            requireContext(),
            ChattingRoomListDatabase::class.java,
            "ChatRoomList"
        ).allowMainThreadQueries().build()
        val list = db.chatRoomListDao().getAll()

        adapter.submitList(list)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}