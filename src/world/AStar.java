package world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStar {

	public static double lastTime = System.currentTimeMillis();
	private static Comparator <Node> nodeSorter = new Comparator <Node>() {
		
		@Override
		public int compare(Node n1, Node n2) {
			if(n2.fCost < n1.fCost) 
				return + 1;
			
			if(n2.fCost > n1.fCost) 
				return - 1;
			
			return 0;
		}
	};
	
	public static boolean clear() {
		if(System.currentTimeMillis() - lastTime >= 1000)
			return true;
		
		return false;
	}
	
	public static List<Node> findPath(Vector2i start, Vector2i end) {
		lastTime = System.currentTimeMillis();
		List<Node> openList = new ArrayList<>();
		List<Node> closedList = new ArrayList<>();
		
		Node current = new Node(start, null, 0, calcDistance(start, end));
		openList.add(current);
		
		while(!openList.isEmpty()) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if(current.tile.equals(end)) {
				List<Node> path = new ArrayList<>();
				while(current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				//Collections.reverse(path);
				return path;
			}
			
			openList.remove(current);
			closedList.add(current);
			
			for(int i = 0; i < 9; i++) {
				if(i == 4) continue;
				int x = current.tile.x;
				int y = current.tile.y;
				int xi = (i%3) - 1;
				int yi = (i/3) - 1;
				
				Tile tile = World.tiles[x+xi+((y+yi)*World.WIDTH)];
				
				if(tile == null) continue;
				if(tile instanceof Wall) continue;
				if(i == 0 || i == 2 || i == 6 || i == 8) {
					Tile test1 = World.tiles[x+xi+((y)*World.WIDTH)];
					Tile test2 = World.tiles[x+((y+yi)*World.WIDTH)];
					if(test1 instanceof Wall || test2 instanceof Wall)
						continue;
				}
				
				Vector2i a = new Vector2i(x+xi, y+yi);
				double gCost = current.gCost + calcDistance(current.tile, a);
				double hCost = calcDistance(a, end);
				Node node = new Node(a, current, gCost, hCost);
				
				if(vecInList(closedList, a)) continue;
				
				if(!vecInList(openList, a)) 
					openList.add(node);
				
				Node existing = getNodeFromList(openList, a);
				if(existing == null) {
					openList.add(node);
				} else if(gCost < existing.gCost) {
					openList.remove(existing);
					openList.add(node);
				}
			}
		}
		closedList.clear();
		return Collections.emptyList();
	}
	
	private static double calcDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.x - goal.x;
		double dy = tile.y - goal.y;
		
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}
	
	private static boolean vecInList(List<Node> list, Vector2i pos) {
	    for (Node node : list) {
	        if (node.tile.equals(pos))
	            return true;
	    }
	    return false;
	}
	
	private static Node getNodeFromList(List<Node> list, Vector2i position) {
		for (Node node : list) {
			if (node.tile.equals(position))
				return node;
		}
		return null;
	}
	
}
