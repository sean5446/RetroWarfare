/*
 * Sean Maguire
 * see README
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

public class RWGame {
	private ArrayList<RWPlayer> players;
	private ArrayList<int[]> solids;
	private HashMap<int[], String[]> bullets;
	private String map;
	private RWMessage rwMessage;
	
	public RWGame(RWMessage message) {
		players = new ArrayList<RWPlayer>();
		solids = new ArrayList<int[]>();
		map = "10 12 69`~38 5 18`";
		bullets = new HashMap<int[], String[]>();
		rwMessage = message;
	}
	
	public void addPlayer(String avatar) {
		players.add(new RWPlayer(avatar));
	}
	
	public int playerCount() {
		return players.size();
	}
	
	public void updatePlayer(String avatar, String action) {
		RWPlayer p = getPlayer(avatar);
		int posx = p.getX();
		int posy = p.getY();
		
		if (action.equals("u")) {			// up
			if (isValidMove(posx, posy - 1))
				p.setY(--posy);
			p.setLastMove(action);
		} else if (action.equals("l")) {	// left
			if (isValidMove(posx - 1, posy)) 
				p.setX(--posx);
			p.setLastMove(action);
		} else if (action.equals("d")) {	// down
			if (isValidMove(posx, posy + 1)) 
				p.setY(++posy);
			p.setLastMove(action);
		} else if (action.equals("r")) {	// right
			if (isValidMove(posx + 1, posy)) 
				p.setX(++posx);
			p.setLastMove(action);
		} else if (action.equals("s")) {	//shoot
			addBullet(avatar);
		} else if (action.equals("q")) {	// quit
			removePlayer(p);
		}
	}
	
	private RWPlayer getPlayer(String avatar) {
		for (RWPlayer p : players) {
			if (p.getAvatar().equals(avatar)) {
				return p;
			}
		}
		return null;
	}
	
	public boolean playerExists(String avatar) {
		for (RWPlayer p : players) {
			if (p.getAvatar().trim().equals(avatar.trim())) {
				return true;
			}
		}
		return false;
	}
	
	public void removePlayer(RWPlayer p) {
		players.remove(p);
	}
	
	public String getMap() {
		return map;
	}
	
	private boolean isValidMove(int posx, int posy) {
		findSolids(); // find solid positions
		for (int[] p : solids) {
			if (p[0] == posx && p[1] == posy) {
				return false;
			}
		}
		return true;
	}
	
	private void findSolids() {
		String maphorz = map.split("~")[0];
		String mapvert = map.split("~")[1];
		
		solids.clear(); // clear for recalculation
		
		// border
		for (int y = 1; y < 23; y++) {
			solids.add(new int[] {0, y});
			solids.add(new int[] {79, y});
		}
		for (int x = 0; x < 79; x++) {
			solids.add(new int[] {x, 1});
			solids.add(new int[] {x, 23});
		}

		// horizontal lines
		String[] smaphorz = maphorz.split("`");
		for (int i = 0; i < smaphorz.length; i++) {
			int startx = Integer.parseInt(smaphorz[i].split(" ")[0].trim());
			int starty = Integer.parseInt(smaphorz[i].split(" ")[1].trim());
			int length = Integer.parseInt(smaphorz[i].split(" ")[2].trim());
			
			for (int x = startx; x <= length; x++) {
				solids.add(new int[] {x, starty});
			}
		}
		
		// vertical lines
		String[] smapvert = mapvert.split("`");
		for (int i = 0; i < smapvert.length; i++) {
			int startx = Integer.parseInt(smapvert[i].split(" ")[0].trim());
			int starty = Integer.parseInt(smapvert[i].split(" ")[1].trim());
			int length = Integer.parseInt(smapvert[i].split(" ")[2].trim());
		
			for (int y = starty; y <= length; y++) {
				solids.add(new int[] {startx, y});
			}
		}
		
		// players
		for (RWPlayer p : players) {
			solids.add(new int[] {p.getX(), p.getY()});
		}
	}
	
	public String toString() {
		String player = "";
		String bullet = "";
		for (RWPlayer p : players) {
			player += p.toString() + "`";
		}
		for (int[] b : bullets.keySet()) {
			bullet += b[0] + " " + b[1] + "`"; 
		}
		return player + "~" + bullet;
	}
	
	public void addBullet(String avatar) {
		RWPlayer p = getPlayer(avatar);
		int posx = p.getX();
		int posy = p.getY();
		
		if (p.getLastMove().equals("d")) {
			bullets.put(new int[] {posx, posy + 1}, new String[] {avatar, "d"});
		} else if (p.getLastMove().equals("r")) {
			bullets.put(new int[] {posx + 1, posy}, new String[] {avatar, "r"});
		} else if (p.getLastMove().equals("l")) {
			bullets.put(new int[] {posx - 1, posy}, new String[] {avatar, "l"});
		} else if (p.getLastMove().equals("u")) {
			bullets.put(new int[] {posx, posy - 1}, new String[] {avatar, "u"});
		}
	}
	
	public void moveBullets() {
		Object[] posA = bullets.keySet().toArray();
		Object[] bulA = bullets.values().toArray();
		
		for (int i = 0; i < bullets.size(); i++) { // loop thru bullets
			int[] pos = (int[])posA[i];			// get positions
			int posx = pos[0], posy = pos[1];	// find positions
			String[] bul = (String[])bulA[i];   // get info
			String shooter = bul[0];  // find shooter
			String dir = bul[1];      // find direction

			bullets.remove(pos);	// remove bullet
			
			if (dir.equals("d")) {	// update positions
				posy++;
			} else if (dir.equals("r")) {
				posx++;
			} else if (dir.equals("l")) {
				posx--;
			} else if (dir.equals("u")) {
				posy--;
			}
			
			for (RWPlayer p : players) {
				if (p.getX() == posx && p.getY() == posy) {
					p.setScore(p.getScore() - 1);
					RWPlayer shoota = getPlayer(shooter);
					shoota.setScore(shoota.getScore() + 2);
					if (shoota.getScore() >= 20) {	// send map
						for (RWPlayer pl : players) {
							pl.setScore(0);
							rwMessage.put("rwsvm " + pl.getAvatar() + " " + map); 
						}
					}
				}
			}
			
			if (isValidMove(posx, posy)) {
				bullets.put(new int[] {posx, posy}, new String[] {shooter, dir});
			}
		}
		
	}

}



