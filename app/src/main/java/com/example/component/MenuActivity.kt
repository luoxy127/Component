package com.example.component

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

class MenuActivity : Activity() {
    private lateinit var tvTest: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        tvTest = findViewById(R.id.tv_test)
    }

    /** 使用 XML 定义的选项菜单 */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.font_small -> { tvTest.textSize = 10f; true }
            R.id.font_medium -> { tvTest.textSize = 16f; true }
            R.id.font_large -> { tvTest.textSize = 20f; true }
            R.id.menu_normal -> {
                Toast.makeText(this, "你点击了普通菜单项", Toast.LENGTH_SHORT).show(); true
            }
            R.id.color_red -> { tvTest.setTextColor(Color.RED); true }
            R.id.color_black -> { tvTest.setTextColor(Color.BLACK); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
