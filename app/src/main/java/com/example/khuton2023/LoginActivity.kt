package com.example.khuton2023

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.khuton2023.data.model.AppUser
import com.example.khuton2023.data.model.StudyMate
import com.example.khuton2023.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User

class LoginActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginBinding
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    loginToServer(user)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, "fd756b84a12e625ea49a548a5676f677")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(FirebaseAuth.getInstance().currentUser !=null){
            startMainActivity()
        }else{
            Log.d("XXXXXXXXXXXX", Utility.getKeyHash(this))
            binding.imageButton.setOnClickListener{
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                    loginWithKakaoApp()
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                }

            }
        }
        UserApiClient.instance.logout {  }
        UserApiClient.instance.unlink {  }


    }
    private fun loginWithKakaoApp() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡으로 로그인 실패", error)
                // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            } else if (token != null) {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        loginToServer(user)
                    }
                }

            }
        }
    }


    private fun loginToServer(kakaoUser: User) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(kakaoUser.kakaoAccount!!.email!!,"Ttink1245!")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    startMainActivity()
                } else {
                    creatUser(kakaoUser)
                }
            }
            Log.d("ABBABABABABABBAABAB",FirebaseAuth.getInstance().currentUser?.email.toString())


    }

    private fun creatUser(kakaoUser: User) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(kakaoUser.kakaoAccount!!.email!!, "Ttink1245!")
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {     //회원 가입 성공 시
                    try {
                        val user = FirebaseAuth.getInstance().currentUser
                        val userId = user?.uid
                        val userIdSt = userId.toString()
                        FirebaseDatabase.getInstance().getReference("User").child("users")
                            .child(userId.toString()).setValue(
                                AppUser(
                                    userId,
                                    userIdSt,
                                    kakaoUser.kakaoAccount!!.email
                                )
                            )             //Firebase RealtimeDatabase에 User 정보 추가
                        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("UserId", "$userId")
                        Log.i(TAG, "카카오톡으로 로그인 성공")
                        startMainActivity()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "화면 이동 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else
                    Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()

            }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

}