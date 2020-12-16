package com.example.fingerprintkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //biometricManager
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "지문인식 센서가 없습니다!", Toast.LENGTH_SHORT).show()
                loginBtn.setVisibility(View.GONE)
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "지문인식센서를 사용할 수 없어요!", Toast.LENGTH_SHORT).show()
                loginBtn.setVisibility(View.GONE)
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Toast.makeText(this, "지문인식을 저장할 수 없어요, 설정에 가서 확인해주세요!", Toast.LENGTH_SHORT).show()
        }

        val executor:Executor = ContextCompat.getMainExecutor(this)


        val biometricPrompt = BiometricPrompt(this@MainActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        }) //결과 알려줌

        val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("로그인")
                .setDescription("지문을 인식해주세요.")
                .setNegativeButtonText("취소")
                .build()

        loginBtn.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

    }
}