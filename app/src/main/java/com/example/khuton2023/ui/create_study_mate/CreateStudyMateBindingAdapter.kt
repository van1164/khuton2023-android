package com.example.khuton2023.ui.create_study_mate

import android.widget.Button
import androidx.databinding.BindingAdapter
import com.example.khuton2023.data.model.Birth

object CreateStudyMateBindingAdapter {
    @JvmStatic
    @BindingAdapter("birthDate")
    fun setBirthDate(button: Button, birth: Birth) {
        val formattedDate = "${birth.year}-${birth.month}-${birth.day}"
        button.text = formattedDate
    }
}