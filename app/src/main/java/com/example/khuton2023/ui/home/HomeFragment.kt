package com.example.khuton2023.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.khuton2023.R
import com.example.khuton2023.data.database.StudyMateData
import com.example.khuton2023.databinding.FragmentHomeBinding
import com.example.khuton2023.ui.create_study_mate.CreateStudyMateActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter = ProfileRecyclerViewAdapter()
    private lateinit var viewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        recyclerView = initRecyclerView()
        initViewModelListener()
        getStudyMateList()
        return binding.root
    }

    fun clickPlusButton() {
        val intent = Intent(requireContext(), CreateStudyMateActivity::class.java)
        startActivity(intent)
    }

    private fun getStudyMateList() {
        val db = Room.databaseBuilder(
            requireContext(),
            StudyMateData::class.java, "studymate"
        ).allowMainThreadQueries().build()
        viewModel.getStudyMateList(db)
    }

    private fun initViewModelListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.studyMateList.collectLatest { studyMateList ->
                    adapter.submitList(studyMateList)
                    adapter.notifyDataSetChanged()
                    studyMateList?.run { recyclerView.scrollToPosition(studyMateList.size - 1) }
                }
            }
        }
    }

    private fun initRecyclerView(): RecyclerView {
        val recyclerView = binding.profileRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(RecyclerViewDecoration(60))
        return recyclerView
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