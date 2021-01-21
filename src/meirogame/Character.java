package meirogame;
import javafx.scene.image.Image;
public class Character extends Actor {
	private long speed;
	public boolean alive = true;
	public Character(Image image, int posX, int posY) {
		super(image, posX, posY);
		this.speed = speed;
		this.start();
	}
	public void start() {
		new Thread(() -> {
			while (this.alive) {
				try {
					Thread.sleep(this.speed);
					//Thread.sleep(this.speed); // 一定時間止まる 
				} catch (InterruptedException e) { }

				int x = 0, y = 0;
				if ((int)(Math.random() * 2) == 0) { 	// 乱数が0ならX軸方向、1ならY軸報告に移動
					x = (int)(Math.random() * 3) - 1; 	// -1, 0, 1
				} else {
					y = (int)(Math.random() * 3) - 1; 	// -1, 0, 1
				}
				if (this.moves(x, y)) {
					Game.game.draw();					// Characterが動いたら、Canvasを再描画 
				}
			}
		}).start();
	}
	public static void killAll() {
		synchronized (Actor.actors) {
			for (Actor actor : Actor.actors) {
				if (actor instanceof Character) {
					((Character) actor).alive = false;
				}
			}
		}
	}
}
