package classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Battlefield {

	private ArrayList<Creature>[][] field;
	//private boolean[][] enemy;
	private List<Vertex> vertexList;
	
	@SuppressWarnings("unchecked")
	Battlefield() {
		this.field = new ArrayList[10][10];
		fillVertexList();
	}

	public void fillVertexList() {
		this.vertexList = new ArrayList<Vertex>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				this.vertexList.add(new Vertex(i, j));
			}
		}
	}
	
	public void put(ArrayList<Creature> units, int x, int y) {
		this.field[x][y] = units;
	}

	public void move(int currentX, int currentY, int futureX, int futureY) {
		fillVertexList();
		
		ArrayList<Creature> startPosition = field[currentX][currentY];
		field[currentX][currentY] = null;
		
		addAdjacent(this.vertexList);
		PathFinder.computePaths( getVertex(currentX , currentY) , this.vertexList);
		List<Vertex> path = PathFinder.getShortestPathTo( getVertex(futureX, futureY) );
		
		if (startPosition.get(0).getStamina() + 1 < path.size()) {
			field[currentX][currentY] = startPosition;
			return;
		}
		
		field[futureX][futureY] = startPosition;
		System.out.println(path);
	}

	public boolean canMove(int currentX, int currentY, int futureX, int futureY) {
		if (currentX < 0 || currentX > 9 ||
				currentY < 0 || currentY > 9 ||
				futureX < 0 || futureX > 9 ||
				futureY < 0 || futureY > 9 ) {
			System.out.println("Invalid command! Positions are indexed from 0 to 9.");
			return false;
		} else if (field[currentX][currentY] == null) {
			System.out.println("Invalid command! There is noone to move.");
			return false;
		} else if (field[futureX][futureY] != null) {
			System.out.println("Invalid command! You can't move to an occupied position.");
			return false;
		}
		return true;
	}
	
	public void addAdjacent(List<Vertex> vertexList) {

		for (int i = 0; i < 100; i++) {
			int x = i / 10;
			int y = i % 10;
			if (field[x][y] == null) {	
				ArrayList<Edge> adj = vertexList.get(i).adjacencies = new ArrayList<Edge>();
				
				if (y < 9 && field[x][y + 1] == null)		 		{ adj.add(new Edge(x, y + 1));  }
				if (y > 0 && field[x][y - 1] == null)          		{ adj.add(new Edge(x, y - 1)); }
				if (x > 0 && field[x - 1][y] == null) 				{ adj.add(new Edge(x - 1, y)); }
				if (x > 0 && y > 0 && field[x - 1][y - 1] == null)	{ adj.add(new Edge(x - 1, y - 1)); }
				if (x > 0 && y < 9 && field[x - 1][y + 1] == null)	{ adj.add(new Edge(x - 1, y + 1)); }
				if (x < 9 && field[x + 1][y] == null)				{ adj.add(new Edge(x + 1, y)); }
				if (x < 9 && y > 0 && field[x + 1][y - 1] == null) 	{ adj.add(new Edge(x + 1, y - 1)); }
				if (x < 9 && y < 9 && field[x + 1][y + 1] == null)	{ adj.add(new Edge(x + 1, y + 1)); }	
			}
		}
	}
	
	public void printBattlefield(boolean[][] enemy) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("battlefield.txt", "UTF-8");
			for (int i = 0; i < 10; i++) {
				writer.print("|" + i + " |");
				for (int j = 0; j < 10; j++) {
					if (this.field[i][j] == null || this.field[i][j].size() == 0) {
						writer.print("    |");
					} else if (this.field[i][j].get(0).getClass() == (new Peasant()).getClass()) {
						if (enemy[i][j]) {
							writer.print("EP" + this.field[i][j].size() + 
									(this.field[i][j].size() < 10 ? " |" : "|"));
						} else {
							writer.print(" P" + this.field[i][j].size() + 
									(this.field[i][j].size() < 10 ? " |" : "|"));
						}
					} else if (this.field[i][j].get(0).getClass() == (new Archer()).getClass()) {
						if (enemy[i][j]) {
							writer.print("EA" + this.field[i][j].size() + 
									(this.field[i][j].size() < 10 ? " |" : "|"));
						} else {
							writer.print(" A" + this.field[i][j].size() + 
									(this.field[i][j].size() < 10 ? " |" : "|"));
						}
					} else if (this.field[i][j].get(0).getClass() == (new Footman()).getClass()) {
						if (enemy[i][j]) {
							writer.print("EF" + this.field[i][j].size() + 
									(this.field[i][j].size() < 10 ? " |" : "|"));
						} else {
							writer.print(" F" + this.field[i][j].size() + 
									(this.field[i][j].size() < 10 ? " |" : "|"));
						}
					} else if (this.field[i][j].get(0).getClass() == (new Griffon()).getClass()) {
						if (enemy[i][j]) {
							writer.print("EG" + this.field[i][j].size() + 
									(this.field[i][j].size() < 10 ? " |" : "|"));
						} else {
							writer.print(" G" + this.field[i][j].size() + 
									(this.field[i][j].size() < 10 ? " |" : "|"));
						}
					}
				}
				writer.println();
				writer.println("------------------------------------------------------");
			}
			writer.println("   | 0  | 1  | 2  | 3  | 4  | 5  | 6  | 7  | 8  | 9  |");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		openFile();
	}
	
	private void openFile() {
		ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "battlefield.txt");
		try {
			pb.directory();
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public Boolean canAttack(int currentX, int currentY, int futureX, int futureY) {
		if (currentX < 0 || currentX > 9 ||
				currentY < 0 || currentY > 9 ||
				futureX < 0 || futureX > 9 ||
				futureY < 0 || futureY > 9 ) {
			System.out.println("Invalid command! Positions are indexed from 0 to 9.");
			return false;
		} else if (field[currentX][currentY] == null) {
			System.out.println("Invalid command! You have no units there.");
			return false;
		} else if (field[futureX][futureY] == null) {
			System.out.println("Invalid command! There is nobody to attack.");
			return false;
		}
		return true;
	}

	public Vertex getVertex(int x, int y) {
		return this.vertexList.get( x * 10 + y);
	}

	public List<Vertex> getVertexList() {
		return vertexList;
	}

	public ArrayList<Creature>[][] getField() {
		return field;
	}
	
}
