package com.example.khuton2023.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.khuton2023.data.database.StudyMateDatabase
import com.example.khuton2023.databinding.FragmentDashboardBinding
import com.example.khuton2023.ui.home.ProfileRecyclerViewAdapter
import com.example.khuton2023.ui.home.RecyclerViewDecoration

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val recycler_chatroom = binding.chatRecycerView
        recycler_chatroom.layoutManager = LinearLayoutManager(requireContext())
        recycler_chatroom.adapter = RecyclerChatRoomsAdapter(requireContext())
//        val recyclerView = binding.chatRecycerView
//        val adapter = ChatRecyclerViewAdapter()
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        val db = Room.databaseBuilder(
//            requireContext(),
//            StudyMateDatabase::class.java, "StudyMate1"
//        ).allowMainThreadQueries().build()
//        val list = db.studyMateDao().getAll().map{Message(it,"용우야, 오늘 공부한 것 좀 보내줘",1)}
//        adapter.submitList(list)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}