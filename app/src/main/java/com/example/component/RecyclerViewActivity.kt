package com.example.component

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewActivity : Activity() {

    private data class Animal(val name: String, val imageRes: Int)

    private val data = mutableListOf(
        Animal("Lion", R.drawable.lion),
        Animal("Tiger", R.drawable.tiger),
        Animal("Monkey", R.drawable.monkey),
        Animal("Dog", R.drawable.dog),
        Animal("Cat", R.drawable.cat),
        Animal("Elephant", R.drawable.elephant)
    )

    private val extraImages = intArrayOf(
        R.drawable.lion,
        R.drawable.tiger,
        R.drawable.monkey,
        R.drawable.dog,
        R.drawable.cat,
        R.drawable.elephant
    )
    private var extraCount = 0

    /** 根据名字匹配对应动物图片；匹配不到时按顺序循环分配一张 */
    private fun animalImageFor(name: String): Int {
        val names = arrayOf("Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant")
        val idx = names.indexOfFirst { it.equals(name, ignoreCase = true) }
        return if (idx >= 0) {
            extraImages[idx]
        } else {
            extraImages[extraCount++ % extraImages.size]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_animals)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = AnimalAdapter(data)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.btn_add).setOnClickListener {
            val et = findViewById<EditText>(R.id.et_new_item)
            val name = et.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "请输入要添加的名称", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val image = animalImageFor(name)
            data.add(Animal(name, image))
            // 增加记录后立即更新列表
            adapter.notifyItemInserted(data.size - 1)
            et.text.clear()
            Toast.makeText(this, "已添加 $name，列表已更新", Toast.LENGTH_SHORT).show()
        }
    }

    private class AnimalAdapter(private val items: MutableList<Animal>) :
        RecyclerView.Adapter<AnimalAdapter.VH>() {

        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val image: ImageView = itemView.findViewById(R.id.rv_image)
            val name: TextView = itemView.findViewById(R.id.rv_name)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
            return VH(view)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val animal = items[position]
            holder.image.setImageResource(animal.imageRes)
            holder.name.text = animal.name
            // 单击只显示名字的 Toast（不带系统应用图标）
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val view = LayoutInflater.from(context).inflate(R.layout.toast_name, null)
                view.findViewById<TextView>(R.id.toast_name_text).text = animal.name
                val toast = Toast(context)
                toast.duration = Toast.LENGTH_SHORT
                toast.view = view
                toast.show()
            }
        }

        override fun getItemCount(): Int = items.size
    }
}
