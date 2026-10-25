package com.example.component

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val labels = arrayOf(
            getString(R.string.exp1),
            getString(R.string.exp2),
            getString(R.string.exp3),
            getString(R.string.exp4),
            getString(R.string.exp5)
        )
        val targets = arrayOf<Class<*>>(
            ListViewActivity::class.java,
            DialogActivity::class.java,
            MenuActivity::class.java,
            ActionModeActivity::class.java,
            RecyclerViewActivity::class.java
        )

        val list = findViewById<ListView>(R.id.main_list)
        list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, labels)
        list.setOnItemClickListener { _, _, position, _ ->
            startActivity(Intent(this, targets[position]))
        }
    }
}
