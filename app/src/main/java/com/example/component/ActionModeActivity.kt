package com.example.component

import android.app.Activity
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class ActionModeActivity : Activity() {

    private val items = mutableListOf("One", "Two", "Three", "Four", "Five")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actionmode)

        val listView = findViewById<ListView>(R.id.lv_actionmode)
        val adapter = ArrayAdapter<String>(this, R.layout.actionmode_item, R.id.am_name, items)
        listView.adapter = adapter

        // 开启多选模态模式，长按列表项进入 ActionMode 上下文操作模式
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        listView.setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {

            override fun onItemCheckedStateChanged(
                mode: ActionMode,
                position: Int,
                id: Long,
                checked: Boolean
            ) {
                mode.title = "${listView.checkedItemCount} selected"
            }

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                mode.menuInflater.inflate(R.menu.menu_context, menu)
                mode.title = "${listView.checkedItemCount} selected"
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_delete -> {
                        val checked = listView.checkedItemPositions
                        for (i in items.indices.reversed()) {
                            if (checked.get(i)) {
                                items.removeAt(i)
                            }
                        }
                        checked.clear()
                        adapter.notifyDataSetChanged()
                        mode.finish()
                        Toast.makeText(
                            this@ActionModeActivity,
                            "已删除所选条目",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode) = Unit
        })
    }
}
