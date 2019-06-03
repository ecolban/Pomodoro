package com.drawmetry.pomodoro

import java.applet.Applet
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.awt.event.ActionEvent
import javax.swing.*
import java.lang.System.currentTimeMillis as now

class PomodoroTimer : Runnable {

    private var endTime: Long = 0
    private var running: Boolean = false

    private val timeBar = PomodoroToolBar()

    private val timeField: JTextField by lazy {
        val field = JTextField("0:00:00")
        field.background = Color.RED
        field.foreground = Color.WHITE
        field.font = Font("Helvetica", Font.PLAIN, 48)
        field.horizontalAlignment = JTextField.CENTER
        field
    }

    private val startCancelButton: JButton by lazy {
        val button = JButton("START")
        button.addActionListener {
            if (!running) {
                start()
            } else {
                cancel()
            }
        }
        button
    }

    private val ticker: Timer = Timer(1000, ::onTick)


    override fun run() {
        val frame = JFrame("Pomodoro")
        frame.layout = BorderLayout()
        frame.add(timeBar, BorderLayout.NORTH)
        frame.add(timeField, BorderLayout.CENTER)
        frame.add(startCancelButton, BorderLayout.SOUTH)
        frame.pack()
        frame.isResizable = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true

    }

    private fun start() {
        endTime = now() + timeBar.time
        startCancelButton.text = "CANCEL"
        running = true
        ticker.start()
    }

    private fun cancel() {
        ticker.stop()
        startCancelButton.text = "START"
        running = false
        timeField.text = 0L.toTimeString()
    }

    private fun onTick(@Suppress("UNUSED_PARAMETER") e: ActionEvent) {
        val timeRemaining = endTime - now()
        if (timeRemaining > 0L) {
            timeField.text = timeRemaining.toTimeString()
        } else {
            startCancelButton.text = "START"
            running = false
            ticker.stop()
            buzz()
            timeField.text = 0L.toTimeString()
        }
    }

    private fun buzz() {
        val url = javaClass.getResource("res/shipsbell.wav")
        val clip = Applet.newAudioClip(url)
        clip.play()
    }

    private fun Long.toTimeString(): String {
        val seconds = (this + 500L) / 1000L // round to nearest second
        val minutes = seconds / 60L
        val hours = minutes / 60L
        return "%d:%02d:%02d".format(hours, minutes % 60L, seconds % 60L)
    }

}

fun main() {
    SwingUtilities.invokeLater(PomodoroTimer())
}

