import java.util.Random;

/*
 * Sean Maguire
 * see README
 * 
 */


public class RWPlayer {
	private int posx;
	private int posy;
	private int score;
	private String avatar;
	private String lastMove;

	public RWPlayer(String avatar) {
		this.avatar = avatar;
		score = 0;
		lastMove = "d";
		java.util.Random rand = new java.util.Random();
		int min = 1, max = 10; // range should be changed if map changes
		posx = rand.nextInt(max - min + 1) + min;
		posy = rand.nextInt(max - min + 1) + min;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void setLastMove(String lastmove) {
		this.lastMove = lastmove;
	}
	
	public String getLastMove() {
		return lastMove;
	}
	
	public int getX() {
		return posx;
	}

	public void setX(int x) {
		posx = x;
	}

	public int getY() {
		return posy;
	}

	public void setY(int y) {
		posy = y;
	}
	
	public String toString() {
		return avatar + " " + posx + " " + posy + " " + score;
	}
	
}


