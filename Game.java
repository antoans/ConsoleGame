package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Game {

	private Player player;
	private Player enemy;
	private boolean[][] enemies = new boolean[10][10];
	private Map<TypeOfUnit, Integer> prices = new HashMap<TypeOfUnit, Integer>();
	private Map<TypeOfUnit, Boolean> actions = new HashMap<TypeOfUnit, Boolean>();
	private Battlefield battlefield = new Battlefield(); 
	private List<Integer> moved = new ArrayList<Integer>();
	
	public Game() { 
		prices.put(TypeOfUnit.Peasant, 30);
		prices.put(TypeOfUnit.Footman, 90);
		prices.put(TypeOfUnit.Archer, 50);
		prices.put(TypeOfUnit.Griffon, 150);
		
		player = new Player();
		enemy = new Player();
		enemy.addUnits(TypeOfUnit.Archer, 3);
		enemy.addUnits(TypeOfUnit.Peasant, 4);
		enemy.addUnits(TypeOfUnit.Footman, 1);
		enemy.addUnits(TypeOfUnit.Griffon, 1);
	}
	
	public void run() {
	    printMainMenu();
		int choice;
		Scanner in = new Scanner(System.in);
	    while (in.hasNext()) {	
		    try {
				choice = in.nextInt();
				switch (choice) {
				case 1:
					printInGameMenu();
					ingameMenu();
				  break;
				case 2:
					manual();
					run();
					break;
				case 3:
					System.exit(0);
					break;
				default:
					System.out.println("Invalid command");
					run();
					break;
				}
			} catch (Exception e) {
				in = new Scanner(System.in);
				System.out.println("Invalid command.");
			}
	    }
	}

	private void printMainMenu() {
		System.out.println("============================");
	    System.out.println("|    World of MathCraft    |");
	    System.out.println("============================");
	    System.out.println("|   Menu :                 |");
	    System.out.println("|        1. Start Game     |");
	    System.out.println("|        2. Manual         |");
	    System.out.println("|        3. Exit           |");
	    System.out.println("============================");
	    System.out.print("Your choice :  ");
	}
	
	private void ingameMenu() {
		System.out.println();
		
		int choice;
	    Scanner in = new Scanner(System.in);
	    while (in.hasNext()) {
		    try {
				choice = in.nextInt();
				switch (choice) {
				case 1:
					putArmiesOnBattlefield();
					fight();
					break;
				case 2:
					shop();
					break;
				case 3:
					run();
				}
		    } catch (Exception e) {
			    in = new Scanner(System.in);
		    	System.out.println("Invalid command.");
		    }
	    }
	}

	private void printInGameMenu() {
		System.out.println("============================");
	    System.out.println("|     1. Fight             |");
	    System.out.println("|     2. Visit the shop    |");
	    System.out.println("|     3. Back              |");
	    System.out.println("============================");
	    System.out.print("Your choice :  ");
	}
	
	private void fight() {
		renewActions();
		Scanner in = new Scanner(System.in);
		fight:
		while (in.hasNextLine()) {	
			String[] commands = in.nextLine().split(" ");
			if (commands[0].equals("move")) {
				int currentX = Character.getNumericValue(commands[1].charAt(1));
				int currentY = Character.getNumericValue(commands[1].charAt(3));
				int futureX = Character.getNumericValue(commands[2].charAt(1));
				int futureY = Character.getNumericValue(commands[2].charAt(3));
				
				if (battlefield.canMove(currentX, currentY, futureX, futureY) && 
						!(moved.contains(currentX * 10 + currentY)) ) {	
					battlefield.move(currentX, currentY, futureX, futureY);
					if (battlefield.getField()[currentX][currentY] != null) {
						System.out.println("Your soldiers can't go that far.");
					} else {
						moved.add(futureX * 10 + futureY);
						removeAction(futureX, futureY);
					}
				}
				in = new Scanner(System.in);
			} else if (commands[0].equals("attack")) {

				int attackerX = Character.getNumericValue(commands[1].charAt(1));
				int attackerY = Character.getNumericValue(commands[1].charAt(3));
				int targetX = Character.getNumericValue(commands[2].charAt(1));
				int targetY = Character.getNumericValue(commands[2].charAt(3));
				
				if (!(battlefield.canAttack(attackerX,attackerY,targetX,targetY))) {
					continue fight;
				}
				
				this.battlefield.fillVertexList();
				ArrayList<Creature> startPosition = battlefield.getField()[attackerX][attackerY];
				
				//make sure if the units belong to us, not the enemy
				Boolean isPlayers = isPlayers(startPosition);
				
				if (!isPlayers) {
					continue fight;
				}
				
				// check if it has already attacked
				if (!moved.contains(attackerX * 10 + attackerY)) {
				
					battlefield.getField()[attackerX][attackerY] = null;
					
					ArrayList<Creature> targetPosition = battlefield.getField()[targetX][targetY];
					battlefield.getField()[targetX][targetY] = null;
					
					battlefield.addAdjacent(battlefield.getVertexList());
					PathFinder.computePaths( battlefield.getVertex(attackerX , attackerY) ,
							battlefield.getVertexList());
					List<Vertex> path = PathFinder.getShortestPathTo( battlefield.getVertex(targetX, targetY) );
					battlefield.getField()[attackerX][attackerY] = startPosition;
					battlefield.getField()[targetX][targetY] = targetPosition;
					
					int distance = path.size() - 1;
					int range = startPosition.get(0).getRange();
					if ( distance > range ) {
						System.out.println("Out of range!");
					} else {
						for (Creature c : battlefield.getField()[attackerX][attackerY]) {
							if (battlefield.getField()[targetX][targetY] != null) {
								c.attack(battlefield.getField()[targetX][targetY]);
							}
						}
						moved.add(attackerX * 10 + attackerY);
						System.out.println("Attack successful.");
						//check if game is won
						if (enemy.getArmy()[0].size() == 0 &&
							enemy.getArmy()[1].size() == 0 &&
							enemy.getArmy()[2].size() == 0 &&
							enemy.getArmy()[3].size() == 0 ) {
							winGame();
						}
						
						removeAction(attackerX, attackerY);
					}
				}
				in = new Scanner(System.in);
			} else if (commands[0].equals("print")) {
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						enemies[i][j] = !(isPlayers(battlefield.getField()[i][j]));
					}
				}
				this.battlefield.printBattlefield(enemies);
			} else if(commands[0].equals("endturn")) {
				enemyTurn();
			} else if (commands[0].equals("back")) {
				ingameMenu();
			}	
			if (outOfMoves()) {
				enemyTurn();
			}
		}
	}

	private Boolean isPlayers(ArrayList<Creature> startPosition) {
		Boolean isPlayers = false;
		for (ArrayList<Creature> units : player.getArmy()) {
			if (units == startPosition) {
				isPlayers = true;
			}
		}
		return isPlayers;
	}

	private void putArmiesOnBattlefield() {
		for (ArrayList<Creature> units : this.player.getArmy()) {
			if (units.size() != 0) {
				int position;
				do {
					position = (int)( Math.random() * 10 );
				} while (this.battlefield.getField()[position][0] != null);
				this.battlefield.put(units, position, 0);
			}
		}
		
		for (ArrayList<Creature> units : this.enemy.getArmy()) {
			if (units.size() != 0) {
				int position;
				do {
					position = (int)( Math.random() * 10 );
				} while (this.battlefield.getField()[position][9] != null);
				this.battlefield.put(units, position, 9);
			}
		}
		
	}

	private void renewActions() {
		this.actions.put(TypeOfUnit.Peasant, player.getPeasants().size() != 0);
		this.actions.put(TypeOfUnit.Archer, player.getArchers().size() != 0);
		this.actions.put(TypeOfUnit.Footman, player.getFootmen().size() != 0);
		this.actions.put(TypeOfUnit.Griffon, player.getGriffons().size() != 0);
	}
	
	private void removeAction(int x, int y) {
		ArrayList<Creature> units = battlefield.getField()[x][y];
		if (units.get(0).getClass() == (new Peasant().getClass())) {
			actions.put(TypeOfUnit.Peasant, false);
		} else if (units.get(0).getClass() == (new Archer().getClass())) {
			actions.put(TypeOfUnit.Archer, false);
		} else if (units.get(0).getClass() == (new Footman().getClass())) {
			actions.put(TypeOfUnit.Footman, false);
		} else if (units.get(0).getClass() == (new Griffon().getClass())) {
			actions.put(TypeOfUnit.Griffon, false);
		}
	}
	private Boolean outOfMoves() {
		return this.actions.get(TypeOfUnit.Peasant)==false &&
				this.actions.get(TypeOfUnit.Archer)==false &&
				this.actions.get(TypeOfUnit.Footman)==false &&
				this.actions.get(TypeOfUnit.Griffon)==false ;
	}
	
	private void enemyTurn() {
		System.out.println("Enemy's turn ...");
		moved.clear();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				ArrayList<Creature> cell = battlefield.getField()[i][j];
				if (cell != null && !isPlayers(cell)) {
					searchForTarget( i , j );
				}
			}
		}
		//check if you lose
		if (player.getArmy()[0].size() == 0 &&
				player.getArmy()[1].size() == 0 &&
				player.getArmy()[2].size() == 0 &&
				player.getArmy()[3].size() == 0 ) {
				loseGame();
		}
		
		fight();
	}
	
	private void searchForTarget(int x, int y) {
		if (moved.contains(x * 10 + y)) { return;}
		ArrayList<Creature> attacker = battlefield.getField()[x][y];
		Boolean hasAttacked = false;
		search:
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				ArrayList<Creature> target = battlefield.getField()[i][j];
				if (target != null && target.size() != 0 && isPlayers(target)) {
					battlefield.getField()[x][y] = null;
					battlefield.getField()[i][j] = null;
					
					battlefield.fillVertexList();
					battlefield.addAdjacent(battlefield.getVertexList());
					PathFinder.computePaths( battlefield.getVertex(x , y) ,
							battlefield.getVertexList());
					List<Vertex> path = PathFinder.getShortestPathTo( battlefield.getVertex(i, j) );
					battlefield.getField()[x][y] = attacker;
					battlefield.getField()[i][j] = target;
					
					int distance = path.size() - 1;
					int range = attacker.get(0).getRange();
					if ( !(distance > range ) ) {
						for (Creature c : battlefield.getField()[x][y]) {
							if (battlefield.getField()[i][j] != null) {
								c.attack(battlefield.getField()[i][j]);
							}
						}
						hasAttacked = true;
						break search;
					}
				}
			}
		}
		if (!hasAttacked) {
			Boolean hasAMove = true;
			while (hasAMove) {
				int tryX, tryY;
				tryX = (int)( Math.random() * 10 );
				tryY = (int)( Math.random() * 10 );
				battlefield.move(x, y, tryX, tryY);
				if (battlefield.getField()[x][y] == null) {
					hasAMove = false;
					moved.add(10*tryX + tryY);
				}
			}
		}
		
	}
	private void shop() {
		System.out.println();
		System.out.println("Welcome to the shop.");
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {	
				String[] commands = in.nextLine().split(" ");
				if (commands[0].equals("buy")) {
					switch (commands[1]) {
					case "peasant":
						buy(TypeOfUnit.Peasant,Integer.parseInt(commands[2]));
						break;
					case "footman":
						buy(TypeOfUnit.Footman,Integer.parseInt(commands[2]));
						break;
					case "archer":
						buy(TypeOfUnit.Archer,Integer.parseInt(commands[2]));
						break;
					case "griffon":
						buy(TypeOfUnit.Griffon,Integer.parseInt(commands[2]));
						break;
					default:
						System.out.println("Invalid command!");
						break;
					}
				} else if (commands[0].equals("prices")) {
					System.out.println("============================");
				    System.out.println("|   Peasant - 30 gold      |");
				    System.out.println("|   Footman - 90 gold      |");
				    System.out.println("|   Archer  - 50 gold      |");
				    System.out.println("|   Griffon - 150 gold     |");
				    System.out.println("============================");
				} else if (commands[0].equals("gold")) {
					System.out.println(String.format("You have %s gold.", this.player.getGold()));
				} else if (commands[0].equals("exit")) {
			    	printInGameMenu();
					ingameMenu();
				} else if (commands[0].equals("army")) {
					this.player.printUnits();
				}
		}
		in.close();
	}

	private void buy(TypeOfUnit type, int amount) {
		int price = amount * this.prices.get(type);
		if (this.player.getGold() < price) {
			System.out.println("You have insufficient gold. You need " + 
					Integer.toString(price - this.player.getGold())
					+ " more!");
		} else {
			this.player.subtractGold(price);
			this.player.addUnits(type, amount);
			System.out.println(String.format("You have successfully purchased %s %s",
					amount, type.toString() ));
		}
	}
	
	private void manual() {
		System.out.println("-------------------------------------------------");
		System.out.println("| While in the shop :                           |");
		System.out.println("| - type 'buy [type] [ammount]' to buy units.   |");
		System.out.println("| (types are : peasant, archer, footman and     |");
		System.out.println("|             griffon).                         |");
		System.out.println("| - type 'gold' to view your gold.              |");
		System.out.println("| - type 'army' to view all of your units.      |");
		System.out.println("|                                               |");
		System.out.println("| While fighting:							    |");
		System.out.println("| - type 'print' to see the battlefield.        |");
		System.out.println("| - type 'endturn' to start your enemy's turn.  |");
		System.out.println("| - type 'move ([x],[y]) ([x],[y])' to move.    |");
		System.out.println("|                                               |");
		System.out.println("| The game ends when either you or your enemy   |");
		System.out.println("|     are left with 0 units.                    |");
		System.out.println("| Try to beat the AI :)                         |");
		System.out.println("-------------------------------------------------");
	}
	
	private void winGame() {
		System.out.println("-------------------------------------------------");
		System.out.println("| CONGRATULATIONS, YOU HAVE WON THE GAME !!!!   |");
		System.out.println("-------------------------------------------------");
		System.exit(0);
	}
	
	private void loseGame() {
		System.out.println("-------------------------------------------------");
		System.out.println("|           You have beed defeated :(            |");
		System.out.println("-------------------------------------------------");
		System.exit(0);
	}
}
