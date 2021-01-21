package meirogame; 
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration; 
public class Game extends Application {
	static final int TILESIZE = 32;
	static final int NUMTILE_X = 20;
	static final int NUMTILE_Y = 20;
	static final int PANEL_W = TILESIZE * NUMTILE_X;
	static final int PANEL_H = TILESIZE * NUMTILE_Y;
	static int width = 55 ;
	static int height = 55 ;
	static int [][] maze = new int [width][height];
	static Game game;
	Actor player;
	Actor hasigo;
	Stage stage;
	int baseX;
	int baseY;
	private Canvas canvas;
	public GraphicsContext gc;
	long startTime;
	Timeline timer;
	public Game() {

	}
	public static void create(){
		Random random = new Random();
		int randomValue = random.nextInt(4);
		for(int y=0;y<height;y++) {
			for(int x = 0;x < width; x++){
				maze[x][y] = 0;
			}
		}
		for(int y=0;y<height;y++) {
			for(int x = 0;x < width; x++){
				if ( x== 0 || y == 0 || x == width - 1 || y == height - 1){
					maze[x][y] = 1;
				}else{
					if (x % 2 == 0 && y % 2 == 0){
						maze[x][y]=1;
						while(true) {
							randomValue = random.nextInt(4);
							if (randomValue == 0&&maze[x][y - 1] == 0){
								maze[x][y - 1] = 1;
								break;
							}else if(randomValue == 1&&maze[x + 1][y] == 0){
								maze[x + 1][y] = 1;
								break;
							}else if(randomValue == 2&&maze[x][y + 1] == 0){
								maze[x][y + 1] = 1;
								break;
							}else if(randomValue == 3&&maze[x - 1][y] == 0){
								maze[x - 1][y] = 1;
								break;
							}
						}
					}
				}
			}
		}
	} 
	public static void main(String[] args) {
		create();
		launch(args);
	}
	public void start(Stage primaryStage) throws Exception {
		Game.game = this;
		Pane root = new Pane();
		this.canvas = new Canvas(Game.PANEL_W, Game.PANEL_H);
		root.getChildren().add(this.canvas);
		this.draw();
		Scene scene = new Scene(root);
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case UP: // javafx.scene.input.KeyCodeの定数
				if(this.player.posY == 0-baseY ) {
					baseY++;
					break;
				}
				this.player.moves(0, -1);
				break;
			case DOWN:
				if(this.player.posY == (NUMTILE_Y-1)-baseY ) {
					baseY--;
					break;
				}
				this.player.moves(0, 1);
				break;
			case LEFT:
				if(this.player.posX == 0-baseX ) {
					baseX++;
					break;
				}
				this.player.moves(-1, 0);
				break;
			case RIGHT:
				if(this.player.posX == (NUMTILE_X-1)-baseX ) {
					baseX--;
					break;
				}
				this.player.moves(1,0);
				break;
			case SPACE:
				System.out.println("わー");
				break;
			default:
				break;
			} // プレイヤーが移動するたびに終了条件を判定 
			this.draw();
		});
		this.timer = new Timeline(new KeyFrame(Duration.millis(999), e -> {
			long sec = (System.currentTimeMillis() - Game.game.startTime) / 1000;
			System.out.println(sec); // コンソールに経過秒を出力(デバッグ用)
			if (sec >= 100) { // 時間が60秒経過したらタイマーを止め終了判定
				this.timer.stop();
				this.call();

			}
		}));
		this.timer.setCycleCount(Timeline.INDEFINITE);

		primaryStage.setTitle("NE30-0015 野本優輝"); 
		primaryStage.setScene(scene);
		primaryStage.show();
		Game.game.startTime = System.currentTimeMillis();
		this.timer.play();
	}
	synchronized void draw() {
		if (this.canvas != null) {
			gc = this.canvas.getGraphicsContext2D();
			gc.clearRect(0, 0, PANEL_W, PANEL_H);
			//			gc.setFill(Color.SADDLEBROWN);
			//			gc.fillArc(NUMTILE_X, NUMTILE_Y, NUMTILE_X, NUMTILE_Y, 0, 0, null);
			//			gc.setStroke(Color.BLACK);
			for (int i = 0; i < height/8; i++) {
				//								gc.strokeLine(i* TILESIZE  , 0, i * TILESIZE, TILESIZE * NUMTILE_Y );
				for(int k=0;k<width/8;k++) {
					gc.drawImage(new Image(this.getClass().getResourceAsStream("yuka2.png"), 256, 256, true, false), i * TILESIZE*8, k * TILESIZE*8);
				}
			}
			for (int i = 0; i < NUMTILE_Y * 2; i++) {
				gc.strokeLine(0, i * TILESIZE /2, TILESIZE * NUMTILE_X*2, i * TILESIZE/2);
			}

			gc.save();
			gc.translate(this.baseX * TILESIZE, this.baseY * TILESIZE); 
			Actor.paintActors(gc); // キャラクターの描画
			gc.restore();
		}
	}
	public void init() throws Exception {
		synchronized (Actor.actors) {
			for(int i=0; i < height; i++){
				for(int k=0; k < width; k++) {
					if(maze[k][i]==1){
						new Actor(new Image(this.getClass().getResourceAsStream("iwa.png"),32,32,true,false),i,k);
					}
				}
			}
			new hasigo(new Image(this.getClass().getResourceAsStream("hasigo.png"),32,32,true,false),width-2 ,height-2);
			this.player = new Actor(
					new Image(this.getClass().getResourceAsStream("nomotoyuuki.png"),32,32,true,true), 1, 1);
		}
	}
	public static void call() {
		Actor.clear();
		Game.game.gc.drawImage(new Image(Game.game.getClass().getResourceAsStream("gameover.png"), 640, 640, true, false), 0* TILESIZE, 0 * TILESIZE);
		Game.game.gc.restore();

		System.out.println("Game-over");
	}
	public void goal() {
		Actor.clear();
	}
	public void stop() throws Exception {
		Character.killAll();
	}
}

