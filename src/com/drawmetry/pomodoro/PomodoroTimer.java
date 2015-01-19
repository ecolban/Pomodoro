package com.drawmetry.pomodoro;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class PomodoroTimer extends JPanel implements Runnable, ActionListener {

	private javax.swing.Timer ticker = new javax.swing.Timer(1000, this);
	private long endTime;
	private long timeRemaining = 0L;
	private Font timeFont = new Font("Helvetica", Font.PLAIN, 48);
	private JButton startCancelButton;
	private boolean running;
	private SpinnerModel hourModel = new SpinnerNumberModel(0, 0, 12, 1);
	private SpinnerModel minuteModel = new SpinnerNumberModel(25, 0, 59, 1);
	private SpinnerModel secondModel = new SpinnerNumberModel(0, 0, 59, 1);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new PomodoroTimer());
	}

	@Override
	public void run() {
		JFrame frame = new JFrame("Pomodoro");
		frame.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(200, 100));
		frame.add(this, BorderLayout.CENTER);
		// tool bar
		JToolBar toolBar = new JToolBar();

		toolBar.add(new JLabel("H:"));
		JSpinner hourSpinner = new JSpinner(hourModel);
		toolBar.add(hourSpinner);

		toolBar.add(new JLabel("M:"));
		JSpinner minuteSpinner = new JSpinner(minuteModel);
		toolBar.add(minuteSpinner);

		toolBar.add(new JLabel("S:"));
		JSpinner secondSpinner = new JSpinner(secondModel);
		toolBar.add(secondSpinner);

		frame.add(toolBar, BorderLayout.NORTH);
		// start/cancel button
		startCancelButton = new JButton("START");
		startCancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startCancelButtonClicked();
			}

		});
		frame.add(startCancelButton, BorderLayout.SOUTH);
		// usual stuff
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	@Override
	public void paintComponent(Graphics g) {
		g.setFont(timeFont);
		g.setColor(Color.RED);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.drawString(formatTime(timeRemaining), 20, 70);
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
		repaint();
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
		repaint();
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
