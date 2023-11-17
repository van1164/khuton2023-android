package com.example.khuton2023.ui.chatting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.khuton2023.R
import com.example.khuton2023.RecyclerMessagesAdapter
import com.example.khuton2023.data.dao.ProblemResponse
import com.example.khuton2023.data.database.ChattingData
import com.example.khuton2023.data.model.ChatRoom
import com.example.khuton2023.data.model.Message
import com.example.khuton2023.databinding.ActivityChattingBinding
import com.example.khuton2023.network.service.KhutonService
import com.example.khuton2023.network.service.multiPartService
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingActivity : AppCompatActivity() {
    lateinit var binding: ActivityChattingBinding
    private val apiService = multiPartService.create()
    var lastQuestion: String = ""
    private val viewModel: ChattingViewModel by viewModels()
    val chatRoomDb: ChattingData by lazy {
        Room.databaseBuilder(
            this,
            ChattingData::class.java, "chatroom"
        ).allowMainThreadQueries().build()
    }

    val chatRoom: ChatRoom by lazy {
        chatRoomDb.chatRoomDao().findByStudyMateId(intent.extras!!["studyMateId"] as String).apply {
            viewModel.setName(this.studyMateName)
        }
    }
    val adapter: RecyclerMessagesAdapter by lazy {
        RecyclerMessagesAdapter(
            chatRoom.studyMateName,
            chatRoom.profileImage
        )
    }
    var messagList = mutableListOf<Message>()
    var isPro = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chatting)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
//        updateMessageList(chatRoom.messages)
        messagList.addAll(chatRoom.messages)
        adapter.submitList(messagList)


        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this).apply {
            this.stackFromEnd = true    // 가장 최근의 대화를 표시하기 위해 맨 아래로 정렬.
        }
        KhutonService.create()
            .createName(FirebaseAuth.getInstance().currentUser!!.uid, KhutonService.Request())
            .enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())
                    // 성공적으로 업로드되었을 때의 처리
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    // 업로드 실패 시의 처리
                    Log.d("TTTTTTTTTTTTTTTTTTTTTTTTTTTT", t.toString())
                }
            })
        generateQuiz(ProblemResponse(problems, ""), chatRoom)

        adapter.submitList(messagList)

        binding.sendButton.setOnClickListener {
            if ("문제" in binding.editTextText.text.toString() || "새로운" in binding.editTextText.text.toString()) {
//
//                if (isPro) {
//                    messagList.add(proMessage.copy(message = "새로운 문제를 내줄게."))
//                } else {
//                    messagList.add(karinaMessage.copy(message = "새로운 문제 갑니다~~"))
//                }
//                adapter.submitList(messagList)
//                adapter.notifyDataSetChanged()
//                binding.recyclerView.scrollToPosition(messagList.size - 1)
                generateQuiz(ProblemResponse(problems, ""), chatRoom)
            } else if (lastQuestion != "") {
                val message = Message(
                    chatRoom.studyMateId,
                    binding.editTextText.text.toString(),
                    false,
                    true,
                )
                binding.editTextText.text = Editable.Factory.getInstance().newEditable("")
                messagList.add(message)
                chatRoom.messages.add(message)
                chatRoomDb.chatRoomDao().update(chatRoom)
                adapter.submitList(messagList)
                adapter.notifyDataSetChanged()
                binding.recyclerView.scrollToPosition(messagList.size - 1)
                KhutonService.create().setAnswer(
                    FirebaseAuth.getInstance().uid!!,
                    lastQuestion,
                    binding.editTextText.text.toString(),
                    if (isPro) "prof" else "karina"
                ).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())
                        val message = Message(
                            chatRoom.studyMateId,
                            response.body().toString(),
                            true,
                            false,
                        )
                        messagList.add(message)
                        chatRoom.messages.add(message)
                        chatRoomDb.chatRoomDao().update(chatRoom)
                        adapter.submitList(messagList)
                        adapter.notifyDataSetChanged()
                        binding.recyclerView.scrollToPosition(messagList.size - 1)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        // 업로드 실패 시의 처리
                        Log.d("TTTTTTTTTTTTTTTTTTTTTTTTTTTT", t.toString())
                    }
                })
            }
            binding.editTextText.text = Editable.Factory.getInstance().newEditable("")
            adapter.submitList(messagList)
            adapter.notifyDataSetChanged()
            binding.recyclerView.scrollToPosition(messagList.size - 1)

        }
        binding.studyDocs.setOnClickListener {
//                intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//                intent.addCategory(Intent.CATEGORY_OPENABLE)
//                intent.type = "application/pdf"
//                startActivityForResult(intent, 1)
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"

            }

            startActivityForResult(intent, 1)

        }

    }

