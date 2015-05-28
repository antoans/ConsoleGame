package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Game {

	private Player player;
	private Player enemy;
	private Map<TypeOfUnit, Integer> prices = new HashMap<TypeOfUnit, Integer>();
	public Battlefield battlefield = new Battlefield(); 
	
	public Game() { 
		prices.put(TypeOfUnit.Peasant, 30);
		prices.put(TypeOfUnit.Footman, 90);
		prices.put(TypeOfUnit.Archer, 50);
		prices.put(TypeOfUnit.Griffon, 150);
		
		player = new Player();
		enemy = new Player();
		enemy.addUnits(TypeOfUnit.Peasant, 10);
	}
	
	public void run() {
		int choice;
	    System.out.println("============================");
	    System.out.println("|    World of MathCraft    |");
	    System.out.println("============================");
	    System.out.println("|   Menu :                 |");
	    System.out.println("|        1. Start Game     |");
	    System.out.println("|        2. Manual         |");
	    System.out.println("|        3. Exit           |");
	    System.out.println("============================");
	    System.out.print("Your choice :  ");

	    Scanner in = new Scanner(System.in);
	    while (in.hasNextLine()) {	
		    choice = in.nextInt();
		    switch (choice) {
		    case 1:
		      ingameMenu();
		      break;
		    case 2:
		      System.out.println("Option 2 selected");
		      break;
		    case 3:
		      System.exit(0);
		      break;
		    default:
		      System.out.println("Invalid selection");
		      run();
		      break;
		    }
	    }
	    in.close();
	}
	
	private void ingameMenu() {
		System.out.println();
		
		int choice;

	    System.out.println("============================");
	    System.out.println("|     1. Fight             |");
	    System.out.println("|     2. Visit the shop    |");
	    System.out.println("|     3. Back              |");
	    System.out.println("============================");
	    System.out.print("Your choice :  ");
	    
	    Scanner in = new Scanner(System.in);
	    while (in.hasNextLine()) {
	    	choice = in.nextInt();
		    switch (choice) {
			case 1:
				fight();
				break;
			case 2:
				shop();
				break;
			case 3:
				ingameMenu();
				break;
			default:
			      System.out.println("Invalid selection");
			      ingameMenu();
				break;
			}
		}  
	    in.close();
	}
	
	private void fight() {
		putArmiesOnBattlefield();
		
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {	
			String[] commands = in.nextLine().split(" ");
			if (commands[0].equals("move")) {
				int currentX = Character.getNumericValue(commands[1].charAt(1));
				int currentY = Character.getNumericValue(commands[1].charAt(3));
				int futureX = Character.getNumericValue(commands[2].charAt(1));
				int futureY = Character.getNumericValue(commands[2].charAt(3));
				
				battlefield.move(currentX, currentY, futureX, futureY);
			} else if (commands[0].equals("attack")) {

				int attackerX = Character.getNumericValue(commands[1].charAt(1));
				int attackerY = Character.getNumericValue(commands[1].charAt(3));
				int targetX = Character.getNumericValue(commands[2].charAt(1));
				int targetY = Character.getNumericValue(commands[2].charAt(3));
				
				battlefield.canAttack();
				
				this.battlefield.fillVertexList();
				ArrayList<Creature> startPosition = battlefield.getField()[attackerX][attackerY];
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
				}
			} else if (commands[0].equals("print")) {
				this.battlefield.printBattlefield();
			} else if (commands[0].equals("back")) {
				ingameMenu();
			}
			
		}
		in.close();
	}

	private void putArmiesOnBattlefield() {
		for (ArrayList<Creature> units : this.player.getArmy()) {
			if (units.size() == 0) { }
			else {
				int position;
				do {
					position = (int)( Math.random() * 10 );
				} while (this.battlefield.getField()[position][0] != null);
				this.battlefield.put(units, position, 0);
				this.battlefield.setEnemy(position, 0, false);
			}
		}
		
		for (ArrayList<Creature> units : this.enemy.getArmy()) {
			if (units.size() == 0) { }
			else {
				int position;
				do {
					position = (int)( Math.random() * 10 );
				} while (this.battlefield.getField()[position][1] != null);
				this.battlefield.put(units, position, 1);
				this.battlefield.setEnemy(position, 1, true);
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
}
