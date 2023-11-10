package com.example.khuton2023

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khuton2023.data.dao.ProblemResponse
import com.example.khuton2023.data.model.Mbti
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.databinding.ActivityChattingBinding
import com.example.khuton2023.network.service.KhutonService
import com.example.khuton2023.network.service.multiPartService
import com.example.khuton2023.ui.dashboard.Message
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChattingActivity : AppCompatActivity() {
    fun isNotificationEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context.getApplicationContext())
            .areNotificationsEnabled()
    }
    lateinit var binding: ActivityChattingBinding
    val apiService = multiPartService.create()
    val adapter = RecyclerMessagesAdapter()
    var lastQuestion : String =""
    val messagList = mutableListOf(
        Message(
            StudyMate(
                "카리나", 2000, 1, 1,
                Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
            ), "용우야, \n 밥먹었어?", true
        ),
        Message(
            StudyMate(
                "카리나", 2000, 1, 1,
                Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
            ), "test", true
        ),
        Message(
            StudyMate(
                "카리나", 2000, 1, 1,
                Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
            ), "test", true
        ),
        Message(
            StudyMate(
                "카리나", 2000, 1, 1,
                Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
            ), "test", true
        ),
        Message(
            StudyMate(
                "카리나", 2000, 1, 1,
                Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
            ), "test", true
        ),
        Message(
            StudyMate(
                "카리나", 2000, 1, 1,
                Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
            ), "싫어", false
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNotificationEnabled(this)
        setContentView(R.layout.activity_chatting)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val adapter = ChatRecyclerViewAdapter()
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = adapter

//        val list = listOf(Message(
//            StudyMate("카리나",2000,1,1,
//                Mbti.ENFJ,"images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"),"test",true))
//        adapter.submitList(list)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this).apply {
            this.stackFromEnd = true	// 가장 최근의 대화를 표시하기 위해 맨 아래로 정렬.
        }
        KhutonService.create().createName(FirebaseAuth.getInstance().currentUser!!.uid,KhutonService.Request()).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())
                // 성공적으로 업로드되었을 때의 처리
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // 업로드 실패 시의 처리
                Log.d("TTTTTTTTTTTTTTTTTTTTTTTTTTTT", t.toString())
            }
        })


            adapter.submitList(messagList)

            binding.sendButton.setOnClickListener {
                messagList.add(
                    Message(
                        StudyMate(
                            "카리나", 2000, 1, 1,
                            Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                        ), binding.editTextText.text.toString(), false
                    )
                )
                if(lastQuestion!=""){
                    KhutonService.create().setAnswer(FirebaseAuth.getInstance().uid!!,lastQuestion,binding.editTextText.text.toString()).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())
                            messagList.add(
                                Message(
                                    StudyMate(
                                        "카리나", 2000, 1, 1,
                                        Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                                    ), response.body()!!, true
                                )
                            )
                            adapter.submitList(messagList)
                            adapter.notifyDataSetChanged()
                            binding.recyclerView.scrollToPosition(messagList.size-1)
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            // 업로드 실패 시의 처리
                            Log.d("TTTTTTTTTTTTTTTTTTTTTTTTTTTT", t.toString())
                        }
                    })
                }
                binding.editTextText.text = Editable.Factory.getInstance().newEditable("")
                Log.d("XXXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXXXXXX")
                adapter.submitList(messagList)
                adapter.notifyDataSetChanged()
                binding.recyclerView.scrollToPosition(messagList.size-1)

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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            2->{

            }
            0 -> {
                // 작업
            }

            1 -> {
                Log.d("XXXXXXXXXXXX","XXXXXXXXXXXXXXXXXxx")
                data?.let{
                    val uri = data.data
                    uri?.let{uri->

                        // 사용 예시
                        val contentUri: Uri = uri// your content URI here
                        val contentResolver = this.contentResolver
                        val inputStream = contentResolver.openInputStream(contentUri)
                        val file = createTempFile("temp", null, this.cacheDir)
                        file.outputStream().use { inputStream?.copyTo(it) }

                        val requestFile = RequestBody.create(
                            contentResolver.getType(contentUri)!!.toMediaTypeOrNull(), file)
                        val body = MultipartBody.Part.createFormData("upload_file", file.name, requestFile)




                        val call = apiService.uploadFile(body)

                        call.enqueue(object : Callback<ProblemResponse> {
                            override fun onResponse(call: Call<ProblemResponse>, response: Response<ProblemResponse>) {
                                Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())

                                KhutonService.create().createProblem(FirebaseAuth.getInstance().currentUser!!.uid,hashMapOf(Pair("problems",response.body()!!.problems))).enqueue(object : Callback<Any> {
                                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                        Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())
                                        KhutonService.create().getQuiz(FirebaseAuth.getInstance().currentUser!!.uid).enqueue(object : Callback<String> {
                                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                                Log.d("BBBBBBBBBBBBBBBBBBBBBBBB", response.body().toString())
                                                messagList.add(
                                                    Message(
                                                        StudyMate(
                                                            "카리나", 2000, 1, 1,
                                                            Mbti.ENFJ, "images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"
                                                        ), response.body()!!, true
                                                    )
                                                    )

                                                adapter.submitList(messagList)
                                                adapter.notifyDataSetChanged()
                                                binding.recyclerView.scrollToPosition(messagList.size-1)
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
    }