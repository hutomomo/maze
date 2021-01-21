package meirogame;
import javafx.scene.image.*;
import javafx.scene.canvas.*;
import javafx.animation.Timeline;
import java.util.*;

public class Actor {
	private Image image;
	protected int posX;
	protected int posY;
	boolean isMovable = false;
	static ArrayList<Actor> actors = new ArrayList<Actor>();
	public Actor(Image image, int posX, int posY) {
		this.image = image;
		this.posX = posX;
		this.posY = posY;
		Actor.actors.add(this);
	}
	static public void paintActors(GraphicsContext gc) {
		synchronized (Actor.actors) {
			for (Actor actor : Actor.actors) {
				gc.drawImage(actor.image, actor.posX * Game.TILESIZE, actor.posY * Game.TILESIZE);
			}
		}
	}

	public boolean moves(int x, int y) {
		if(existsAt(posX+x,posY+y)==null) {

			posX+=x;
			posY+=y;
			return true;
		}else {
			if(existsAt(posX+x,posY+y) instanceof hasigo) {	
					
					clear();
			}

			//			isMovable = true;
			//			pushed(x, y);
			//			if(pushed(x,y)==false) {
			//				return true;
			//			}
			//			posX+=x;
			//			posY+=y;
			return true;
		}
	}
	public static void clear(){
		Actor.actors.clear();
		Game.game.timer.stop();
		//TODO: これを動かしたい
		Game.game.gc.drawImage(new Image(Game.game.getClass().getResourceAsStream("clear.png"), 640, 640, true, false), 0 * Game.game.TILESIZE , 0 * Game.game.TILESIZE);
		Game.game.gc.restore();
	}

	//return false;


	//	public boolean pushed(int x ,int y) {
	//		if(this.isMovable) {
	//			synchronized(Actor.actors){
	//				if(existsAt(this.posX+x+x,this.posY)!=null && existsAt(this.posX,this.posY+y+y)!=null) {
	//					return false;
	//				}
	//				existsAt(posX+x,posY).posX += x;
	//				existsAt(posX,posY+y).posY += y;
	//			}
	//		}
	//		return true;
	//	}

	static public Actor existsAt(int x, int y) {
		for (Actor actor : Actor.actors) {
			if(actor.posX ==x&&actor.posY ==y) {
				return actor;
			}

		}

		return null;
	}
}
