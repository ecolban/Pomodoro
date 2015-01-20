package com.drawmetry.pomodoro;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class PomodoroTimer implements Runnable, ActionListener {

	private Timer ticker = new Timer(1000, this);
	private long endTime;
	private long timeRemaining = 0L;
	private JButton startCancelButton;
	private boolean running;
	private SpinnerModel hourModel = new SpinnerNumberModel(0, 0, 12, 1);
	private SpinnerModel minuteModel = new SpinnerNumberModel(25, 0, 59, 1);
	private SpinnerModel secondModel = new SpinnerNumberModel(0, 0, 59, 1);
	private JTextField timeField;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new PomodoroTimer());
	}

	@Override
	public void run() {
		JFrame frame = new JFrame("Pomodoro");
		frame.setLayout(new BorderLayout());
		JToolBar toolBar = configToolBar();
		frame.add(toolBar, BorderLayout.NORTH);
		configTimeField();
		frame.add(timeField, BorderLayout.CENTER);
		configStartCancelButton();
		frame.add(startCancelButton, BorderLayout.SOUTH);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timeRemaining = endTime - System.currentTimeMillis();
		if (timeRemaining <= 0L) {
			timeRemaining = 0L;
			startCancelButton.setText("START");
			running = false;
			ticker.stop();
			buzz();
		}
		timeField.setText(formatTime(timeRemaining));
	}

	private JToolBar configToolBar() {
		JToolBar toolBar = new JToolBar();
	
		toolBar.add(new JLabel(" H:"));
		JSpinner hourSpinner = new JSpinner(hourModel);
		toolBar.add(hourSpinner);
	
		toolBar.add(new JLabel(" M:"));
		JSpinner minuteSpinner = new JSpinner(minuteModel);
		toolBar.add(minuteSpinner);
	
		toolBar.add(new JLabel(" S:"));
		JSpinner secondSpinner = new JSpinner(secondModel);
		toolBar.add(secondSpinner);
		return toolBar;
	}

	private void configTimeField() {
		timeField = new JTextField("0:00:00");
		timeField.setBackground(Color.RED);
		timeField.setForeground(Color.WHITE);
		timeField.setFont(new Font("Helvetica", Font.PLAIN, 48));
		timeField.setHorizontalAlignment(JTextField.CENTER);
	}

	private void configStartCancelButton() {
		startCancelButton = new JButton("START");
		startCancelButton.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				startCancelButtonClicked();
			}
	
		});
	}

	private void startCancelButtonClicked() {
		if (!running) {
			start();
		} else {
			cancel();
		}
	}

	private void start() {
		long now = System.currentTimeMillis();
		endTime = now + getTime();
		startCancelButton.setText("CANCEL");
		running = true;
		ticker.start();
	}

	private void cancel() {
		ticker.stop();
		timeRemaining = 0L;
		startCancelButton.setText("START");
		running = false;
		timeField.setText(formatTime(timeRemaining));
	}

	private long getTime() {
		long h = ((Integer) hourModel.getValue()).longValue();
		long m = ((Integer) minuteModel.getValue()).longValue();
		long s = ((Integer) secondModel.getValue()).longValue();
		return (((h * 60L) + m) * 60L + s) * 1000L;
	}

	private void buzz() {
		URL url = getClass().getResource("res/shipsbell.wav");
		AudioClip clip = Applet.newAudioClip(url);
		clip.play();
	}

	private String formatTime(long time) {
		long seconds = (time + 500) / 1000; // round to nearest second
		long minutes = seconds / 60;
		long hours = minutes / 60;
		return String.format("%d:%02d:%02d", hours, minutes % 60, seconds % 60);
	}

}
