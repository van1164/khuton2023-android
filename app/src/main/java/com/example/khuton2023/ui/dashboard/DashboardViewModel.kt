package com.example.khuton2023.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khuton2023.data.database.ChattingRoomListDatabase
import com.example.khuton2023.data.model.ChatRoomList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _chatRoomList = MutableStateFlow<List<ChatRoomList>?>(null)
    val chatRoomList: StateFlow<List<ChatRoomList>?> = _chatRoomList.asStateFlow()


    fun getChatRoomList(db:ChattingRoomListDatabase) {
        viewModelScope.launch {
            val list = db.chatRoomListDao().getAll()
            _chatRoomList.update { list }
        }
    }
}