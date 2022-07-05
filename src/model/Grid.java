package model;

import java.util.LinkedList;

import model.GridObject.GridObjectType;



/**
 * Diese Klasse beschreibt die gesamte Spiellogik sowie das Spielfeld ansich. Es
 * beinhaltet alle Methoden und Variablen zur Darstellung des Spielfelds.
 * 
 * @author Andrej Felde, Daniel Stein, Dominik Renkel, Markus Bommer, Markus
 *         Wedel
 *
 */
public class Grid {
	
	
	/*
	 * Aufgabe 1.a)
	 * 1.a)Erstellen Sie im Paket model einen Aufzählungstyp (enum) mit dem Namen Direction. Dieser soll folgende
		Werte besitzen:
	 */
	// X-Coordinates
	public static final int LEFT 	= -1; 
	public static final int RIGHT 	=  1;
	
	// Y-Coordinates
	
	public static final int UP 		=  1;
	public static final int DOWN 	= -1;
	
	private enum Direction {LEFT, RIGHT, UP, DOWN }
	

	/**
	 * Das aktuell angezeigte Spielbrett
	 */
	private GridObject[][] grid;

	/**
	 * Die Breite des Levels
	 */
	private final int levelWidth;

	/**
	 * Die Hoehe des Levels
	 */
	private final int levelHeight;

	/**
	 * Die gespeicherten Spielzustaende
	 */
	private LinkedList<GridObject[][]> states;

	/**
	 * Der Index des aktuellen Spielzustands
	 */
	private int statePointer;

	/**
	 * Eigenschaft ob das Spielbrett gewonnen wurde
	 */
	private boolean isWon;

	/**
	 * Eigenschaft ob sich das Spielbrett in einem Deadlock befindet
	 */
	private boolean containsDeadlock;

	/**
	 * Die Position des Spielers auf dem Spielbrett (als Coordinate dargestellt)
	 */
	private Coordinate playerPos;

	/**
	 * Der Konstruktor, der aus einem 2-dimensionalen char-Array ein neues
	 * Spielbrett vom Typ GridObject[][] erzeugt. Dabei wird die Methode
	 * initGrid() verwendet. Falls das uebergebene Array ein invalides Level
	 * darstellt, wird eine InvalidLevelException geworfen. Außerdem werden hier
	 * die Instanzvariablen für Hoehe und Breite auf konkrete Werte gesetzt und
	 * die Eigenschaften isWon und containsDeadlock werden auf false gesetzt.
	 * 
	 * @param loadedBoard
	 *            Das uebergebene 2-dimensionale char-Array, aus welchem ein
	 *            GridObject[][] erzeugt werden soll.
	 * 
	 * @throws InvalidLevelException
	 *             Wird geworfen, wenn das 2-dimensionale char-Array invalide
	 *             ist.
	 */
	public Grid(char[][] loadedBoard) throws InvalidLevelException {
		if (!Grid.isValidLevel(loadedBoard)) {
			throw new InvalidLevelException();
		}

		this.levelHeight = loadedBoard.length;
		this.levelWidth = loadedBoard[0].length;
		initGrid(loadedBoard);

		states = new LinkedList<GridObject[][]>();
		states.add(copyState(grid));
		statePointer = 0;

		isWon = false;
		containsDeadlock = false;

		findPlayerPos();
	}

	
	//TODO task 3.2a
	public void initGrid(char[][] loadedBoard) {
		
	}

	/**
	 * Getter Methode, die die Breite des Spielbrettes zurueckgibt
	 * 
	 * @return Die Breite des Spielbretts
	 */
	public int getLevelWidth() {
		return this.levelWidth;
	}

	/**
	 * Getter Methode, die die Hoehe des Spielbrettes zurueckgibt
	 * 
	 * @return Die Höhe des Spielbretts
	 */
	public int getLevelHeight() {
		return this.levelHeight;
	}

	/**
	 * Getter Methode, die das Spielfeld-Element an der Stelle mit den
	 * Koordinaten [x, y] zurueckgibt.
	 * 
	 * @param x
	 *            Die x-Koordiante des gesuchten Spielfeld-Elements
	 * @param y
	 *            Die y-Koordiante des gesuchten Spielfeld-Elements
	 * 
	 * @return Das Spielfeld-Element welches an der Stelle mit den Koordinaten
	 *         [x, y] liegt.
	 */
	public GridObject getGridObjectAt(int x, int y) {
		return grid[y][x];
	}

	/**
	 * Getter Methode, die das Spielfeld-Element an der Stelle der uebergebenen
	 * Koordinate zurueckgibt.
	 * 
	 * @param pos
	 *            Die Koordinate des gesuchten Spielfeld-Elements
	 * 
	 * @return Das Spielfeld-Element, welches an der Stelle der uebergebenen
	 *         Koordinate liegt.
	 */
	public GridObject getGridObjectAt(Coordinate pos) {
		return grid[pos.getY()][pos.getX()];
	}

