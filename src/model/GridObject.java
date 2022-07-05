package model;

/**
 * Klasse um die Eigenschaften der Objekte im Grid zu speichern.
 * Die wichtigen Eigenschaften sind position, movable (ob das Objekt
 * bewegt werden kann) und stackable (stapelbar, ob der Player oder 
 * eine Kiste noch auf das Feld können).
 * 
 * @author Andrej Felde, Dominik Renkel
 */
public class GridObject {

	private final Coordinate position;
	private boolean movable;
	private boolean stackable;
	private final GridObjectType type;
	
	//b) Fügen Sie dem Paket model einen weiteren Aufzählungstyp mit dem Namen GridObjectType hinzu. Dieser
	//   soll folgende Werte besitzen:
	
	public enum GridObjectType {BOX, BOX_ON_GOAL, FIELD, GOAL, PLAYER, PLAYER_ON_GOAL, WALL }

	/**
	 * Konstruktor des GridObject.
	 * Zustaendig für die Erstellung einer neuen Instanz.
	 * 
	 * @param position Die Koordinate an der sich das Objekt befindet
	 * @param type Der Typus des Feldes.
	 */
	public GridObject(Coordinate position, GridObjectType type) {
		this.position = position;
		this.type = type;

		initProperties();
	}


	//TODO task 3.1a
	private void initProperties() {
		
	}

	
	//TODO task 3.1b
	public Coordinate getPosition() {
		return null;
	}

	public boolean isMovable() {
		return false;
	}

	public boolean isStackable() {
		return false;
	}

	public GridObjectType getType() {
		return null;
	}

	
	//TODO task 3.1c
	@Override
	public String toString() {
		return "";
	}
}