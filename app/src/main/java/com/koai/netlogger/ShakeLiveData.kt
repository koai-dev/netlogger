package com.koai.netlogger

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import kotlin.math.abs


class ShakeLiveData(
    private val sensorManager: SensorManager? = null
) : LiveData<Boolean>(), SensorEventListener {
    companion object {
        const val SHAKE_THRESHOLD: Float = 15.0f // Shake threshold
        const val SHAKE_TIME_LIMIT: Int = 500 // Time limit in milliseconds to detect shakes
    }

    private val accelerometer: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastShakeTime: Long = 0
    private var lastX = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f

    override fun onActive() {
        super.onActive()
        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        try {
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values?.get(0)?:0f
                val y = event.values?.get(1)?:0f
                val z = event.values?.get(2)?:0f

                val currentTime = event.timestamp
                val diffTime = currentTime - lastShakeTime

                if (diffTime > SHAKE_TIME_LIMIT) { // 500ms timeout for shake
                    val deltaX = abs((lastX - x).toDouble()).toFloat()
                    val deltaY = abs((lastY - y).toDouble()).toFloat()
                    val deltaZ = abs((lastZ - z).toDouble()).toFloat()

                    // If the changes in acceleration exceed the threshold
                    if ((deltaX > SHAKE_THRESHOLD && deltaY > SHAKE_THRESHOLD) ||
                        (deltaX > SHAKE_THRESHOLD && deltaZ > SHAKE_THRESHOLD) ||
                        (deltaY > SHAKE_THRESHOLD && deltaZ > SHAKE_THRESHOLD)
                    ) {
                        // A shake was detected, update LiveData
                        value = true
                    }

                    lastX = x
                    lastY = y
                    lastZ = z
                    lastShakeTime = currentTime
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onInactive() {
        sensorManager?.unregisterListener(this)
        super.onInactive()
    }
}