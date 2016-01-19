package org.james.javafx.shutdown;

import java.util.Calendar;

public class ShutdownByTimeThread implements Runnable {

	private int hour;
	private int minutes;
	private boolean toggle;

	public ShutdownByTimeThread() {
		toggle = true;
	}
	
	public ShutdownByTimeThread(int hour, int minutes) {
		this.hour = hour;
		this.minutes = minutes;
		toggle = true;
	}

	@Override
	public void run() {
		while(toggle){
			Calendar ca = Calendar.getInstance();
			int hourNum = ca.get(Calendar.HOUR_OF_DAY);
			int minutesNum = ca.get(Calendar.MINUTE);
			if(hourNum == hour && minutesNum == minutes){
				System.out.println("shutdown");
				setToggle(false);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public boolean isToggle() {
		return toggle;
	}

	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}

}
