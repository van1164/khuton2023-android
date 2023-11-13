package com.example.khuton2023

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
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
import java.util.Calendar

class CreateStudyMateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStudyMateBinding

    var yearItem = 1999
    var monthItem = 1
    var dayItem = 1
    var mbti = Mbti.ISFP
    var profileImage: Bitmap? = null
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            }
            binding.selectProfileButton.setImageBitmap(
                Bitmap.createScaledBitmap(
                    bitmap,
                    500,
                    700,
                    true
                )
            )
            profileImage = Bitmap.createScaledBitmap(
                bitmap,
                500,
                700,
                true
            )
            binding.selectProfileButton.background = getDrawable(R.drawable.border_profile)
            binding.selectProfileButton.clipToOutline = true
            binding.selectProfileButton.scaleType = ImageView.ScaleType.CENTER_CROP
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            this,
            StudyMateData::class.java, "studymate"
        ).allowMainThreadQueries().build()

        val chatRoomListDb = Room.databaseBuilder(
            this,
            ChattingRoomListDatabase::class.java,
            "ChatRoomList"
        ).allowMainThreadQueries().build()

        val chatRoomDb = Room.databaseBuilder(
            this,
            ChattingData::class.java,
            "chatroom"
        ).allowMainThreadQueries().build()
        binding = ActivityCreateStudyMateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSpinner()
        initBirthSelectButton()
        binding.selectProfileButton.setOnClickListener {
            getContent.launch("image/*")
        }
        binding.nextButton.setOnClickListener {
//            val storage = FirebaseStorage.getInstance()
//            val storageRef = storage.reference
//            val uuid = FirebaseAuth.getInstance().currentUser!!.uid
//
//            val riverRef =
//                storageRef.child("images/${uuid}+/${binding.nameEditText.text.toString()}")
//            var downloadUrl: String? = null
//            profileImage?.let {
//                Log.d("VVVVVVVVVVVVV", "VVVVVVVVVVVVVVVVVVV")
//                val baos = ByteArrayOutputStream()
//                it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//                val task = riverRef.putBytes(baos.toByteArray()).addOnFailureListener {
//                    Log.d("XXXXXXXXXXXX", "XXXXXXXXXXXXXXXXX")
//                }.addOnSuccessListener {
//                    downloadUrl = "images/${uuid}+/${binding.nameEditText.text.toString()}"
//                    val database = Firebase.database.reference
//                    val studyMate = StudyMate(
//                        binding.nameEditText.text.toString(),
//                        yearItem,
//                        monthItem,
//                        dayItem,
//                        findMbti(binding.spinner.selectedItem.toString()),
//                        downloadUrl,
//                        uuid
//                    )
//                    Log.d("AAAAAAAAAAAAAAAAA", studyMate.toString())
//                    database.child("studyMates").push().setValue(
//                        studyMate
//                    )
//                    var chatRoom = ChatRoom(         //추가할 채팅방 정보 세팅
//                        "",
//                        listOf<Message>(),
//                    )
//                    var chatDatabase = FirebaseDatabase.getInstance().getReference("ChatRoom")
//                    chatDatabase.child("chatRooms").push().setValue(chatRoom)
//                }
//
//                task.continueWithTask { task ->
//                    if (!task.isSuccessful) {
//                        task.exception?.let {
//                            throw it
//                        }
//                    }
//                    riverRef.downloadUrl
//                }.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                    } else {
//                    }
//
//                }
//
//            }
            val studyMateId = UidGenerator().generateUID()
            val studyMate = StudyMate(
                binding.nameEditText.text.toString(),
                yearItem,
                monthItem,
                dayItem,
                findMbti(binding.spinner.selectedItem.toString()),
                profileImage,
                studyMateId
            )


            db.studyMateDao().insert(
                studyMate
            )
            chatRoomListDb.chatRoomListDao().insert(
                ChatRoomList(
                    studyMate.name,
                    studyMateId,
                    "",
                    true,
                    studyMate.profileImage
                )
            )
            chatRoomDb.chatRoomDao().insert(
                ChatRoom(
                    studyMate.name,
                    studyMateId,
                    messages = mutableListOf<Message>(),
                    studyMate.profileImage
                )
            )


            val intent = Intent(this@CreateStudyMateActivity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }

    private fun findMbti(mbti: String): Mbti {
        return Mbti.values().associateBy(Mbti::name)[mbti]!!
    }

    private fun initBirthSelectButton() {
        binding.birthSelectButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                yearItem = year
                monthItem = month
                dayItem = day
                binding.birthSelectButton.text = "${yearItem}-${monthItem + 1}-${dayItem}"
            }
            DatePickerDialog(
                this,
                data,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
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