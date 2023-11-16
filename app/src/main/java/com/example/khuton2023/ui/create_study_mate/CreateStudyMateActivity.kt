package com.example.khuton2023.ui.create_study_mate

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import com.example.khuton2023.R
import com.example.khuton2023.data.database.ChattingData
import com.example.khuton2023.data.database.ChattingRoomListDatabase
import com.example.khuton2023.data.database.StudyMateData
import com.example.khuton2023.data.model.ChatRoom
import com.example.khuton2023.data.model.ChatRoomList
import com.example.khuton2023.data.model.Mbti
import com.example.khuton2023.data.model.Message
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.data.util.UidGenerator
import com.example.khuton2023.databinding.ActivityCreateStudyMateBinding
import com.example.khuton2023.network.service.KhutonService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class CreateStudyMateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStudyMateBinding
    val viewModel : CreateStudyMateViewModel by viewModels()
    var profileImage: Bitmap? = null

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            }
            profileImage = Bitmap.createScaledBitmap(
                bitmap,
                500,
                700,
                true
            )
            binding.selectProfileButton.setImageBitmap(
                profileImage
            )
            binding.selectProfileButton.background = getDrawable(R.drawable.border_profile)
            binding.selectProfileButton.clipToOutline = true
            binding.selectProfileButton.scaleType = ImageView.ScaleType.CENTER_CROP
        }

    }

    private lateinit var studyMateDB : StudyMateData
    private lateinit var chatRoomListDb : ChattingRoomListDatabase
    private lateinit var chatRoomDb : ChattingData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_study_mate)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this


        studyMateDB = Room.databaseBuilder(
            this,
            StudyMateData::class.java, "studymate"
        ).allowMainThreadQueries().build()

        chatRoomListDb = Room.databaseBuilder(
            this,
            ChattingRoomListDatabase::class.java,
            "ChatRoomList"
        ).allowMainThreadQueries().build()

        chatRoomDb = Room.databaseBuilder(
            applicationContext,
            ChattingData::class.java,
            "chatroom"
        ).allowMainThreadQueries().build()


        initSpinner()
        initListener()

    }

    fun clickBirthSelectButton() {
        Log.d("XXXXXXXXX","XXXXXXXXXXXXXXX")
        val cal = Calendar.getInstance()
        val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            viewModel.setBirth(year,month,day)
        }
        DatePickerDialog(
            this,
            data,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun initListener() {
        binding.selectProfileButton.setOnClickListener {
            getContent.launch("image/*")
        }
        binding.nextButton.setOnClickListener {
            val studyMateId = UidGenerator().generateUID()
            val studyMate = StudyMate(
                binding.nameEditText.text.toString(),
                viewModel.birth.value.year,
                viewModel.birth.value.month,
                viewModel.birth.value.day,
                findMbti(binding.spinner.selectedItem.toString()),
                profileImage,
                studyMateId
            )

            studyMateDB.studyMateDao().insert(
                studyMate
            )


            getWelcomeMessage(studyMate, studyMateId)

            finish()
        }
    }

    private fun getWelcomeMessage(
        studyMate: StudyMate,
        studyMateId: String
    ) {
        GlobalScope.launch {
            var message: String
            runBlocking {
                message = getWelcome()
                insertMessageToChatRoom(studyMate, studyMateId, message)
            }

            insertMessageToChatRoomList(studyMate, studyMateId, message)
        }
    }

    private fun insertMessageToChatRoomList(
        studyMate: StudyMate,
        studyMateId: String,
        message: String
    ) {
        chatRoomListDb.chatRoomListDao().insert(
            ChatRoomList(
                studyMate.name,
                studyMateId,
                message,
                true,
                studyMate.profileImage
            )
        )
    }

    private fun insertMessageToChatRoom(
        studyMate: StudyMate,
        studyMateId: String,
        message: String
    ) {
        val chatRoom = ChatRoom(
            studyMate.name,
            studyMateId,
            messages = mutableListOf<Message>(Message(studyMateId, message, true)),
            studyMate.profileImage
        )
        chatRoomDb.chatRoomDao().insert(
            chatRoom
        )
    }


    private fun getWelcome(): String {
        return KhutonService.create().getWelcome(
            FirebaseAuth.getInstance().currentUser!!.uid,
            "karina"
        ).execute().body().toString()
    }

    private fun findMbti(mbti: String): Mbti {
        return Mbti.values().associateBy(Mbti::name)[mbti]!!
    }

    private fun initSpinner() {
        val spinner = binding.spinner
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.mbti_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                return
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }
        }
    }

}