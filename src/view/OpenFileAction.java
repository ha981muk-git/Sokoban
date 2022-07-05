package view;

import java.io.File;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.Component;
import model.Grid;
import model.IOOperations;

/**
 * Aktion, die den "Oeffne-Datei" Dialog ausfuehrt und anzeigt
 * 
 * @author Andrej Felde, Dominik Renkel, Markus Bommer, Markus Wedel
 *
 */
public class OpenFileAction extends ChangeStateAction {

	private static final String LEVELS_DIR = "resources/levels/";

	public OpenFileAction(int newState) {
		super(newState);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("txt files (*.txt)", "txt");
		fc.setFileFilter(xmlfilter);
		fc.showOpenDialog(fc);

		if (fc.getSelectedFile() != null) {
			String path = fc.getSelectedFile().getAbsolutePath();

			LevelSelectionState.selectedCharGrid = IOOperations.parseLevel(path);
			int indexDot = path.lastIndexOf('.');
			if (indexDot == -1)
				indexDot = path.length();

			if (path.contains(File.separator)) {
				LevelSelectionState.selectedLevelName = path.substring(path.lastIndexOf(File.separatorChar) + 1,
						indexDot);
			} else {
				LevelSelectionState.selectedLevelName = path.substring(0, indexDot);
			}

			// Check if the name of the file already exists
			if (searchForName(LevelSelectionState.selectedLevelName)) {
				String msg = "Name of level already exists";
				JOptionPane.showMessageDialog(new JFrame(), msg, "Name exists already", JOptionPane.ERROR_MESSAGE);
			} else if (!Grid.isValidLevel(LevelSelectionState.selectedCharGrid)) {
				GamePlayState.openAction = false;
				String msg = "Check level-size or level-data";
				JOptionPane.showMessageDialog(new JFrame(), msg, "Invalid Level", JOptionPane.ERROR_MESSAGE);
			} else {
				GamePlayState.openAction = true;
				super.update(gc, sb, delta, event);
			}
		}
	}

	/**
	 * Durchsucht die Standard Level auf eventuelle Namenskonflikte
	 * 
	 * @param name
	 *            Name des Levels das geladen werden soll
	 * 
	 * @return true, wenn der Name bereits vorkommt; false, wenn nicht
	 */
	public boolean searchForName(String name) {
		String[] levels = new File(LEVELS_DIR).list();
		return Arrays.asList(levels).contains(name);
	}
}
