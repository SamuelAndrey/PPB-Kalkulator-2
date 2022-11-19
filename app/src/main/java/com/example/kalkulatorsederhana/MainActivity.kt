package com.example.kalkulatorsederhana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {

    private var OPERATOR: String? = null
    var data: ArrayList<ItemsViewModel> = ArrayList()
    lateinit var courseRV: RecyclerView
    lateinit var courseRVAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // shared preferences
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("histori", "")
        val type: Type = object : TypeToken<ArrayList<ItemsViewModel?>?>() {}.type
        data = gson.fromJson<Any>(json, type) as ArrayList<ItemsViewModel>

        if (data == null) {
            data = ArrayList()
        }

        //  menampilkan recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CustomAdapter(data)
        recyclerView.adapter = adapter

        // perhitungan
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



                val tanda = tanda()
                data.add(ItemsViewModel("$value1 $tanda $value2 = " + calculator(value1, value2)))


                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

                recyclerView.layoutManager = LinearLayoutManager(this)

                val adapter = CustomAdapter(data)

                recyclerView.adapter = adapter

                setupListener()



                // method for saving the data in array list.
                // creating a variable for storing data in
                // shared preferences.
                val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

                // creating a variable for editor to
                // store data in shared preferences.
                val editor = sharedPreferences.edit()

                // creating a new variable for gson.
                val gson = Gson()

                // getting data from gson and storing it in a string.
                val json: String = gson.toJson(data)

                // below line is to save data in shared
                // prefs in the form of string.
                editor.putString("histori", json)

                // below line is to apply changes
                // and save data in shared prefs.
                editor.apply()



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