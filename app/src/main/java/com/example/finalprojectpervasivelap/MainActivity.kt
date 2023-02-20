package com.example.finalprojectpervasivelap

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), SensorEventListener {
    var sensorManager: SensorManager? = null
    var AMSensor: Sensor? = null
    var MFSensor: Sensor? = null
    var rotationValues = FloatArray(9)
    var AMValues = FloatArray(3)
    var MFValues = FloatArray(3)
    var finalValues = FloatArray(3)
    var textView: TextView? = null
    var textView2: TextView? = null
    var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, MyService::class.java))
        textView = findViewById(R.id.infoCompass)
        textView2 = findViewById(R.id.infoQibla)
        imageView = findViewById(R.id.imageCompass)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        AMSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        MFSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (AMSensor != null && MFSensor != null) {
        } else {
            Toast.makeText(this, "Sensor not found!!!", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "mohammed", Toast.LENGTH_SHORT).show()
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            AMValues = p0.values
        } else if (p0.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            MFValues = p0.values
        }

        SensorManager.getRotationMatrix(rotationValues, null, AMValues, MFValues)
        SensorManager.getOrientation(rotationValues, finalValues)

        var newValue = Math.toDegrees(finalValues[0].toDouble()).toInt()

        if (newValue < 0) {
            textView!!.text = "" + (newValue + 360)
            imageView!!.rotation = (newValue * -1).toFloat()
        } else {
            textView!!.text = "" + newValue
            imageView!!.rotation = (newValue * -1).toFloat()
        }

        if (newValue in 121..149) {
            textView2!!.visibility = View.VISIBLE
            textView2!!.text = "إتجاه القبلة الصحيح ✓"

            val v = (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
            // Vibrate for 500 milliseconds
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                v.vibrate(500)
            }

        } else {
            textView2!!.visibility = View.INVISIBLE
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(
            this,
            AMSensor, SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager!!.registerListener(
            this,
            MFSensor, SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                startActivity(Intent(this, Settings::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}