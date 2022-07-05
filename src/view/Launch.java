package view;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.StateBasedEntityManager;

/**
 * Launch Klasse, die das Spiel startet und die Zustaende verwaltet
 * 
 * @author Andrej Felde, Daniel Stein, Dominik Renkel, Markus Bommer, Markus Wedel, Tim Lukas Kessel
 *
 */
public class Launch extends StateBasedGame {

	// Spiel-Zustand Indizes
	public static final int MAINMENU_STATE = 0;
	public static final int LEVELSELECTION_STATE = 1;
	public static final int GAMEPLAY_STATE = 2;
	public static final int HIGHSCORE_STATE = 3;
	public static final int CHANGE_SKIN_STATE = 4;

	public Launch() {
		super("Sokoban");
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new MainMenuState(MAINMENU_STATE));
		addState(new LevelSelectionState(LEVELSELECTION_STATE));
		addState(new GamePlayState(GAMEPLAY_STATE));
		addState(new HighscoreState(HIGHSCORE_STATE));
		addState(new ChangeSkinState(CHANGE_SKIN_STATE));

		// Füge Spiel-Zustaende hinzu
		StateBasedEntityManager.getInstance().addState(MAINMENU_STATE);
		StateBasedEntityManager.getInstance().addState(LEVELSELECTION_STATE);
		StateBasedEntityManager.getInstance().addState(GAMEPLAY_STATE);
		StateBasedEntityManager.getInstance().addState(HIGHSCORE_STATE);
		StateBasedEntityManager.getInstance().addState(CHANGE_SKIN_STATE);
	}

	public static void main(String[] args) {
		// OS abhaengige Pfade
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/windows");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/macosx");
		} else {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir") + "/native/" + System.getProperty("os.name").toLowerCase());
		}

		// Starte das Sokoban-Spiel Fenster
		try {
			AppGameContainer app = new AppGameContainer(new Launch());
			app.setDisplayMode(800, 600, false);
			app.setVSync(true);
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}