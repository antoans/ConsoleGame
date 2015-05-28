package classes;

import java.util.ArrayList;

public class Player {

	private ArrayList<Creature> peasants = new ArrayList<Creature>();
	private ArrayList<Creature> footmen = new ArrayList<Creature>();
	private ArrayList<Creature> archers = new ArrayList<Creature>();
	private ArrayList<Creature> griffons = new ArrayList<Creature>();
	@SuppressWarnings("unchecked") 
	private ArrayList<Creature>[] army = new ArrayList[4];
	private int gold;
	
	public Player() {
		this.gold = 300;
		army[0] = peasants;
		army[1] = footmen;
		army[2] = archers;
		army[3] = griffons;
	}
	
	public ArrayList<Creature> getPeasants() {
		return peasants;
	}

	public void setPeasants(ArrayList<Creature> peasants) {
		this.peasants = peasants;
	}

	public ArrayList<Creature> getFootmen() {
		return footmen;
	}

	public void setFootmen(ArrayList<Creature> footmen) {
		this.footmen = footmen;
	}

	public ArrayList<Creature> getArchers() {
		return archers;
	}

	public void setArchers(ArrayList<Creature> archers) {
		this.archers = archers;
	}

	public ArrayList<Creature> getGriffons() {
		return griffons;
	}

	public void setGriffons(ArrayList<Creature> griffons) {
		this.griffons = griffons;
	}

	public ArrayList<Creature>[] getArmy() {
		return army;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getGold() {
		return this.gold;
	}
	
	public void addGold(int amount) {
		this.gold += amount;
	}
	public void subtractGold(int amount) {
		this.gold -= amount;
	}
	public void addUnits(TypeOfUnit type, int amount) {
		switch (type) {
		case Peasant:
			for (int i = 0; i < amount; i++) {
				this.peasants.add(new Peasant());
			}
			break;
		case Footman:
			for (int i = 0; i < amount; i++) {
				this.footmen.add(new Footman());
			}
			break;
		case Archer:
			for (int i = 0; i < amount; i++) {
				this.archers.add(new Archer());
			}
			break;
		case Griffon:
			for (int i = 0; i < amount; i++) {
				this.griffons.add(new Griffon());
			}
			break;
		}
	}
	public void removeUnits(TypeOfUnit type, int amount) {
		switch (type) {
		case Peasant:
			if (this.peasants.size() <= amount) {
				this.peasants.clear();
			} else {
				for (int i = 0; i < amount; i++) {
					this.peasants.remove(0);
				}
			}			
			break;
		case Footman:
			if (this.footmen.size() <= amount) {
				this.footmen.clear();
			} else {
				for (int i = 0; i < amount; i++) {
					this.footmen.remove(0);
				}
			}
			break;
		case Archer:
			if (this.archers.size() <= amount) {
				this.archers.clear();
			} else {
				for (int i = 0; i < amount; i++) {
					this.archers.remove(0);
				}
			}
			break;
		case Griffon:
			if (this.griffons.size() <= amount) {
				this.griffons.clear();
			} else {
				for (int i = 0; i < amount; i++) {
					this.griffons.remove(0);
				}
			}
			break;
		}
	}
	public void printUnits() {
		System.out.printf("Peasants : %s\n", this.peasants.size());
		System.out.printf("Footmen  : %s\n", this.footmen.size());
		System.out.printf("Archers  : %s\n", this.archers.size());
		System.out.printf("Griffons : %s\n", this.griffons.size());
	}
}
