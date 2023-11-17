package com.example.khuton2023.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.khuton2023.data.database.ChattingRoomListDatabase
import com.example.khuton2023.databinding.FragmentDashboardBinding
import com.example.khuton2023.ui.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel :DashboardViewModel
    private lateinit var adapter : ChatRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        adapter = initRecyclerViewAdapter()
        initListener()
        getChatRoomList()
        return binding.root
    }

    private fun initRecyclerViewAdapter(): ChatRecyclerViewAdapter {
        val recyclerChatRoom = binding.chatRecycerView
        val adapter = ChatRecyclerViewAdapter()
        recyclerChatRoom.layoutManager = LinearLayoutManager(requireContext())
        recyclerChatRoom.adapter = adapter
        return adapter
    }

    private fun getChatRoomList() {
        val db = Room.databaseBuilder(
            requireContext(),
            ChattingRoomListDatabase::class.java,
            "ChatRoomList"
        ).allowMainThreadQueries().build()
        viewModel.getChatRoomList(db)
    }

    private fun initListener() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatRoomList.collectLatest {chatRoomList->
                    adapter.submitList(chatRoomList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}