	/**
	 * Getter Methode, die das gesamte aktuelle Spielfeld zurueckgibt
	 * 
	 * @return Das aktuelle Spielfeld
	 */
	public GridObject[][] getGrid() {
		return grid;
	}

	/**
	 * Getter Methode, die die Liste der gespeicherten Spielzustaende
	 * zurueckgibt.
	 * 
	 * @return Die Liste der gespeicherten Spielzustaende
	 */
	public LinkedList<GridObject[][]> getStates() {
		return states;
	}

	/**
	 * Getter Methode, die die aktuelle Spielerposition auf dem Spielfeld als
	 * Koordinate zurueckgibt.
	 * 
	 * @return Die aktuelle Spielerposition auf dem Spielfeld, als Koordinate.
	 */
	public Coordinate getPlayerPosition() {
		return playerPos;
	}

	/**
	 * Getter Methode, die die aktuelle Anzahl der durchgefuehrten Spielzuege
	 * zurueckgibt.
	 * 
	 * @return Die aktuelle Anzahl der Spielzuege
	 */
	public int getStepCount() {
		return statePointer;
	}

	/**
	 * Getter Methode, die die Eigenschaft zurueckgibt, ob das Spielbrett
	 * gewonnen wurde
	 * 
	 * @return true, wenn das Spiel gewonnen wurde; false, wenn das Spiel noch
	 *         nicht gewonnen wurde.
	 */
	public boolean getIsWon() {
		return isWon;
	}

	/**
	 * Getter Methode, die die Eigenschaft zurueckgibt, ob sich eine der Boxen
	 * in einem Deadlock befindet.
	 * 
	 * @return true, wenn sich eine Box in einem Deadlock befindet; false, wenn
	 *         sich keine Box in einem Deadlock befindet.
	 */
	public boolean getContainsDeadlock() {
		return containsDeadlock;
	}

	
	//TODO task 3.2b
	public void findPlayerPos() {
		
	}

	
	//TODO task 5.2b
    public void resetState() {
    	
    }
	
	
	//TODO task 5.2a
	public void previousState() {
		
	}

	
	//TODO task 5.2a
	public void nextState() {
		
	}

	
	//TODO task 3.2c
	public boolean isValidCoordinate(Coordinate pos) {
		return false;
	}

	
	//TODO task 3.2d
	public boolean performAction(Direction direction) {
		return false;
	}

	/**
	 * Aktualisiert das Spielfeld, wenn der Spieler auf die uebergebene neue
	 * Spieler-Position bewegt werden konnte. Steht dem Spieler eine Box im Weg,
	 * wird versucht diese in Richtung der Spielerbewegung zu schieben. Wenn der
	 * Spielzug ausgefuehrt werden konnte (die im Weg stehende Box wurde
	 * verschoben oder der Spieler laeuft ohne eine Box zu verschieben auf die
	 * neue Spieler-Position), wird das neue Spielfeld (mit Spieler an der neuen
	 * Position) an die Liste der Spielzustaende angehaengt und es wird true
	 * zurueckgegeben. Außerdem prueft die Methode, wenn ein Spielzug moeglich
	 * war, ob das Spiel gewonnen wurde oder sich eine Box in einem Deadlock
	 * befindet. Wenn der Spielzug nicht moeglich war (Spieler laeuft gegen eine
	 * Wand oder versucht Box gegen eine Wand/Box zu schieben), bleibt die Liste
	 * der Spielzustaende unveraendert und es wird false zurueckgegeben.
	 * 
	 * @param newPos
	 *            Die neue Spielerposition, wenn der Spielzug gueltg ist und der
	 *            Spieler bewegt werden konnte.
	 * 
	 * @return true, wenn er Spielzug durchgefuehrt werden konnte; false, wenn
	 *         der Spielzug nicht durchfuehrbar war.
	 */
	public boolean updateGrid(Coordinate newPos) {

		GridObject to = grid[newPos.getY()][newPos.getX()];
		Coordinate newBoxPos = computeNewBoxPos(newPos);

		if (to.isMovable() && isValidCoordinate(newBoxPos))
			moveBox(newPos, newBoxPos);

		if (movePlayer(newPos)) {
			statePointer++;

			states.subList(statePointer, states.size()).clear();
			states.add(copyState(grid));

			checkIsWon();
			checkForDeadlocks();
			return true;
		} else {
			return false;
		}
	}

	
	//TODO task 3.2e
	public Coordinate computeNewBoxPos(Coordinate boxPos) {
		return null;
	}

	
	//TODO task 3.2f
	public boolean movePlayer(Coordinate newPos) {
		return false;
	}

