package com.drawmetry.pomodoro

import javax.swing.JLabel
import javax.swing.JSpinner
import javax.swing.JToolBar
import javax.swing.SpinnerNumberModel

class PomodoroToolBar : JToolBar() {

    private val hourModel: SpinnerNumberModel = SpinnerNumberModel(0, 0, 12, 1)
    private val minuteModel: SpinnerNumberModel = SpinnerNumberModel(25, 0, 59, 1)
    private val secondModel: SpinnerNumberModel = SpinnerNumberModel(0, 0, 59, 1)

    init {
        add(JLabel(" H:"))
        val hourSpinner = JSpinner(hourModel)
        add(hourSpinner)

        add(JLabel(" M:"))
        val minuteSpinner = JSpinner(minuteModel)
        add(minuteSpinner)

        add(JLabel(" S:"))
        val secondSpinner = JSpinner(secondModel)
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