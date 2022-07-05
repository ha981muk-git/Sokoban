package model;

/**
 * Klasse die einen Highscore-Eintrag erzeugt und repraesentiert ein
 * Highscore-Eintrag bestehend aus der ID des gespielten Levels, die benoetigten
 * Zuege und die benoetigte Zeit. Gibt die Moeglichkeit einzelne
 * Highscore-Eintraege untereinander zu vergleichen und im Stringformat zu
 * erzeugen.
 * 
 * @author Andrej Felde, Steven Arzt, Oren Avni, Markus Bommer
 * 
 */
public class HighscoreEntry implements Comparable<HighscoreEntry> {
	private String levelID;
	private int moveCount;
	private int seconds;

	
	//TODO task 4b
	public HighscoreEntry(String levelID, int moveCount, int seconds) throws IllegalArgumentException {
		
	}

	
	//TODO task 4c
	public HighscoreEntry(String data) throws IllegalArgumentException, NumberFormatException {
		
	}

	
	//TODO task 4a
	public void validate(int moveCount, int seconds) throws IllegalArgumentException {
		
	}

	//TODO task 4d
	@Override
	public boolean equals(Object obj) {
		return false;
	}

	/**
	 * Getter-Methode der LevelID. Gibt die eindeutige Identifikationsstring des
	 * Levels wieder, für welches der Highscore-Eintrag gespeichert werden soll.
	 * 
	 * @return returnt die eindeutige LevelID als String
	 */
	public String getLevelID() {
		return levelID;
	}

	/**
	 * Getter Methode der Zuege. Gibt die Anzahl der benoetigten Schritte des
	 * Higscore-Eintrags wieder.
	 * 
	 * @return die benoetigten Schritte des Levels als Integer
	 */
	public int getMoveCount() {
		return moveCount;
	}

	/**
	 * 
	 * Getter-Methode der Zeit. Gibt die benoetigte Zeit des Higscore-Eintrags
	 * wieder.
	 * 
	 * @return die benoetigten Sekunden dieses Highscore-Eintrags als Integer
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * Wird dazu genutzt die Zeit im "mm:ss"-Format anzuzeigen.
	 * 
	 * @return Eine Stringrepraesentation der benoetigten Zeit im Format "mm:ss".
	 */
	public String getTime() {
		return String.format("%02d:%02d", (seconds % 3600) / 60, (seconds % 60));
	}

	
	//TODO task 4e
	@Override
	public int compareTo(HighscoreEntry o) {
		return 0;
	}

	@Override
	public String toString() {
		return levelID + "-" + moveCount + "-" + seconds;
	}
}