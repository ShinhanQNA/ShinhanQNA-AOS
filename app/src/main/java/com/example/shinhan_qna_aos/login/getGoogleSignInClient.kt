package com.example.shinhan_qna_aos.login

import android.content.Context
import com.example.shinhan_qna_aos.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

// GoogleSignInClient 생성
fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail() // 이메일 정보 요청
        .requestServerAuthCode(BuildConfig.GOOGLE_WEB_CLIENT_ID) // 서버 인가 코드 요청
        .build()
    return GoogleSignIn.getClient(context, gso)
}