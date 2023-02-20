package com.example.finalprojectpervasivelap

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.util.*

class MyService : Service(), SensorEventListener {
    var sensorManager: SensorManager? = null
    var s: Sensor? = null
    var SensorPROXIMITY: Sensor? = null
    var sA: Sensor? = null

    var mediaPlayerM: MediaPlayer? = null
    var mediaPlayerN: MediaPlayer? = null

    var check: Boolean = true


    @SuppressLint("ResourceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // get Sensor
        s = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        sA = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        SensorPROXIMITY = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (s == null) {
            Toast.makeText(this, "Sensor is not found!!!", Toast.LENGTH_SHORT).show()
        }

        sensorManager!!.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager!!.registerListener(this, sA, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager!!.registerListener(this, SensorPROXIMITY, SensorManager.SENSOR_DELAY_NORMAL)

        // get MediaPlayer
        mediaPlayerM = MediaPlayer.create(this, R.raw.sbah)
        mediaPlayerN = MediaPlayer.create(this, R.raw.masa)

        return START_STICKY
    }

    override fun onDestroy() {

        super.onDestroy()
        sensorManager!!.unregisterListener(this)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    @SuppressLint("CommitPrefEdits")
    override fun onSensorChanged(sensorEvent: SensorEvent) {


        if (sensorEvent.sensor.type == Sensor.TYPE_LIGHT) {
            val valLight = sensorEvent.values[0].toInt()


            // save var in Shared Preferences
            val shard = getSharedPreferences("times1", Context.MODE_PRIVATE)

            val varSensor = shard.getString("Sensor", "0").toString()
            val Sensor2 = shard.getString("Sensor2", "0").toString()

            try {
                if (check) {
                    // Sensor LIGHT
                    if (varSensor.toInt() > 0) {
                        // Turning on the morning Azkar when the intensity of the lighting in the place is higher than varSensor
                        if (valLight > varSensor.toInt()) {
                            if (!mediaPlayerM!!.isPlaying) {
                                mediaPlayerM!!.start()
                            }
                            if (mediaPlayerN!!.isPlaying) {
                                mediaPlayerN!!.pause()
                                mediaPlayerN!!.seekTo(0)
                            }

                        }
                    }
                    // Turning on the evening Azkar when the lighting intensity in the place is less than varSensor.
                    else if (valLight < Sensor2.toInt()) {
                        if (!mediaPlayerN!!.isPlaying) {
                            mediaPlayerN!!.start()
                        }
                        if (mediaPlayerM!!.isPlaying) {
                            mediaPlayerM!!.pause()
                            mediaPlayerM!!.seekTo(0)
                        }
                    }

                    //evening Azkar by choosing a working hour


                }


            } catch (e: Exception) {
                Toast.makeText(this, "Error $e", Toast.LENGTH_SHORT).show()
            }

        }


        // get time in phone
        val HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val MINUTE = Calendar.getInstance().get(Calendar.MINUTE)
        val Sc = Calendar.getInstance().get(Calendar.SECOND)

        // save var in Shared Preferences
        val shard = getSharedPreferences("times1", Context.MODE_PRIVATE)
        val time_H = shard.getString("time_H", "0")
        val time_M = shard.getString("time_M", "0")

        if (time_H.equals("$HOUR") && time_M.equals("$MINUTE")) {

            if (time_H!!.toInt() > 12) {
                if (!mediaPlayerN!!.isPlaying) {
                    mediaPlayerN!!.start()
                }
                if (mediaPlayerM!!.isPlaying) {
                    mediaPlayerM!!.pause()
                    mediaPlayerM!!.seekTo(0)
                }
            } else {
                if (!mediaPlayerM!!.isPlaying) {
                    mediaPlayerM!!.start()
                }
                if (mediaPlayerN!!.isPlaying) {
                    mediaPlayerN!!.pause()
                    mediaPlayerN!!.seekTo(0)
                }
            }

        }

        // Sensor PROXIMITY

        if (sensorEvent.sensor.type == Sensor.TYPE_PROXIMITY) {
            val value = sensorEvent.values[0].toInt()
            //proximity sensor to temporarily stop the Azkar
            if (value < 2) {
                mediaPlayerM!!.pause()
                mediaPlayerN!!.pause()
            }
        }


        // Sensor ACCELEROMETER
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val y = sensorEvent.values[1].toInt()
            val z = sensorEvent.values[2].toInt()

            // When the phone rotates on the y-axis, the Azkar will be stopped.
            if (y == 0) {
                mediaPlayerM!!.pause()
                mediaPlayerN!!.pause()
            }
            // When the phone rotates on the z-axis check = true Stop Sensor PROXIMITY

            check = z >= 0

        }


    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

}