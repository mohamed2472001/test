package com.example.finalprojectpervasivelap

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*


class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btn_add.setOnClickListener {
            // get data
            val time_H = text_H.text.toString()
            val time_M = text_M.text.toString()
            val textSensor = textSensor.text.toString()
            val textSensor2 = textSensor2.text.toString()

            // save var in getSharedPreferences
            val shard = getSharedPreferences("times1", Context.MODE_PRIVATE)
            val edit = shard.edit()


            // check in var in is Not Empty
            if (time_H.isNotEmpty()) {
                edit.putString("time_H", time_H).apply()
            }
            if (time_M.isNotEmpty()) {
                edit.putString("time_M", time_M).apply()
            }
            if (textSensor.isNotEmpty()) {
                edit.putString("Sensor", textSensor).apply()
            }
            if (textSensor2.isNotEmpty()) {
                edit.putString("Sensor2", textSensor2).apply()
                edit.putString("Sensor3", textSensor2).apply()
            }


            startActivity(Intent(this, MainActivity::class.java))


            Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
        }


    }
}