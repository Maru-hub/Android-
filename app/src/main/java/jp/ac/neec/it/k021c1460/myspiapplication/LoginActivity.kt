package jp.ac.neec.it.k021c1460.myspiapplication

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.firestore

class LoginActivity : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        val listener = HelloListener()
        val btLogin = findViewById<Button>(R.id.emailLoginInButton)
        val btCreate = findViewById<Button>(R.id.emailCreateAccountButton)
        val btLogout = findViewById<Button>(R.id.signOutButton)
        btLogout.setOnClickListener(listener)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btLogin.setOnClickListener(listener)
        btCreate.setOnClickListener(listener)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 戻り値用の変数を初期値trueで用意
        var returnVal = true
        // 選択されたメニューが「戻る」の場合、アクティビティを終了。
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }

    private inner class HelloListener : View.OnClickListener {
        override fun onClick(p0: View) {
            val et_email = findViewById<EditText>(R.id.fieldEmail)
            val email = et_email.text.toString()
            val et_pass = findViewById<EditText>(R.id.fieldPassword)
            val pass = et_pass.text.toString()
            when (p0.id) {
                R.id.emailLoginInButton -> {
                    if(email != "" && pass != ""){
                        signIn(email,pass)
                    }
                }
                R.id.emailCreateAccountButton -> {
                    if(email != "" && pass != ""){
                        createAccount(email,pass)
                    }
                }
                R.id.signOutButton -> {
                    signOut()
                }
            }
        }
    }
    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }
    // [END on_start_check_user]

    private fun validateForm(): Boolean {
        var valid = true
        val tv_email = findViewById<EditText>(R.id.fieldEmail)
        val email = tv_email.toString()
        if (email == "") {
            valid = false
        }
        val tv_pass = findViewById<EditText>(R.id.fieldPassword)
        val pass = tv_pass.toString()
        if (pass == "") {
            valid = false
        }
        return valid
    }

    private fun signOut() {
        auth.signOut()
        updateUI(null)
    }

    val db = Firebase.firestore
    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        val userId = user?.uid.toString()
                        val userName = user?.email?.split("@")?.get(0) ?: ""
                        val data = hashMapOf("name" to "$userName")
                        db.collection("users").document(userId).set(data) // userId = uid
                        updateUI(user)
                    } else {
                        // アカウント作成に失敗した場合、エラーハンドリング
                        try {
                            throw task.exception ?: Exception("Unknown error")
                        } catch (e: FirebaseAuthUserCollisionException) {
                            // 同じメールアドレスのアカウントが存在する場合
                            Toast.makeText(
                                this@LoginActivity,
                                "このメールアドレスは既に使用されています。",
                                Toast.LENGTH_SHORT
                            ).show()
                            val et_email = findViewById<EditText>(R.id.fieldEmail)
                            et_email.setText("")
                            val et_pass = findViewById<EditText>(R.id.fieldPassword)
                            et_pass.setText("")
                        } catch (e: Exception) {
                            // その他のエラー
                            Log.w(TAG, "createUserWithEmail:failure", e)
                            Toast.makeText(
                                this@LoginActivity,
                                "認証に失敗しました。",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "login process failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            // [END sign_in_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {
        val et_email = findViewById<EditText>(R.id.fieldEmail)
        et_email.setText("")
        val et_pass = findViewById<EditText>(R.id.fieldPassword)
        et_pass.setText("")

        val tv_title = findViewById<TextView>(R.id.titleText)
        val tv_statue = findViewById<TextView>(R.id.status)
        val tv_detail = findViewById<TextView>(R.id.detail)
        val bt_signOut = findViewById<Button>(R.id.signOutButton)

        if (user != null){
            tv_title.text = user.email
            tv_statue.text = "ログインしています"
            tv_detail.text = user.uid
            bt_signOut.visibility = View.VISIBLE
        }
        else{
            tv_title.text = ""
            tv_statue.text = "未ログイン"
            tv_detail.text = ""
            bt_signOut.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}