//    private fun updateMessageList(messages: MutableList<Message>) {
//        viewModel.setMessageList(messages)
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            2 -> {

            }

            0 -> {
                // 작업
            }

            1 -> {
                data?.let {
                    val uri = data.data
                    uri?.let { uri ->

                        // 사용 예시
                        val contentUri: Uri = uri// your content URI here
                        val contentResolver = this.contentResolver
                        val inputStream = contentResolver.openInputStream(contentUri)
                        val file = createTempFile("temp", null, this.cacheDir)
                        file.outputStream().use { inputStream?.copyTo(it) }

                        val requestFile = RequestBody.create(
                            contentResolver.getType(contentUri)!!.toMediaTypeOrNull(), file
                        )
                        val body =
                            MultipartBody.Part.createFormData("upload_file", file.name, requestFile)


                        val call = apiService.uploadFile(body)

                        call.enqueue(object : Callback<ProblemResponse> {
                            override fun onResponse(
                                call: Call<ProblemResponse>,
                                response: Response<ProblemResponse>
                            ) {
                                Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())

                                generateQuiz(response.body()!!, chatRoom)
                                // 성공적으로 업로드되었을 때의 처리
                            }

                            override fun onFailure(call: Call<ProblemResponse>, t: Throwable) {
                                // 업로드 실패 시의 처리
                                Log.d("TTTTTTTTTTTTTTTTTTTTTTTTTTTT", t.toString())
                            }
                        })


//                        val file = File(uri.path!!)
//                        val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//                        val part = MultipartBody.Part.createFormData(
//                            "upload_file",
//                            file.name,
//                            requestFile
//                        )
//                        Log.e("AAAAAAAAAAAAAAAAA",requestFile.toString())
//                        Log.e("NNNNNNNNNNNNNNNNNNNNNN",file.name)
//
//                        multiPartService.create().search(part)
//                            .enqueue(object : Callback<ProblemResponse> {
//                                override fun onResponse(
//                                    call: Call<ProblemResponse>,
//                                    response: Response<ProblemResponse>
//                                ) {
//                                    Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())
//                                }
//                                override fun onFailure(call: Call<ProblemResponse>, t: Throwable) {
//                                    Log.d("TTTTTTTTTTTTTTTTTTTTTTTTTTTT", t.toString())
//                                }
//                            })

                    }
                }
            }
        }
    }

    private fun generateQuiz(response: ProblemResponse, chatRoom: ChatRoom) {
        KhutonService.create().createProblem(
            FirebaseAuth.getInstance().currentUser!!.uid,
            hashMapOf(Pair("problems", response.problems))
        ).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())
                KhutonService.create().getQuiz(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    if (isPro) "prof" else "karina"
                )
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())
                            val message = Message(
                                chatRoom.studyMateId,
                                response.body().toString(),
                                true,
                                false,
                            )
                            messagList.add(message)
                            chatRoom.messages.add(message)
                            chatRoomDb.chatRoomDao().update(chatRoom)
                            adapter.submitList(messagList)
                            adapter.notifyDataSetChanged()
                            binding.recyclerView.scrollToPosition(messagList.size - 1)
                            lastQuestion = response.body()!!
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            // 업로드 실패 시의 처리
                            Log.d("TTTTTTTTTTTTTTTTTTTTTTTTTTTT", t.toString())
                        }
                    })
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // 업로드 실패 시의 처리
                Log.d("TTTTTTTTTTTTTTTTTTTTTTTTTTTT", t.toString())
            }
        })
    }
}