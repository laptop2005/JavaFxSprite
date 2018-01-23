package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	
	//화면 넓이
	private static final int PREF_W = 800;
	//화면 높이
    private static final int PREF_H = 640;
    
    //캐릭터 최초 위치x
    private int spriteX = 400;
    //캐릭터 최초 위치y
    private int spriteY = 320;
	
    //화면 구성 객체들
    private Pane root = new Pane();
    private Canvas canvas = new Canvas(PREF_W,PREF_H);
    private GraphicsContext gc;
    private Scene scene;
    private CharacterSprite sprite;
    
    //사용자가 누른 방향키
    private KeyCode direction;
    
	@Override
	public void start(Stage primaryStage) {
		try {
			
			scene = new Scene(root,PREF_W,PREF_H);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			setCanvas();
			setEvent();
			
			Platform.runLater(()->{
				Thread thread = new Thread(()->{
					int duration = 0;
					while(duration<100000){
						int ran = (int)(Math.random()*4);
						int length = (int)(Math.random()*25);
						KeyCode code = null;
						switch(ran){
							case 0:
								code = KeyCode.UP; 
								break;
							case 1:
								code = KeyCode.DOWN; 
								break;
							case 2:
								code = KeyCode.LEFT; 
								break;
							case 3:
								code = KeyCode.RIGHT; 
								break;
						}
						for(int i=0;i<length;i++){
							characterMove(code);
							try {
								Thread.sleep(100);
								duration += 100;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						characterStop();
					}
				});
				thread.start();
			});
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//캐릭터가 움직일 공간인 캔버스와 캐릭터 움직임을 표시할 스프라이트(이미지) 설정
	private void setCanvas(){
		root.getChildren().add(canvas);
		
		gc = canvas.getGraphicsContext2D();
		try {
			sprite = new CharacterSprite(KeyCode.RIGHT, spriteX, spriteY, PREF_W, PREF_H);
			sprite.draw(gc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//방향키를 눌렀을때의 이벤트 설정
	private void setEvent(){
		//키를 눌렀을때
		scene.setOnKeyPressed(event->{
			gc.clearRect(0, 0, PREF_W, PREF_H);
			direction = event.getCode();
			sprite.setDirection(direction);
			sprite.setMoving(true);
			sprite.tick();
			sprite.draw(gc);
		});
		
		//키를 떼었을때
		scene.setOnKeyReleased(event->{
			gc.clearRect(0, 0, PREF_W, PREF_H);
			sprite.setMoving(false);
			sprite.tick();
			sprite.draw(gc);
		});
	}
	
	private void characterMove(KeyCode code){
		gc.clearRect(0, 0, PREF_W, PREF_H);
		direction = code;
		sprite.setDirection(direction);
		sprite.setMoving(true);
		sprite.tick();
		sprite.draw(gc);
	}
	
	private void characterStop(){
		gc.clearRect(0, 0, PREF_W, PREF_H);
		sprite.setMoving(false);
		sprite.tick();
		sprite.draw(gc);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
