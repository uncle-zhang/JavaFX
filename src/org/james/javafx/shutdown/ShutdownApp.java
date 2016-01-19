package org.james.javafx.shutdown;

import java.util.Map;
import java.util.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ShutdownApp extends Application {
	private GridPane grid;
	private ToggleGroup group;
	private RadioButton shutDownByTimeRbtn;
	private RadioButton shutDownByClockRbtn;
	private Label shutDownByTimeLabel;
	private Label shutDownByClockLabel;
	private TextField textHour1;
	private TextField textMin1;
	private TextField textHour2;
	private TextField textMin2;
	private Button startBtn;
	private Button cancleBtn;
	private HBox startHbox;
	private Label tipsLabel;
	private HBox tipsHbox;
	private Timer shutdownTimer;
	private long distTime = 0;
	private ShutdownByTimeThread sdtt;
	private Timeline timeline;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("定时关机");
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(20);
		grid.setPadding(new Insets(20, 20, 20, 20));

		shutDownByTimeRbtn.setToggleGroup(group);

		textHour1.setMaxWidth(50);
		textHour1.setMinWidth(20);

		textMin1.setMaxWidth(50);
		textMin1.setMinWidth(20);
		grid.add(shutDownByTimeLabel, 0, 0);
		grid.add(shutDownByTimeRbtn, 1, 0);
		grid.add(textHour1, 2, 0);
		grid.add(new Label("时"), 3, 0);
		grid.add(textMin1, 4, 0);
		grid.add(new Label("分"), 5, 0);

		textHour2.setMaxWidth(50);
		textHour2.setMinWidth(20);
		textMin2.setMaxWidth(50);
		textMin2.setMinWidth(20);
		grid.add(shutDownByClockLabel, 0, 1);
		grid.add(shutDownByClockRbtn, 1, 1);
		grid.add(textHour2, 2, 1);
		grid.add(new Label("小时"), 3, 1);
		grid.add(textMin2, 4, 1);
		grid.add(new Label("分钟"), 5, 1);

		startHbox.setAlignment(Pos.CENTER);
		startHbox.getChildren().add(startBtn);
		grid.add(startHbox, 0, 2, 6, 1);

		tipsHbox.setAlignment(Pos.CENTER);
		tipsHbox.getChildren().add(tipsLabel);
		grid.add(tipsHbox, 0, 3, 6, 1);
		
		grid.setGridLinesVisible(false);
		
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (!shutDownByClockRbtn.isSelected() && !shutDownByTimeRbtn.isSelected()) {
					tipsLabel.setText("请选择一个选项!");
					return;
				}
				if (shutDownByTimeRbtn.isSelected()) {
					if (textHour1.getText().isEmpty() || textMin1.getText().isEmpty()) {
						tipsLabel.setText("请填写完整的关机时间!");
						return;
					} else {
						boolean validateResult = validation();
						if (validateResult){
							numberValueHandle(textHour1, textMin1);
							startHbox.getChildren().remove(startBtn);
							startHbox.getChildren().add(cancleBtn);
							disableProcess("all",true);
							sdtt.setToggle(true);
							sdtt.setHour(Integer.valueOf(textHour1.getText()));
							sdtt.setMinutes(Integer.valueOf(textMin1.getText()));
							new Thread(sdtt).start();
							tipsLabel.setText("电脑将于" + textHour1.getText() + ":" + textMin1.getText() + "关闭！" + "取消请点取消按钮！");
						}
					}
				}
				if (shutDownByClockRbtn.isSelected()) {
					if (textHour2.getText().isEmpty() || textMin2.getText().isEmpty()) {
						tipsLabel.setText("请填写完整的时间!");
						return;
					} else {
						boolean validateResult = validation();
						if (validateResult){
							numberValueHandle(textHour2, textMin2);
							startHbox.getChildren().remove(startBtn);
							startHbox.getChildren().add(cancleBtn);
							disableProcess("all",true);
							distTime = Integer.valueOf(textHour2.getText()).intValue()*60*60*1000+
									Integer.valueOf(textMin2.getText()).intValue()*60*1000;
							shutdownTimer.schedule(new ShutDownTask(tipsLabel,distTime), distTime);
							timeline.setCycleCount(Timeline.INDEFINITE);
							timeline.play();
						}
					}
				}
			}

		});

		cancleBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				timeline.stop();
				startHbox.getChildren().remove(cancleBtn);
				startHbox.getChildren().add(startBtn);
				tipsLabel.setText("");
				sdtt.setToggle(false);
				shutdownTimer.cancel();
				enableProcess("auto",true);
			}

		});
		shutDownByTimeRbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				enableProcess("time",false);
				disableProcess("clock",false);
				cleanTextProcess("clock");
			}
		});
		shutDownByClockRbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				enableProcess("clock",false);
				disableProcess("time",false);
				cleanTextProcess("time");
			}
		});

		Scene scene = new Scene(grid, 500, 200);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void doSomething(){
		if(distTime > 0){
			distTime = distTime-1000;
			Map<String,Long> countdownMap = DateTimeUtils.getCountdownMap(distTime);
			tipsLabel.setText("电脑将于"+countdownMap.get("hour")+"小时"+countdownMap.get("minutes")+"分钟"
								+countdownMap.get("seconds")+"秒后关闭。如需取消请点取消按钮。");		
		}
	}

	private boolean validation() {
		int hour = 0;
		int minute = 0;
		try {
			if(shutDownByClockRbtn.isSelected()){
				hour = Integer.valueOf(textHour2.getText());
				minute = Integer.valueOf(textMin2.getText());
			}
			if(shutDownByTimeRbtn.isSelected()){
				hour = Integer.valueOf(textHour1.getText());
				minute = Integer.valueOf(textMin1.getText());
			}
		} catch (NumberFormatException e) {
			tipsLabel.setText("请输入正确的数字！");
			return false;
		}
		if (hour > 24 || hour < 0) {
			tipsLabel.setText("请输入正确的时间值！");
			return false;
		}
		if (minute > 60 || minute < 0) {
			tipsLabel.setText("请输入正确的时间值！");
			return false;
		}
		return true;
	}

	public void numberValueHandle(TextField... textFields) {
		if (textFields.length > 0) {
			for (TextField textField : textFields) {
				int value = Integer.valueOf(textField.getText());
				if (value < 10) {
					textField.setText("0" + value);
				}
			}
		}
	}
	
	private void disableProcess(String code,boolean isIncludeRadioBtn){
		if(code.equalsIgnoreCase("time")){
			textHour1.setDisable(true);
			textMin1.setDisable(true);
			if(isIncludeRadioBtn){
				shutDownByTimeRbtn.setDisable(true);
			}
		}
		if(code.equalsIgnoreCase("clock")){
			textHour2.setDisable(true);
			textMin2.setDisable(true);
			if(isIncludeRadioBtn){
				shutDownByClockRbtn.setDisable(true);
			}
		}
		if(code.equalsIgnoreCase("all")){
			textHour1.setDisable(true);
			textMin1.setDisable(true);
			textHour2.setDisable(true);
			textMin2.setDisable(true);
			if(isIncludeRadioBtn){
				shutDownByTimeRbtn.setDisable(true);
				shutDownByClockRbtn.setDisable(true);
			}
		}
		
	}
	
	private void enableProcess(String code,boolean isIncludeRadioBtn){
		if(code.equalsIgnoreCase("time")){
			textHour1.setDisable(false);
			textMin1.setDisable(false);
			if(isIncludeRadioBtn){
				shutDownByTimeRbtn.setDisable(false);
			}
		}
		if(code.equalsIgnoreCase("clock")){
			textHour2.setDisable(false);
			textMin2.setDisable(false);
			if(isIncludeRadioBtn){
				shutDownByClockRbtn.setDisable(false);
			}
		}
		if(code.equalsIgnoreCase("auto")){
			if(shutDownByTimeRbtn.isSelected()){
				enableProcess("time",isIncludeRadioBtn);
			}
			if(shutDownByClockRbtn.isSelected()){
				enableProcess("clock",isIncludeRadioBtn);
			}
			shutDownByClockRbtn.setDisable(false);
			shutDownByTimeRbtn.setDisable(false);
		}
		if(code.equalsIgnoreCase("all")){
			enableProcess("time",isIncludeRadioBtn);
			enableProcess("clock",isIncludeRadioBtn);
		}
		
	}
	
	private void cleanTextProcess(String code){
		if(code.equalsIgnoreCase("time")){
			textHour1.setText("");
			textMin1.setText("");
		}
		if(code.equalsIgnoreCase("clock")){
			textHour2.setText("");
			textMin2.setText("");
		}
		if(code.equalsIgnoreCase("all")){
			textHour1.setText("");
			textMin1.setText("");
			textHour2.setText("");
			textMin2.setText("");
		}
		
	}

	@Override
	public void init() throws Exception {
		super.init();
		grid = new GridPane();
		group = new ToggleGroup();
		shutDownByTimeRbtn = new RadioButton();
		shutDownByTimeLabel = new Label("按设定时间关机");
		textHour1 = new TextField();
		textMin1 = new TextField();
		shutDownByClockRbtn = new RadioButton();
		shutDownByClockRbtn.setToggleGroup(group);
		shutDownByClockLabel = new Label("规定时间后关机");
		startBtn = new Button("开始");
		cancleBtn = new Button("取消");
		startHbox = new HBox();
		textMin2 = new TextField();
		textHour2 = new TextField();
		tipsLabel = new Label();
		tipsHbox = new HBox();
		textHour2.setDisable(true);
		textMin2.setDisable(true);
		textHour1.setDisable(true);
		textMin1.setDisable(true);
		shutdownTimer = new Timer();
		sdtt = new ShutdownByTimeThread();
		timeline = new Timeline(new KeyFrame(Duration.millis(1000),args -> doSomething()));
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		shutdownTimer.cancel();
		sdtt.setToggle(false);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
