package com.example.kalkulatorsederhana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var OPERATOR: String? = null
    private lateinit var cadp : CustomAdapter
    private lateinit var ivm : ArrayList<ItemsViewModel>
    val data = ArrayList<ItemsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListener()
    }

    private fun validate(): Boolean {
        if (edit_value1.text.isNullOrEmpty() || edit_value2.text.isNullOrEmpty()) {
            return false
        } else if(OPERATOR.isNullOrEmpty()) {
            return false
        }
        return true
    }

    private fun calculator(value1: Int, value2: Int): String {
        var result: Int = 0
        when(OPERATOR) {
            "Tambah" -> result = value1 + value2
            "Kurang" -> result = value1 - value2
            "Bagi" -> result = value1 / value2
            "Kali" -> result = value1 * value2
        }
        return result.toString()
    }

    private fun tanda(): String {
        var result: String = ""
        when(OPERATOR) {
            "Tambah" -> result = " + "
            "Kurang" -> result = " - "
            "Bagi" -> result = " : "
            "Kali" -> result = " x "
        }
        return result
    }

    private fun setupListener() {
        button_calculator.setOnClickListener {

            if(validate()) {
                val value1 = edit_value1.text.toString().toInt()
                val value2 = edit_value2.text.toString().toInt()

                text_result.text = calculator(value1, value2)

                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

                recyclerView.layoutManager = LinearLayoutManager(this)



                val tanda = tanda()
                data.add(ItemsViewModel("$value1 $tanda $value2 = " + calculator(value1, value2)))
                // cadp.notifyDataSetChanged()

                val adapter = CustomAdapter(data)

                recyclerView.adapter = adapter

            } else {
                showMessage("Masukkan data dengan benar")
            }
        }

        radio_operator.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(group.checkedRadioButtonId)
            OPERATOR = radioButton.text.toString()
            text_result.text = "HASIL"
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}