package org.james.javafx.shutdown;

import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;

import javafx.scene.control.Label;

public class ShutDownTask extends TimerTask {
	
	private Label tipsLabel;
	private long distTime;
	
	public ShutDownTask(Label tipsLabel,long distTime) {
		this.tipsLabel = tipsLabel;
		this.distTime = distTime;
	}

	@Override
	public void run() {
		
	}
	

}
