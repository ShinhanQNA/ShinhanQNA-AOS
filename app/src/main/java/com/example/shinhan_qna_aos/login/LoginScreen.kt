package com.example.shinhan_qna_aos.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.login.loginserver.sendGoogleAuthCodeToServer
import com.example.shinhan_qna_aos.login.loginserver.sendKakaoOpenIdTokenToServer
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kakao.sdk.user.UserApiClient

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val TAG = "kakao"

    val googleSignInClient = getGoogleSignInClient(context)

    // Google 로그인 결과를 처리하는 런처
    val googleAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
            val authCode = account.serverAuthCode
            Log.d("GoogleLogin", "받은 인가코드: $authCode")

            if (authCode != null) {
                sendGoogleAuthCodeToServer(authCode, "google")
            }
        } catch (e: com.google.android.gms.common.api.ApiException) {
            Log.e("GoogleLogin", "Google 로그인 실패: ${e.statusCode}")
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
            .padding(top = 64.dp, start = 40.dp, end = 40.dp, bottom = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        val maxwidth =maxWidth
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.biglogo),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 카카오 로그인 버튼
                Image(
                    painter = painterResource(R.drawable.kakao_login),
                    contentDescription = "카카오 로그인",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 카카오톡이 설치되어 있다면 카카오톡으로 로그인, 아니면 카카오 계정으로 로그인
                            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                                    if (error != null) {
                                        Log.e(TAG, "카카오톡 로그인 실패", error)
                                        // 카카오 계정으로 로그인 시도
                                        UserApiClient.instance.loginWithKakaoAccount(context) { accountToken, accountError ->
                                            if (accountError != null) {
                                                Log.e(TAG, "카카오 계정 로그인 실패", accountError)
                                            } else if (accountToken != null) {
                                                Log.d(TAG, "카카오 계정 로그인 성공")
                                                // 토큰에서 idToken을 추출하여 서버로 전송
                                                accountToken.idToken?.let { idToken ->
                                                    sendKakaoOpenIdTokenToServer(idToken, TAG)
                                                }
                                            }
                                        }
                                    } else if (token != null) {
                                        Log.d(TAG, "카카오톡 로그인 성공")
                                        // 토큰에서 idToken을 추출하여 서버로 전송
                                        token.idToken?.let { idToken ->
                                            sendKakaoOpenIdTokenToServer(idToken, TAG)
                                        }
                                    }
                                }
                            } else {
                                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                                    if (error != null) {
                                        Log.e(TAG, "카카오 계정 로그인 실패", error)
                                    } else if (token != null) {
                                        Log.d(TAG, "카카오 계정 로그인 성공")
                                        // 토큰에서 idToken을 추출하여 서버로 전송
                                        token.idToken?.let { idToken ->
                                            sendKakaoOpenIdTokenToServer(idToken, TAG)
                                        }
                                    }
                                }
                            }
                        }
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 구글 로그인 버튼
                Image(
                    painter = painterResource(R.drawable.google_login),
                    contentDescription = "구글 로그인",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            googleAuthLauncher.launch(googleSignInClient.signInIntent)
                        }
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "관리자 전용 페이지",
                    color = Color(0xffDFDFDF),
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.clickable { /* 관리자 로그인 처리 */ }
                )
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun loginpreview(){
    LoginScreen()
}