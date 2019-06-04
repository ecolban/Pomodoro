package com.drawmetry.pomodoro

import java.awt.Font
import javax.swing.JLabel
import javax.swing.JSpinner
import javax.swing.JToolBar
import javax.swing.SpinnerNumberModel

class PomodoroToolBar : JToolBar() {

    private val hourModel: SpinnerNumberModel = SpinnerNumberModel(0, 0, 12, 1)
    private val minuteModel: SpinnerNumberModel = SpinnerNumberModel(25, 0, 59, 1)
    private val secondModel: SpinnerNumberModel = SpinnerNumberModel(0, 0, 59, 1)

    init {
        val hourLabel = JLabel(" H:")
        val font = Font("Helvetica", Font.PLAIN, 18)
        hourLabel.font = font
        add(hourLabel)
        val hourSpinner = JSpinner(hourModel)
        hourSpinner.font = font
        add(hourSpinner)

        val minuteLabel = JLabel(" M:")
        minuteLabel.font = font
        add(minuteLabel)
        val minuteSpinner = JSpinner(minuteModel)
        minuteSpinner.font = font
        add(minuteSpinner)

        val secondLabel = JLabel(" S:")
        secondLabel.font = font
        add(secondLabel)
        val secondSpinner = JSpinner(secondModel)
        secondSpinner.font = font
        add(secondSpinner)

        isFloatable = false
    }

    val time: Long
        get() {
            val h: Int = hourModel.value as Int
            val m: Int = minuteModel.value as Int
            val s: Int = secondModel.value as Int
            return (((h * 60 + m) * 60 + s) * 1000).toLong()
        }
}