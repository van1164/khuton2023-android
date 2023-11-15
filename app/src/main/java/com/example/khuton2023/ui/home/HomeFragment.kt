package com.example.khuton2023.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.khuton2023.ui.create_study_mate.CreateStudyMateActivity
import com.example.khuton2023.data.database.StudyMateData
import com.example.khuton2023.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val adapter = ProfileRecyclerViewAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val recyclerView = binding.profileRecyclerView


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(RecyclerViewDecoration(60))

        val db = Room.databaseBuilder(
            requireContext(),
            StudyMateData::class.java, "studymate"
        ).allowMainThreadQueries().build()
        val item = db.studyMateDao().getAll()
        adapter.submitList(item)
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(item.size-1)

        binding.button.setOnClickListener {
            val intent = Intent(requireContext(), CreateStudyMateActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val db = Room.databaseBuilder(
            requireContext(),
            StudyMateData::class.java, "studymate"
        ).allowMainThreadQueries().build()
        val item = db.studyMateDao().getAll()
        adapter.submitList(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}