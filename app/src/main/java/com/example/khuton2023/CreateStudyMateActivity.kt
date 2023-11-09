package com.example.khuton2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.khuton2023.databinding.ActivityCreateStudyMateBinding
import com.example.khuton2023.databinding.ActivityMainBinding

class CreateStudyMateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStudyMateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStudyMateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val spinner = binding.spinner
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.mbti_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener =  object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                return
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }
        }

    }

}