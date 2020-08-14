public class Class {
	private static final int COST = 10;
	private int y;
	private int x;
	private boolean walkable;
	private Class parent;
	private int g;
	private int h;
	public Class(int x, int y, boolean walkable) {
		this.y = y;
		this.x = x;
		this.walkable = walkable;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setG(Class parent) {
		g = (parent.getG() + COST);
	}
	public int calculateG(Class parent) {
		return (parent.getG() + COST);
	}
	public int getX() {
		return x;
	}
	public void setH(Class goal) {
		h = COST * (Math.abs(getX() - goal.getX()) + Math.abs(getY() - goal.getY()));
	}
	public boolean isWalkable() {
		return walkable;
	}
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}
	public Class getParent() {
		return parent;
	}
	public void setParent(Class parent) {
		this.parent = parent;
	}
	public int getF() {
		return g + h;
	}
	public int getH() {
		return h;
	}
	public int getG() {
		return g;
	}
	
	
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if(!(o instanceof Class)) {
			return false;
		}
		if(o == this) {
			return true;
		}
		Class n = (Class) o;
		if(n.getX() == x && n.getY() == y && n.isWalkable() == walkable) {
			return true;
		}
		return false;
	}
	
}
