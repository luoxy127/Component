package com.example.component

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class DialogActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        findViewById<Button>(R.id.btn_show_dialog).setOnClickListener {
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        val Username = EditText(this)
        Username.hint = "Username"
        val Pwd = EditText(this)
        Pwd.hint = "Password"

        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(Username)
            addView(Pwd)
            setPadding(50,20,50,20)
        }

        AlertDialog.Builder(this)
            .setTitle("ANDROID APP")
            .setView(layout)
            .setPositiveButton("Sign in") { _, _ ->
                val name = Username.text
                val pwd = Pwd.text
                Toast.makeText(this, "用户名：$name，密码：$pwd", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this, "取消登录", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

}