	//TODO task 3.2g
	public boolean moveBox(Coordinate oldPos, Coordinate newPos) {
		return false;
	}

	/**
	 * Prueft, ob das Grid-Objekt an der Stelle mit den Koordinaten [x, y] ein
	 * 'stackable'-Objekt ist. Dabei wird der Spieler aus der Betrachtung
	 * ausgeschlossen. Das Feld auf dem der Spieler steht wird somit als
	 * stapelbar angesehen. Werden keine gueltigen Koordinaten uebergeben, ist
	 * das Feld nicht stapelbar.
	 * 
	 * @param x
	 *            Die x-Koordinate
	 * @param y
	 *            Die y-Koordinate
	 * 
	 * @return true, wenn das Feld stapelbar ist; sonst false.
	 */
	public boolean isStackableObject(int x, int y) {
		if (playerPos.getX() == x && playerPos.getY() == y)
			return true;

		return isValidCoordinate(new Coordinate(x, y)) ? grid[y][x].isStackable() : false;
	}

	/**
	 * Prueft, ob eine Box an der Stelle mit den Koordinaten [x, y] in einem
	 * Deadlock steht. Steht die Box in einem Deadlock, wird true zurueckgegben.
	 * Ansonsten wird false zurueckgegeben.
	 * 
	 * @param x
	 *            Die x-Koordinate der Box die ueberprüft werden soll
	 * @param y
	 *            Die y-Koordinate der Box die ueberprüft werden soll
	 * 
	 * @return true, wenn die Box an der Stelle [x, y] in einem Deadlock steht;
	 *         false, wenn nicht.
	 */
	public boolean isBoxInDeadlock(int x, int y) {
		boolean stackableLeft = isStackableObject(x - 1, y);
		boolean stackableTop = isStackableObject(x, y - 1);
        boolean stackableRight = isStackableObject(x + 1, y);
        boolean stackableBottom = isStackableObject(x, y + 1);
        
        boolean movableLeft = grid[y][x-1].isMovable();
        boolean movableTop = grid[y-1][x].isMovable();
        boolean movableRight = grid[y][x+1].isMovable();
        boolean movableBottom = grid[y+1][x].isMovable();

		return !(((stackableLeft || stackableTop) || ((movableLeft || movableTop) && isStackableObject(x - 1, y - 1)))
				&& ((stackableRight || stackableTop) || ((movableRight || movableTop) && isStackableObject(x + 1, y - 1)))
				&& ((stackableRight || stackableBottom) || ((movableRight || movableBottom) && isStackableObject(x + 1, y + 1)))
				&& ((stackableLeft || stackableBottom) || ((movableLeft || movableBottom) && isStackableObject(x - 1, y + 1))));
	}

	/**
	 * Prueft fuer alle Boxen auf dem Spielfeld, ob sie sich mindestens eine in
	 * einem Deadlock befindet. Wenn mindestens eine Box in einem Deadlock
	 * steht, wird die entsprechende Instanzvariable containsDeadlock auf true
	 * gesetzt. Steht keine der Boxen auf dem Spielfeld in einem Deadlock, wird
	 * die Instanzvariable entsprechend auf false gesetzt.
	 */
	public void checkForDeadlocks() {
		for (int y = 0; y < levelHeight; y++) {
			for (int x = 0; x < levelWidth; x++) {
				if (grid[y][x].getType() == GridObjectType.BOX && isBoxInDeadlock(x, y)) {
					this.containsDeadlock = true;
					return;
				}
			}
		}
		this.containsDeadlock = false;
	}

	
	//TODO task 3.2h
	public void checkIsWon() {
		
	}

	
	//TODO task 5.1
	public static boolean isValidLevel(char[][] charGrid) {
		return false;
	}

	/**
	 * Kopiert ein uebergebenes 2-dimensionales GridObject-Array und gibt die
	 * Kopie zurueck.
	 * 
	 * @param gameGrid
	 *            Das 2-dimesionale GridObject-Array das kopiert werden soll
	 * 
	 * @return Die Kopie des 2-dimensionalen GridObject-Arrays
	 */
	public GridObject[][] copyState(GridObject[][] gameGrid) {
		GridObject[][] copyArray = new GridObject[gameGrid.length][gameGrid[0].length];

		for (int i = 0; i < gameGrid.length; i++) {
			System.arraycopy(gameGrid[i], 0, copyArray[i], 0, gameGrid[i].length);
		}
		return copyArray;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < this.levelHeight; y++) {
			for (int x = 0; x < this.levelWidth; x++) {
				sb.append(grid[y][x]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}