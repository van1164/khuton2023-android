package com.example.khuton2023.ui.create_study_mate


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.khuton2023.data.model.Birth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CreateStudyMateViewModel : ViewModel() {

    private val _birth = MutableStateFlow<Birth>(Birth(1999, 1, 1))
    val birth: StateFlow<Birth> get() = _birth


    fun setBirth(year: Int, month: Int, day: Int) {
        _birth.update {
            Birth(year, month, day)
        }
    }
}