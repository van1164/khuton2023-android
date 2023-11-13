package com.example.khuton2023

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.khuton2023.data.dao.Problem
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


val problems = listOf(
    Problem(
        2,
        "Copyleft란 무엇인가요?",
        "Copyleft는 저작권을 보호하면서 자유 소프트웨어의 사용을 가능하게 하는 개념입니다."
    ),
    Problem(
        3,
        "GNU 조직은 무엇인가요?",
        "Richard Stallman이 이끄는 GNU 조직은 자유 소프트웨어 운동에서 중요한 인물로, GNU 프로젝트를 시작했습니다."
    ),
    Problem(
        7,
        "리팩토링이란 무엇인가요?",
        "리팩토링은 코드의 구조와 유지보수성을 개선하기 위해 코드를 재구성하는 과정입니다. 목표는 코드의 가독성과 유지보수성을 향상시키는 것입니다."
    ),
    Problem(
        10,
        "Python의 주요 목적은 무엇인가요?",
        "Python의 주요 목적은 빠른 개발과 다른 언어와의 통합입니다. Python은 빠른 개발과 상호작용이 가능한 시각적으로 매력적인 웹사이트를 만드는 데 사용됩니다."
    ),
    Problem(
        11,
        "마이크로서비스 분야에서 언어 선택에 제한이 적은 이유는 무엇인가요?",
        "마이크로서비스 분야에서 언어 선택에 제한이 적은 이유는 마이크로서비스를 다양한 언어와 기술을 사용하여 개발할 수 있기 때문입니다."
    ),
    Problem(
        12,
        "애자일 선언문의 네 가지 항목은 무엇인가요?",
        "애자일 선언문의 네 가지 항목은 프로세스와 도구보다 개인과 상호작용을 우선시하는 것, 포괄적인 문서보다 작동하는 소프트웨어를 우선시하는 것, 계약 협상보다 고객과의 협력을 우선시하는 것, 계획에 따르기보다 변화에 대응하는 것입니다."
    ),
    Problem(
        13,
        "애자일 선언문에서 개인과 상호작용을 우선시하는 원칙은 무엇인가요?",
        "개인과 상호작용을 우선시하는 원칙은 프로세스와 도구보다 개인과 상호작용을 중요시하는 것을 의미합니다."
    ),
    Problem(
        14,
        "CI/CD는 무엇의 약자인가요?",
        "CI/CD는 Continuous Integration and Continuous Deployment의 약자로, 개발 과정에서 테스트와 배포를 자동화하여 품질을 유지하고 신속한 배포를 가능하게 하는 관행을 말합니다."
    ),
    Problem(
        15,
        "DevOps의 단계는 무엇인가요?",
        "DevOps의 단계는 계획, 개발, 테스트, 배포, 운영 및 모니터링입니다. 각 단계는 소프트웨어 개발과 운영의 다른 측면을 나타냅니다."
    ),
    Problem(
        21,
        "중앙 집중식 VCS와 분산 VCS의 차이점은 무엇인가요?",
        "중앙 집중식 VCS는 중앙 서버에서 파일을 관리하는 반면, 분산 VCS는 각 개발자가 로컬에서 파일을 관리할 수 있습니다. Git은 분산 VCS의 예시입니다."
    ),
    Problem(
        22,
        "버전 관리 용어에는 어떤 것들이 있나요?",
        "Trunk(주요 라인)은 주요 개발 라인을 의미하며, Branch는 개별 작업을 위한 별도의 라인을 의미하며, Merge는 분기된 작업을 다시 결합하는 과정을 의미합니다."
    )
)

class ChattingActivity : AppCompatActivity() {


    lateinit var binding: ActivityChattingBinding
    val apiService = multiPartService.create()
    var lastQuestion: String = ""
    val chatRoomDb: ChattingData by lazy {
        Room.databaseBuilder(
            this,
            ChattingData::class.java, "chatroom"
        ).allowMainThreadQueries().build()
    }

    val chatRoom: ChatRoom by lazy {
        chatRoomDb.chatRoomDao().findByStudyMateId(intent.extras!!["studyMateId"] as String)
    }
    val adapter: RecyclerMessagesAdapter by lazy {RecyclerMessagesAdapter(chatRoom.studyMateName)}
    var messagList = mutableListOf<Message>()
    var isPro = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messagList.addAll(chatRoom.messages)
        Log.d("AAAAAAAAAAAAAAA",messagList.toString())
        binding.textView9.text = chatRoom.studyMateName
        adapter.submitList(messagList)



//        val adapter = ChatRecyclerViewAdapter()
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = adapter

//        val list = listOf(Message(
//            StudyMate("카리나",2000,1,1,
//                Mbti.ENFJ,"images/elTjMhSZ4Xh7zmx5gm13I8Opw6d2+/드"),"test",true))
//        adapter.submitList(list)

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
                    chatRoom.profileImage
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
                            chatRoom.profileImage
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
                                chatRoom.profileImage
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