package com.example.khuton2023.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.khuton2023.CreateStudyMateActivity
import com.example.khuton2023.MainActivity
import com.example.khuton2023.data.model.Mbti
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val recyclerView = binding.profileRecyclerView
        val adapter = ProfileRecyclerViewAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(RecyclerViewDecoration(60))
//        val db = Room.databaseBuilder(
//            requireContext(),
//            StudyMateDatabase::class.java, "StudyMate1"
//        ).allowMainThreadQueries().build()
        var list = mutableListOf<StudyMate>()
        val uuid = FirebaseAuth.getInstance().currentUser!!.uid

        val database = Firebase.database.reference
        adapter.submitList(listOf(StudyMate("카리나",2000,1,1,Mbti.ENFJ,"images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드")))
        val bitmap = Glide.with(requireContext()).load("images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드")

        database.child("studyMates")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val studyMate = snapshot.getValue<StudyMate>()
                }
            }
            )

        binding.button.setOnClickListener {
            val intent = Intent(requireContext(), CreateStudyMateActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}