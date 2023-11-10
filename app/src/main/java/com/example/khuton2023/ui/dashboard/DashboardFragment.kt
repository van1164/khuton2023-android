package com.example.khuton2023.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khuton2023.data.model.Mbti
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        val myUid = FirebaseAuth.getInstance().currentUser!!.uid
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val recycler_chatroom = binding.chatRecycerView
        val adapter = ChatRecyclerViewAdapter()
        recycler_chatroom.layoutManager = LinearLayoutManager(requireContext())
        recycler_chatroom.adapter = adapter

        val list = listOf(Message(
            StudyMate("카리나",2000,1,1,
                Mbti.ENFJ,"images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"),"test",3,true))
        adapter.submitList(list)


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