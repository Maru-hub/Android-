package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserExist {
    //ユーザー認証
    private val auth = Firebase.auth
    private val user = auth.currentUser
    private val userId = user?.uid
     fun LoginCheck(context: Context):Boolean{
        var state = false
        if(user != null){
            state = true
            Log.d(TAG, "$userId")
        }else{
            Log.e(TAG, "ユーザーがログインしていません")
                Toast.makeText(context, "ログインが必要です", Toast.LENGTH_SHORT).show()
            }
        return state
    }
}