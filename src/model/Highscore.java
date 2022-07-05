package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Klasse, welche für die Verwaltung der Highscores verantworlicht ist. Laedt,
 * speichert und legt neue Highscore-Eintraege in einer ArrayList an und
 * speichert die ArrayListen zusammen mit dem Key des passenden Levels in eine
 * Hashmap
 *
 * @author Andrej Felde, Markus Bommer
 *
 */
public class Highscore {
	// maximale Anzahl an Highscore-Einträgen in einer Highscore-Liste pro Level
	private static final int MAX_ENTRIES = 10;
	// Pfad der Highscoredatei für externe Level
	private static final String HIGHSCORE_FILE = "resources/highscores.hs";
	// Pfad der vorhandenen Level
	private static final String LEVELS_DIR = "resources/levels/";

	private final HashMap<String, ArrayList<HighscoreEntry>> highscores;

	/**
	 * Konstruktor der Highscore-Klasse verantwortlich für das Erzeugen eines
	 * Highscore-Objektes. Legt für jedes Level einen Eintrag in der Hashmap an.
	 * Wurden die vorherigen Highscores noch nicht geladen ruft diese Methode
	 * initHighscore auf.
	 */
	public Highscore() {
		highscores = new HashMap<>();

		// Highscore für die regulaeren Level erstellen
		File names = new File(LEVELS_DIR);
		String[] levels = names.list();
		Arrays.sort(levels);

		for (String level : levels) {
			String lvlID = level.substring(level.indexOf("_") + 1, level.indexOf("."));
			highscores.put(lvlID, new ArrayList<>());
		}

		// Alle Highscore-Eintraege eintragen
		String hsData = IOOperations.readFile(HIGHSCORE_FILE);
		if (!(hsData == null || hsData.isEmpty()))
			initHighscore(hsData);
	}

	/**
	 * Methode zum Erzeugen der Highscores aus dem gespeicherten Stringformat.
	 * Teilt den uebergebenen String in einzelne Eintraege und speichert diese in
	 * einem String-Array. Danach werden die einzelnen Eintraege in der Hashmap, in
	 * die passenden Arraylists ihres Levels einsortiert. Gibt es noch keine
	 * ArrayList zu dem Level, wird diese angelegt und der Eintrag dort
	 * gespeichert.
	 *
	 * @param str
	 *            String, der die vorherigen Highscores enthaelt
	 */
	private void initHighscore(String str) {
		String[] lines = str.split("\\r?\\n");
		for (String line : lines) {
			HighscoreEntry entry = new HighscoreEntry(line);
			if (highscores.containsKey(entry.getLevelID()))
				highscores.get(entry.getLevelID()).add(entry);
			else {
				ArrayList<HighscoreEntry> list = new ArrayList<>();
				list.add(entry);
				String id = entry.getLevelID();
				highscores.put(id == null ? "" : id, list);
			}
		}
	}

	/**
	 * Methode zum Speichern der Highscores in der Highscore-File. Bringt die
	 * einzelnen Highscore-Eintraege ins Stringformat und schreibt diese in eine
	 * Textdatei in der sie dann gespeichert werden.
	 */
	private void saveToFile() {
		StringBuilder dataString = new StringBuilder();
		for (ArrayList<HighscoreEntry> list : highscores.values()) {
			for (HighscoreEntry entry : list) {
				dataString.append(entry.toString()).append("\n");
			}
		}
		IOOperations.writeFile(HIGHSCORE_FILE, dataString.toString());
	}

	/**
	 * Getter Methode für Highscores einzelner Level
	 *
	 * @param levelID
	 *            IdentifikationsID des Levels, dessen Highscore man
	 *            erhalten moechte.
	 * @return Gibt die ArrayList des gewuenschten Levels zurueck.
	 */
	public ArrayList<HighscoreEntry> getHighscore(String levelID) {
		return highscores.get(levelID);
	}

	/**
	 * Getter-Methode um alle LevelIDs in geordneten Reihenfolge wiederzugeben,
	 * fuer die es bereits eine ArrayList für Highscore-Eintraege gibt.
	 *
	 * @return Alle Level-IDs für die es schon eine ArrayList gibt.
	 */
	public ArrayList<String> getAllIDs() {
		ArrayList<String> ids = new ArrayList<>(highscores.keySet());
		Collections.sort(ids);
		return ids;
	}

	/**
	 * Methode welche einen Highscore-Eintrag in die passende ArrayList in der
	 * Hashmap einfuegt und die ArrayList nach den besten Highscores sortiert.
	 *
	 * @param entry
	 *            Hinzuzufuegender Highscore-Eintrag
	 */
	public void addEntry(HighscoreEntry entry) {
		ArrayList<HighscoreEntry> list = highscores.get(entry.getLevelID());
		if (list == null)
			list = new ArrayList<>();

		list.add(entry);
		Collections.sort(list);
		highscores.put(entry.getLevelID(), new ArrayList<>(list.subList(0, Math.min(list.size(), MAX_ENTRIES))));
		saveToFile();
	}
}