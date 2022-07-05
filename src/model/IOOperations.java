package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Klasse zum Lesen und Schreiben von Dateien. Dies wird insbesondere zum Lesen
 * und Schreiben der Highscore-Datei sowie der einzelnen Level genutzt.
 * 
 * @author Andrej Felde
 *
 */
public class IOOperations {

	/**
	 * Diese Methode wird dazu genutzt, um eine Datei auszulesen.
	 * 
	 * @param file
	 *            Dateipfad, welcher in der Form eines Strings uebergeben wird
	 * 
	 * @return Den String des Inhaltes der uebergebenen Leveldatei, sofern diese
	 *         gefunden werden kann
	 * 
	 */
	public static String readFile(String file) {
		StringBuffer sb = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line + "\n");
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: Can't find " + file);
			return null;
		} catch (IOException e) {
			System.err.println("IOException: Error with file " + file);
			return null;
		}
		return sb.toString();
	}

	/**
	 * Diese Methode wird dazu genutzt um den Highscore in einer Datei zu
	 * speichern. Allgemein kann die Methode einen beliebigen String in einer
	 * Datei speichern.
	 * 
	 * @param file
	 *            Ein in einem String uebergebener Dateipfad, in welchen
	 *            geschrieben werden soll.
	 * @param dataString
	 *            Der Inhalt, welcher in den angegebenen Pfad geschrieben werden
	 *            soll.
	 * 
	 * @return true, wenn die Datei erstellt bzw. in sie beschrieben werden
	 *         konnte; ansonsten false.
	 */
	public static boolean writeFile(String file, String dataString) {
		Path path = FileSystems.getDefault().getPath(file);
		try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			bw.write(dataString);
		} catch (IOException e) {
			System.err.println("IOException: Can't write file: " + file);
			return false;
		}
		return true;
	}

	/**
	 * Liest eine Level.txt und gibt eine zweidimensionales char-Array zurueck,
	 * welches das Level repraesentiert.
	 * 
	 * @param file
	 *            Textdatei, die das zu modellierende Level beschreibt
	 * 
	 * @return Ein 2D-Chararray, welches die uebersetzte Leveldatei mit einer
	 *         umfassenden Wand enthaelt, sofern die Textdatei gefunden wurde;
	 *         sonst null.
	 */
	public static char[][] parseLevel(String file) {
		int maxWidth = -1;
		ArrayList<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > maxWidth)
					maxWidth = line.length();
				lines.add(line);
			}
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: Can't find " + file);
			return null;
		} catch (IOException e) {
			System.err.println("IOException: Error with file " + file);
			return null;
		}

		// add surrounding walls
		char[][] resultArray = new char[lines.size()][maxWidth];
		boolean[] topWallFound = new boolean[maxWidth];
		boolean[] bottomWallFound = new boolean[maxWidth];
		for (int y = 0; y < resultArray.length; y++) {
			String line = lines.get(y);
			for (int x = 0; x < resultArray[0].length; x++) {
				// top
				if (x < line.length() && line.charAt(x) == '#') {
					topWallFound[x] = true;
				}

				// left / right
				if (x < line.indexOf('#') || x >= line.length() || !topWallFound[x])
					resultArray[y][x] = '#';
				else
					resultArray[y][x] = line.charAt(x);
			}
		}

		// bottom
		for (int y = resultArray.length - 1; y >= 0; y--) {
			for (int x = resultArray[0].length - 1; x >= 0; x--) {
				char[] line = resultArray[y];
				if (line[x] == '#')
					bottomWallFound[x] = true;

				if (!bottomWallFound[x])
					resultArray[y][x] = '#';
			}
		}
		return resultArray;
	}
}