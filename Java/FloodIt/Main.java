import java.util.List;

public class Main {
	private static final int COL = 49;
	private static final int ROW = 49;
	private static final int FLOOR = 2;
	private int[] stairs1;
	private int[] stairs2;
	private int[][] m1;
	private int[][] m2;
	public Main() {
		int[][][] map = new int[ROW][COL][FLOOR];
		for(int k=0;k<COL;k++) {
			for(int i=0;i<ROW;i++) {
				for(int j=0;j<FLOOR;j++) {
					map[i][k][j] = 0;
				}
			}
		}
		
		for(int k=2;k<30;k++) {
			for(int j=0;j<map[k][1].length;j++) {
				map[k][6][j] = -1;
			}
		}
		for(int k=8;k<36;k++) {
			for(int j=0;j<map[k][1].length;j++) {
				map[k][17][j] = -1;
			}
		}
		for(int k=8;k<36;k++) {
			for(int j=0;j<map[k][1].length;j++) {
				map[k][35][j] = -1;
			}
		}
		for(int k=6;k<36;k++) {
			for(int j=0;j<map[k][1].length;j++) {
				map[8][k][j] = -1;
			}
		}
		for(int k=6;k<18;k++) {
			for(int j=0;j<map[k][0].length;j++) {
				map[29][k][j] = -1;
			}
		}
		for(int k=11;k<36;k++) {
				map[35][k][0] = -1;
		}
		for(int k=15;k<36;k++) {
			map[35][k][1] = -1;
		}
		for(int k=36;k<41;k++) {
			map[35][k][0] = -1;
		}
		for(int k=40;k<49;k++) {
			map[40][k][0] = -1;
		}
		for(int k=35;k<41;k++) {
			map[k][40][0] = -1;
		}
		for (int k=6;k<18;k++) {
			map[15][k][0] = -1;
		}
		for(int k=18;k<30;k++) {
			map[16][k][0] = -1;
		}
		for(int k=8;k<25;k++) {
			map[k][29][0] = -1;
		}
		for(int k=26;k<36;k++) {
			map[24][k][0] = -1;
		}
		for(int k=25;k<36;k++) {
			map[k][26][0] = -1;
		}
		for(int k=8;k<12;k++) {
			map[k][5][1] = -1;
		}
		map[26][34][0] = -1;
		map[26][35][0] = -1;
		map[9][21][0] = -1;
		map[9][25][0] = -1;
		map[9][26][0] = -1;
		map[26][34][0] = -1;
		map[26][35][0] = -1;
		
		
		map[2][5][0] = 151;
		map[1][6][0] = 301;		//150A
		map[2][7][0] = 150;
		map[5][5][0] = 17;		//stair 7
		map[5][5][1] = 27;		//stair 7
		map[9][5][0] = 153;
		map[8][5][0] = 152;
		map[13][5][0] = 154;
		map[14][5][0] = 155;
		map[17][5][0] = 157;
		map[20][5][0] = 159;
		map[21][5][0] = 161;
		map[22][5][0] = 163;
		map[25][5][0] = 165;
		map[28][5][0] = 167;
		map[29][1][0] = 16;		//stair 6
		map[29][1][1] = 26;		//stair 6
		map[6][7][0] = 302;		//138A
		map[7][7][0] = 303;		//138B
		map[7][9][0] = 138;
		map[9][8][0] = 136;
		map[9][9][0] = 304;		//136A
		map[9][10][0] = 134;
		map[14][7][0] = 156;
		map[16][7][0] = 158;
		map[21][7][0] = 160;
		map[26][7][0] = 162;
		map[28][11][0] = 164;
		map[28][12][0] = 168;
		map[30][12][0] = 15;		//stair 5
		map[30][12][1] = 25;		//stair 5
		map[35][11][0] = 400;	//gym
		map[36][12][0] = 118;
		map[7][13][0] = 140;
		map[7][20][0] = 142;
		map[7][22][0] = 144;
		map[7][25][0] = 146;
		map[7][27][0] = 148;
		map[7][29][0] = 149;
		map[7][30][0] = 13;		//stair 3
		map[7][30][1] = 23;		//stair 3
		map[9][13][0] = 132;
		map[11][16][0] = 130;
		map[10][18][0] = 127;
		map[10][21][0] = 129;
		map[10][25][0] = 131;
		map[10][26][0] = 133;
		map[14][16][0] = 128;
		map[17][16][0] = 126;
		map[20][16][0] = 124;
		map[27][16][0] = 122;
		map[28][16][0] = 305;	//122A
		map[32][16][0] = 120;
		map[15][18][0] = 125;
		map[19][18][0] = 123;
		map[20][18][0] = 121;
		map[25][18][0] = 119;
		map[28][18][0] = 117;
		map[30][18][0] = 115;
		map[34][18][0] = 113;
		map[36][18][0] = 114;
		map[17][18][0] = 14;		//stair 4
		map[17][18][1] = 24;		//stair 4
		map[34][23][0] = 111;
		map[34][24][0] = 109;
		map[34][26][0] = 107;
		map[34][27][0] = 11;		//stair 1
		map[34][27][1] = 21;		//stair 1
		map[34][34][0] = 105;
		map[26][33][0] = 12;		//stair 2
		map[26][33][1] = 22;		//stair 2
		map[36][24][0] = 108;
		map[36][29][0] = 106;
		map[36][32][0] = 104;
		map[35][41][0] = 500;	//auditorium
		map[41][43][0] = 501;	//music room
		map[41][45][0] = 502;	//band room
		map[41][48][0] = 503;	//choral room
		map[39][48][0] = 504;	//005 (other choral room)
		map[26][36][0] = 505;	//cafeteria
		
		map[7][15][1] = -1;
		map[26][34][1] = -1;
		
		map[2][5][1] = 280;
		map[2][7][1] = 279;
		map[7][5][1] = 281;
		map[9][4][1] = 282;
		map[11][4][1] = 283;
		map[14][5][1] = 285;
		map[16][5][1] = 287;
		map[19][5][1] = 289;
		map[21][5][1] = 291;
		map[22][5][1] = 293;
		map[25][5][1] = 295;
		map[28][5][1] = 297;
		map[7][9][1] = 258;
		map[7][11][1] = 260;
		map[7][12][1] = 262;
		map[7][13][1] = 264;
		map[6][14][1] = 266;
		map[7][15][1] = 267;
		map[7][16][1] = 268;
		map[7][23][1] = 270;
		map[7][26][1] = 272;
		map[7][27][1] = 274;
		map[7][29][1] = 276;
		map[7][33][1] = 600;	//art room 276
		map[7][35][1] = 601;	//art room 278
		map[9][34][1] = 602;	//library
		map[9][8][1] = 254;
		map[14][7][1] = 284;
		map[16][7][1] = 286;
		map[19][7][1] = 288;
		map[21][7][1] = 290;
		map[24][7][1] = 292;
		map[25][7][1] = 294;
		map[28][11][1] = 296;
		map[28][12][1] = 298;
		map[9][12][1] = 252;
		map[9][13][1] = 250;
		map[11][16][1] = 248;
		map[14][16][1] = 246;
		map[15][16][1] = 244;
		map[18][16][1] = 242;
		map[21][16][1] = 240;
		map[23][16][1] = 238;
		map[26][16][1] = 236;
		map[30][16][1] = 234;
		map[36][15][1] = 232;
		map[17][18][1] = 243;
		map[19][18][1] = 241;
		map[22][18][1] = 239;
		map[25][18][1] = 237;
		map[28][18][1] = 235;
		map[39][18][1] = 233;
		map[32][18][1] = 231;
		map[33][18][1] = 229;
		map[34][18][1] = 227;
		map[36][18][1] = 230;
		map[36][20][1] = 228;
		map[36][23][1] = 226;
		map[36][26][1] = 224;
		map[36][27][1] = 220;
		map[36][30][1] = 218;
		map[36][33][1] = 216;
		map[36][35][1] = 214;
		map[34][22][1] = 225;
		map[34][25][1] = 223;
		map[34][29][1] = 219;
		map[34][32][1] = 217;
		map[34][33][1] = 215;
		map[13][34][1] = 201;
		map[15][34][1] = 203;
		map[17][34][1] = 205;
		map[22][34][1] = 207;
		map[25][34][1] = 209;
		map[28][34][1] = 211;
		map[32][34][1] = 213;
		map[13][36][1] = 200;
		map[21][36][1] = 202;
		map[22][36][1] = 204;
		map[26][36][1] = 206;
		map[28][36][1] = 208;
		map[31][36][1] = 210;
		map[34][36][1] = 212;
		
		
		int[] stair1 = {11,12,13,14,15,16,17};
		stairs1 = stair1;
		int[] stair2 = {21,22,23,24,25,26,27};
		stairs2 = stair2;
		
		
		m1 = new int[49][49];
		for(int k=0;k<COL;k++) {
			for(int i=0;i<ROW;i++) {
				m1[k][i] = map[k][i][0];
			}
		}
		
		m2 = new int[49][49];
		for(int k=0;k<COL;k++) {
			for(int i=0;i<ROW;i++) {
				m2[k][i] = map[k][i][1];
			}
		}
	}
	public List<Class> findPath(int RNa, int RNb) {
		int f1 = getFloor(RNa);
		int f2 = getFloor(RNb);
		if(getFloor(RNa) == getFloor(RNb)) {
			if(f1 == 0) {
				Map map = new Map(m1);
				return map.findPath(getX(RNa),getY(RNa),getX(RNb),getY(RNb));
			}
			else {
				Map map = new Map(m2);
				return map.findPath(getX(RNa),getY(RNa),getX(RNb),getY(RNb));
			}
		}
		else {
			List<Class> path;
			int stair1 = findNearestStaircase(RNa,RNb,f1);
			int stair2 = findNearestStaircase(RNa,RNb,f2);
			Map map1 = new Map(m1);
			Map map2 = new Map(m2);
			path = map1.findPath(getX(RNa), getY(RNa), getX(stair1), getY(stair1));
			java.util.List<Class> path2 = map2.findPath(getX(stair2), getY(stair2), getX(RNb), getY(RNb));
			for(int k=0;k<path2.size();k++) {
				path.add(path2.get(k));
			}
			return path;
		}
	}
	private int findNearestStaircase(int RNa,int RNb,int f) {
		int f1 = getFloor(RNa);
		int f2 = getFloor(RNb);
		if(f1 == 0 && f1 != f2) {
			int minDistance1 = getDistance(RNa,stairs1[0]);
		for(int k=0;k<stairs1.length;k++) {
			if(getDistance(RNa,stairs1[k]) < minDistance1) {
				minDistance1 = getDistance(RNa,stairs1[k]);
			}
		}
		int minDistance2 = getDistance(RNb,stairs2[0]);
		for(int k=0;k<stairs2.length;k++) {
			if(getDistance(RNb,stairs2[k]) < minDistance2) {
				minDistance2 = getDistance(RNb,stairs2[k]);
			}
		}
		if(minDistance1 < minDistance2) {
			for(int k=0;k<stairs1.length;k++) {
				if(getDistance(RNa,stairs1[k]) == minDistance1) {
					if(f == 0) {
					return stairs1[k];
					}
					else {
						return stairs2[k];
					}
				}
			}
		}
		else {
			for(int k=0;k<stairs2.length;k++) {
				if(getDistance(RNb,stairs2[k]) == minDistance2) {
					if(f == 0) {
						return stairs1[k];
						}
						else {
							return stairs2[k];
						}
				}
			}
		}
		}
		else if(f2 == 0 && f2 != f1) {
			int minDistance1 = getDistance(RNb,stairs1[0]);
			for(int k=0;k<stairs1.length;k++) {
				if(getDistance(RNb,stairs1[k]) < minDistance1) {
					minDistance1 = getDistance(RNb,stairs1[k]);
				}
			}
			int minDistance2 = getDistance(RNa,stairs2[0]);
			for(int k=0;k<stairs2.length;k++) {
				if(getDistance(RNa,stairs2[k]) < minDistance2) {
					minDistance2 = getDistance(RNa,stairs2[k]);
				}
			}
			if(minDistance1 < minDistance2) {
				for(int k=0;k<stairs1.length;k++) {
					if(getDistance(RNb,stairs1[k]) == minDistance1) {
						if(f == 0) {
							return stairs1[k];
							}
							else {
								return stairs2[k];
							}
					}
				}
			}
			else {
				for(int k=0;k<stairs2.length;k++) {
					if(getDistance(RNa,stairs2[k]) == minDistance2) {
						if(f == 0) {
							return stairs1[k];
							}
							else {
								return stairs2[k];
							}
					}
				}
			}
		}
			return -1;
	}
	private int getDistance(int RNa,int RNb) {
		int deltaX = Math.abs(getX(RNb) - getX(RNa));
		int deltaY = Math.abs(getY(RNb) - getY(RNa));
		return deltaX + deltaY;
	}
	private int getY(int RNa) {
		int y = 0;
		if(getFloor(RNa) == 0) {
			for(int k=0;k<ROW;k++) {
				for(int i=0;i<COL;i++) {
					if(m1[k][i] == RNa) {
						y = k;
					}
				}
			}
		}
		else {
			for(int k=0;k<ROW;k++) {
				for(int i=0;i<COL;i++) {
					if(m2[k][i] == RNa) {
						y = k;
					}
				}
			}
		}
		return y;
	}
	private int getX(int RNa) {
		int x = 0;
		if(getFloor(RNa) == 0) {
			for(int k=0;k<ROW;k++) {
				for(int i=0;i<COL;i++) {
					if(m1[k][i] == RNa) {
						x = i;
					}
				}
			}
		}
		else {
			for(int k=0;k<ROW;k++) {
				for(int i=0;i<COL;i++) {
					if(m2[k][i] == RNa) {
						x = i;
					}
				}
			}
		}
		return x;
	}
	private int getFloor(int RNa) {
		if((RNa > 10 && RNa < 20) || (RNa >= 100 && RNa < 200) || (RNa >= 300 && RNa < 600)) {
			return 0;
		}
		else {
			return 1;
		}
	}
	public int getRoomNumber(int r, int c, int f) {
		if(f == 0) {
			return m1[r][c];
		}
		else {
			return m2[r][c];
		}
	}
	public int totalDistance(int[] a) {
		int distance = 0;
		for(int k=0;k<a.length - 1;k++) {
			distance += findPath(a[k],a[k+1]).size();
		}
		return distance;
	}
	public int[] optimize(int[] classes) {
		int[] best = new int[8];
		best[0] = classes[0];
		int index = 1;
		int indexC = 0;
		for(int k=indexC;k<classes.length - 1;k++) {
			int minDistance = findPath(classes[indexC],classes[indexC + 1]).size();
			for(int i=indexC+1;i<classes.length;i++) {
				if(findPath(classes[k],classes[i]).size() < minDistance) {
					minDistance = findPath(classes[k],classes[i]).size();
				}
			}
			for(int i=indexC + 1;i<classes.length;i++) {
				if(findPath(classes[k],classes[i]).size() == minDistance) {
					best[index] = classes[i];
					int a = classes[i];
					int b = classes[index];
					classes[index] = a;
					classes[i] = b;
					index++;
					break;
				}
			}
			indexC++;
		}
		return best;
	}
	public String toString() {
		String s = "";
		for(int k=0;k<COL;k++) {
			for(int i=0;i<ROW;i++) {
					s += m1[k][i];
					if(m1[k][i] < 10 && m1[k][i] >= 0) {
						s += "  ";
					}
					else if(m1[k][i] == -1) {
						s += " ";
					}
						
					else if(m1[k][i] >= 10 && m1[k][i] < 100) {
						s += " ";
					}
					s += " ";
			}
			s += "\n";
		}
		s += "\n";
		s += "\n";
		s += "\n";
		for(int k=0;k<COL;k++) {
			for(int i=0;i<ROW;i++) {
					s += m2[k][i];
					if(m2[k][i] < 10 && m2[k][i] >= 0) {
						s += "  ";
					}
					else if(m2[k][i] == -1 ) {
						s += " ";
					}
					else if(m2[k][i] >= 10 && m2[k][i]< 100) {
						s += " ";
					}
					s += " ";
			}
			s += "\n";
		}
		return s;
	}
	
	public static void main(String[] args) {
		int[] classes = {240,400,252,201,278,134,107,166};

		Main main = new Main();
		System.out.println(10 * main.totalDistance(classes));
		int[] newClasses = main.optimize(classes);
		System.out.println(10 * main.totalDistance(newClasses));
	}
}