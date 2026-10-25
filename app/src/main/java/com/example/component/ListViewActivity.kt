package com.example.component

import android.graphics.BitmapFactory
import android.graphics.Color
import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class ListViewActivity : Activity() {

    companion object {
        private const val CHANNEL_ID = "animal_channel"
        private const val REQUEST_NOTIFICATION = 100
        private const val COLOR_SELECTED = 0xFF8B0000.toInt() // 深红色（PPT配图色号）
    }

    private var selectedPosition = -1
    private lateinit var listView: ListView

    private val animals = arrayOf("Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant")
    private val images = intArrayOf(
        R.drawable.lion,
        R.drawable.tiger,
        R.drawable.monkey,
        R.drawable.dog,
        R.drawable.cat,
        R.drawable.elephant
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview)

        requestNotificationPermission()

        val data = ArrayList<HashMap<String, Any>>()
        for (i in animals.indices) {
            val map = HashMap<String, Any>()
            map["name"] = animals[i]
            map["image"] = images[i]
            data.add(map)
        }

        listView = findViewById<ListView>(R.id.listview_animals)
        val adapter = SimpleAdapter(
            this,
            data,
            R.layout.listview_item,
            arrayOf("name", "image"),
            intArrayOf(R.id.item_name, R.id.item_image)
        )
        listView.adapter = adapter

        listView.onItemClickListener = android.widget.AdapterView.OnItemClickListener { _, _, position, _ ->
            val name = animals[position]
            showNameToast(name)
            sendNotification(name, position)

            // 清除之前选中项的背景
            if (selectedPosition != -1) {
                val prevView = getVisibleView(selectedPosition)
                prevView?.setBackgroundColor(Color.TRANSPARENT)
                val prevText = prevView?.findViewById<TextView>(R.id.item_name)
                prevText?.setTextColor(Color.BLACK)
            }

            // 设置当前选中项背景为深红色
            val currentView = getVisibleView(position)
            currentView?.setBackgroundColor(COLOR_SELECTED)
            val currentText = currentView?.findViewById<TextView>(R.id.item_name)
            currentText?.setTextColor(Color.WHITE)

            selectedPosition = position
        }
    }

    /** 获取列表中某位置的可见View（如果不可见返回null） */
    private fun getVisibleView(position: Int): View? {
        val index = position - listView.firstVisiblePosition
        return if (index in 0 until listView.childCount) {
            listView.getChildAt(index)
        } else {
            null
        }
    }

    /** Android 13 及以上需要运行时申请通知权限 */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION
                )
            }
        }
    }

    /** 自定义 Toast：只显示名字，不带系统应用图标 */
    private fun showNameToast(name: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.toast_name, null)
        view.findViewById<TextView>(R.id.toast_name_text).text = name
        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }

    /** 发送通知：小图标为应用图标，大图标为对应选项的动物图片，标题为列表项内容，内容自拟 */
    private fun sendNotification(title: String, position: Int) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "动物列表通知",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        val largeIcon = BitmapFactory.decodeResource(resources, images[position])
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText("你点击了列表中的 $title，这是一条来自“Android界面组件实验”的通知。")
            .setAutoCancel(true)
            .build()
        manager.notify(position + 1, notification)
    }
}
