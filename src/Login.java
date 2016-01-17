import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.geometry.HPos.RIGHT;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login extends Application {

	@Override
    public void start(Stage primaryStage) {
		primaryStage.setTitle("JavaFX Welcome");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(20, 20, 20, 20));
        
        final ToggleGroup group = new ToggleGroup();
        RadioButton shutDownByTimeRbtn = new RadioButton();
        shutDownByTimeRbtn.setToggleGroup(group);
        Label shutDownByTimeLabel = new Label("按设定时间关机");
        Label seperator = new Label("：");
        seperator.setFont(new Font(15));
        TextField textHour1 = new TextField();
        textHour1.setMaxWidth(50);
        textHour1.setMinWidth(20);
        TextField textMin1 = new TextField();
        textMin1.setMaxWidth(50);
        textMin1.setMinWidth(20);
        grid.add(shutDownByTimeLabel, 0, 0);
        grid.add(shutDownByTimeRbtn, 1, 0);
        grid.add(textHour1, 2, 0);
        grid.add(seperator, 3, 0);
        grid.add(textMin1, 4, 0);
        
        RadioButton shutDownByClockRbtn = new RadioButton();
        shutDownByClockRbtn.setToggleGroup(group);
        Label shutDownByClockLabel = new Label("规定时间后关机");
        Label seperator1 = new Label("：");
        seperator.setFont(new Font(15));
        TextField textHour2 = new TextField();
        textHour2.setMaxWidth(50);
        textHour2.setMinWidth(20);
        TextField textMin2 = new TextField();
        textMin2.setMaxWidth(50);
        textMin2.setMinWidth(20);
        grid.add(shutDownByClockLabel, 0, 1);
        grid.add(shutDownByClockRbtn, 1, 1);
        grid.add(textHour2, 2, 1);
        grid.add(seperator1, 3, 1);
        grid.add(textMin2, 4, 1);
        
        Button startBtn = new Button("开始");
        Button cancleBtn = new Button("取消");;
        HBox startHbox = new HBox();
        startHbox.setAlignment(Pos.CENTER);
        startHbox.getChildren().add(startBtn);
//        startHbox.getChildren().add(cancleBtn);
        grid.add(startHbox, 0, 2, 5, 1);
        
        Label tipsLabel = new Label();
        HBox tipsHbox = new HBox();
        tipsHbox.setAlignment(Pos.CENTER);
        tipsHbox.getChildren().add(tipsLabel);
        grid.add(tipsHbox, 0, 3, 5, 1);
        
        grid.setGridLinesVisible(false);
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(!shutDownByClockRbtn.isSelected() && !shutDownByTimeRbtn.isSelected()){
					tipsLabel.setText("请选择一个选项！");
					return;
				}
				if(shutDownByTimeRbtn.isSelected()){
					if(textHour1.getText().isEmpty() || textMin1.getText().isEmpty()){
						tipsLabel.setText("请填写完整的关机时间！");
						return;
					}else{
						boolean valiResult = validation(textHour1.getText(), "hour", tipsLabel);
						if(valiResult){
							valiResult= validation(textMin1.getText(), "min", tipsLabel);
						}
						if(valiResult){
							numberValueHandle(textHour1,textMin1);
							startHbox.getChildren().remove(startBtn);
							startHbox.getChildren().add(cancleBtn);
							tipsLabel.setText("电脑将于"+textHour1.getText()+":"+textMin1.getText()+"关闭！"+"取消请点取消按钮！");
						}
					}
				}
			}
			
		});
		
		cancleBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				startHbox.getChildren().remove(cancleBtn);
				startHbox.getChildren().add(startBtn);
				tipsLabel.setText("");
			}
			
		});
		

        Scene scene = new Scene(grid,400,200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

	public static void main(String[] args) {
		launch(args);
	}
	
	public boolean validation(String valueStr,String code,Label tipsLabe){
		int value = 0;
		try{
			value = Integer.valueOf(valueStr);
		}catch(NumberFormatException e){
			tipsLabe.setText("请输入正确的数字！");
			return false;
		}
		if(code.equalsIgnoreCase("hour")){
			if(value>24 || value<0){
				tipsLabe.setText("请输入正确的时间值！");
				return false;
			}
		}
		if(code.equalsIgnoreCase("min")){
			if(value>60 || value<0){
				tipsLabe.setText("请输入正确的时间值！");
				return false;
			}
		}
		if(code.equalsIgnoreCase("clock")){
			if(value>24 || value<0){
				tipsLabe.setText("请输入正确的时间值！");
				return false;
			}
		}
		return true;
	}
	
	public void numberValueHandle(TextField ...textFields){
		if(textFields.length > 0){
			for(TextField textField:textFields){
				int value = Integer.valueOf(textField.getText());
				if(value < 10){
					textField.setText("0"+value);
				}
			}
		}
	}

}
