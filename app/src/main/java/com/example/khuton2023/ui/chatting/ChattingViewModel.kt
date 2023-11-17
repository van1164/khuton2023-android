package com.example.khuton2023.ui.chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khuton2023.data.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChattingViewModel : ViewModel() {

    private val _messageList = MutableStateFlow<MutableList<Message>?>(null)
    val messageList: StateFlow<MutableList<Message>?> get() = _messageList

    private val _studyMateName = MutableStateFlow<String>("")
    val studyMateName: StateFlow<String> get() = _studyMateName

    fun setMessageList(messages: MutableList<Message>) {
        viewModelScope.launch {
            _messageList.update {
                it?.apply{
                    this.addAll(messages)
                }
            }
        }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            _studyMateName.update { name }
        }
    }


}