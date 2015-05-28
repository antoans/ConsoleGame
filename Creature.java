package classes;

import java.util.ArrayList;

public abstract class Creature {
	private int damage;
	private int defence;
	private int health;
	private int mana;
	private int stamina;
	private int critChance;
	private int range;
	
	public Creature(int damage, int defence, int health, int mana, int stamina,
			int critChance, int range) {
		setDamage(damage);
		setDefence(defence);
		setHealth(health);
		setMana(mana);
		setStamina(stamina);
		setCritChance(critChance);
		setRange(range);
	}
	
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public int getDefence() {
		return defence;
	}
	public void setDefence(int defence) {
		this.defence = defence;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	public int getStamina() {
		return stamina;
	}
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	public int getCritChance() {
		return critChance;
	}
	public void setCritChance(int critChance) {
		this.critChance = critChance;
	}
	
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public void attack(ArrayList<Creature> enemies) {
		if (enemies.isEmpty()) return;
		Creature enemy = enemies.get(0);
		enemy.setHealth( enemy.getHealth() - (this.getDamage() - enemy.getDefence()) );
		if (enemy.getHealth() <= 0) {
			enemies.remove(0);
		}
	}; 
}
