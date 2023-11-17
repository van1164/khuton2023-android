package com.example.khuton2023.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khuton2023.data.database.StudyMateData
import com.example.khuton2023.data.model.StudyMate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _studyMateList = MutableStateFlow<List<StudyMate>?>(null)
    val studyMateList: StateFlow<List<StudyMate>?> = _studyMateList.asStateFlow()

    fun getStudyMateList(db : StudyMateData){
        viewModelScope.launch {
            val list = db.studyMateDao().getAll()
            _studyMateList.update { list }
        }
    }

}