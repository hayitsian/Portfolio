import java.util.LinkedList;
import java.util.List;

public class Map {
	private int width;
	private int height;
	private Class[][] classes;
	public Map(int[][] map) {
		height = map.length;
		width = map[0].length;
		classes = new Class[width][height];
		for(int k=0;k<width;k++) {
			for(int i=0;i<height;i++) {
				if(map[i][k] == -1 || map[i][k] > 10) {
					classes[i][k] = new Class(k,i,true);
				}
				else {
					classes[i][k] = new Class(k,i,false);
				}
			}
		}
	}
	public Class getClass(int x, int y) {
		if(x >= 0 && x < width && y >= 0 && y < height) {
			return classes[x][y];
		}
		else {
			return null;
		}
	}
	
	public Class lowestFInList(List<Class> list) {
		Class cheapest = list.get(0);
		for(int k=0;k<list.size();k++) {
			if(list.get(k).getF() < cheapest.getF()) {
				cheapest = list.get(k);
			}
		}
		return cheapest;
	}
	
	public List<Class> calcPath(Class start, Class goal) {
		LinkedList<Class> path = new LinkedList<Class>();
		Class node = goal;
		boolean done = false;
		while(!done) {
			path.addFirst(node);
			node = node.getParent();
			if(node.equals(start)) {
				done = true;
			}
		}
		return path;
	}
	
	public List<Class> getAdjacent(Class first,List<Class> closedList) {
		List<Class> adjacentClasses = new LinkedList<Class>();
		int x = first.getX();
		int y = first.getY();
		Class adjacent;
		
		if (x > 0)
		{
			adjacent = getClass(x - 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentClasses.add(adjacent);
			}
		}
		if (x < width)
		{
			adjacent = getClass(x + 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentClasses.add(adjacent);
			}
		}
		if (y > 0)
		{
			adjacent = this.getClass(x, y - 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentClasses.add(adjacent);
			}
		}
		if (y < height)
		{
			adjacent = this.getClass(x, y + 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentClasses.add(adjacent);
			}
		}
		return adjacentClasses;
	}
	
	public final List<Class> findPath(int startX, int startY, int goalX, int goalY) {
		if(startX == goalX && startY == goalY) {
			return new LinkedList<Class>();
		}
		
		List<Class> openList = new LinkedList<Class>();
		List<Class> closedList = new LinkedList<Class>();
		
		openList.add(classes[startX][startY]);
		while(true) {
			Class current = lowestFInList(openList);
			openList.remove(current);
			closedList.add(current);
			
			if(current.getX() == goalX && current.getY() == goalY) {
				return calcPath(classes[startX][startY],current);
			}
			
			List<Class> adjacentClasses = getAdjacent(current,closedList);
			for(Class adjacent: adjacentClasses) {
				if(!openList.contains(adjacent)) {
					adjacent.setParent(current);
					adjacent.setH(classes[goalX][goalY]);
					adjacent.setG(current);
					openList.add(adjacent);
				}
				else if(adjacent.getG() > adjacent.calculateG(current)) {
					adjacent.setParent(current);
					adjacent.setG(current);
				}
			}
			if(openList.isEmpty()) {
				return new LinkedList<Class>();
			}
		}
	}
}