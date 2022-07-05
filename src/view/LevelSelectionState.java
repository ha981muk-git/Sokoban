package view;

import java.awt.Font;
import java.io.File;
import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import model.IOOperations;

/**
 * Spielzustand, welcher die Levelauswahlanzeige darstellt.
 * 
 * @author Andrej Felde, Dominik Renkel, Markus Bommer, Markus Wedel, Daniel
 *         Stein, Tim Lukas Kessel
 *
 */
public class LevelSelectionState extends BasicGameState {
	
	static char[][] selectedCharGrid;
	static String selectedLevelName;

	private static int entry_count = 29;
	private static final int ENTRIES_PER_PAGE = 9;
	private static final String LEVELS_DIR = "resources/levels/";
	private static final String SELECTION_BACKGROUND = "resources/assets/SelectionBG.png";

	private int stateID;
	private StateBasedEntityManager entityManager;

	private int pageIndex = 0;
	private String[] levels;
	private TrueTypeFont font;
	private TrueTypeFont lvlFont;

	public LevelSelectionState(int stateID) {
		this.stateID = stateID;
		this.entityManager = StateBasedEntityManager.getInstance();
	}
	
	/**
	 * Load next Level, or don't do anything if no Levels are available
	 */
	static void prepareNextLevel()
	{
		prepareLevel(1 + Integer.valueOf(selectedLevelName));
	}
	
	/**
	 * Prepare to load level with indicated ID
	 * 
	 * @param levelID ID of level to load
	 */
	static void prepareLevel(final int levelID)
	{
		//Error cases with variable level count
		if (levelID > entry_count)
			return; 
		
		selectedLevelName = String.format("%02d", levelID);
		
		String levelFileName = String.format("level_%s.txt", selectedLevelName);
		selectedCharGrid = IOOperations.parseLevel(LEVELS_DIR + levelFileName);
	}
	
	@Override
	public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("LevelSelectionState");
		File names = new File(LEVELS_DIR);
		levels = names.list();
		Arrays.sort(levels);
		entry_count = levels.length;
		font = new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false);
		lvlFont = new TrueTypeFont(new Font("Courier New", Font.PLAIN, 35), false);
	}
	
	
	@Override
	public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
			throws SlickException {
		entityManager.renderEntities(gameContainer, stateBasedGame, graphics);
	}

	@Override
	public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {

		entityManager.clearEntitiesFromState(this.stateID);
		addDefaultElements(gameContainer);
		addLevelSelection(gameContainer);
		entityManager.updateEntities(gameContainer, stateBasedGame, delta);
		
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame sb) throws SlickException {
		this.entityManager.clearEntitiesFromState(stateID);
	}

	@Override
	public int getID() {
		return this.stateID;
	}

	/**
	 * Fuegt den Hintergrund und Buttons hinzu, welche von der Auswahl unabhaengig sind
	 * 
	 * @param gameContainer Das Spiel-Fenster von Sokoban
	 * @throws SlickException
	 */
	private void addDefaultElements(GameContainer gameContainer) throws SlickException 
	{
		Entity background = new Entity("Background");
		background.setPosition(new Vector2f(gameContainer.getWidth() / 2, gameContainer.getHeight() / 2));
		background.addComponent(new ImageRenderComponent(new Image(SELECTION_BACKGROUND)));
//		background.setScale(1.104f);
		entityManager.addEntity(stateID, background);

		MenuButton back = new MenuButton("BACK", new Vector2f(10, 10), 0.2f, font);
		ANDEvent andE = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		andE.addAction(new ChangeStateAction(Launch.MAINMENU_STATE));
		back.addComponent(andE);
		entityManager.addEntity(stateID, back);

		MenuButton open = new MenuButton("LOAD",
				new Vector2f(gameContainer.getWidth() - font.getWidth("LOAD") - 10, 10), 0.2f, font);
		ANDEvent openAndEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		openAndEvent.addAction(new OpenFileAction(Launch.GAMEPLAY_STATE));

		open.addComponent(openAndEvent);
		entityManager.addEntity(stateID, open);
	}

	/**
	 * Fuegt Buttons hinzu, die Auswahl des Levels ermoeglichen
	 * 
	 * @param gameContainer Das Spiel-Fenster von Sokoban
	 */
	private void addLevelSelection(GameContainer gameContainer) 
	{
		int buttonY = gameContainer.getHeight() / 2 - font.getHeight("NEXT") / 2;
		int buttonX = gameContainer.getWidth() - font.getWidth("NEXT") - 10;

		if (pageIndex < levels.length / ENTRIES_PER_PAGE) {
			MenuButton nex = new MenuButton("NEXT", new Vector2f(buttonX, buttonY), 0.2f, font);
			ANDEvent nS = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());

			nS.addAction(new Action() {
				@Override
				public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
					pageIndex++;
				}
			});
			nex.addComponent(nS);
			entityManager.addEntity(stateID, nex);
		}

		if (pageIndex > 0) {
			MenuButton prev = new MenuButton("PREV", new Vector2f(10, buttonY), 0.2f, font);
			ANDEvent pS = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
			pS.addAction(new Action() {
				@Override
				public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
					pageIndex--;
				}
			});
			prev.addComponent(pS);
			entityManager.addEntity(stateID, prev);
		}

		int selectionWidth = (gameContainer.getWidth() / 5) * 4;
		int selectionHeight = (gameContainer.getHeight() / 5) * 4;

		int xOffset = gameContainer.getWidth() / 10;
		int yOffset = gameContainer.getHeight() / 10;

		int stringWidth = lvlFont.getWidth("Lvl. 42");
		int whiteSpaceWidth = (selectionWidth - 3 * stringWidth) / 4;

		int stringHeight = lvlFont.getHeight("Lvl. 42");
		int whiteSpaceHeight = (selectionHeight - 3 * stringHeight) / 4;

		int x = 0, y = 0;

		String[] showLevels = getShowableLevels();
		for (int i = 0; i < showLevels.length; i++) {

			if (showLevels[i] == null) {
				continue;
			}

			int levelID = i +1 + pageIndex * ENTRIES_PER_PAGE;
			
			x = xOffset + (i % 3) * stringWidth + (i % 3 + 1) * whiteSpaceWidth;
			y = yOffset + (i / 3) * stringHeight + (i / 3 + 1) * whiteSpaceHeight;
		

			//LevelID mit bereits gezeigten Eintraegen bestimmen
			String lvlButtonName = String.format("Lvl. %02d", levelID);
			
			MenuButton levelButton = new MenuButton(lvlButtonName, new Vector2f(x, y), 0.15f, lvlFont);
			ANDEvent levelClicked = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
			levelClicked.addAction(new ChangeStateAction(Launch.GAMEPLAY_STATE) {

				@Override
				public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
					prepareLevel(levelID);
					super.update(gc, sb, delta, event);
				}
			});
			levelButton.addComponent(levelClicked);
			entityManager.addEntity(stateID, levelButton);
		}
		
	}
	
	/**
	 * Hilfsmethode zur Berechnung der Level, welche auf der entsprechenden
	 * Seitenzahl angezeigt werden sollen. Hierbei wird im Voraus ein Bereich
	 * der Leveluebersicht berechnet, der anschließend in ein angepasstes Array
	 * kopiert wird.
	 * 
	 * @return Ein Array mit den im Verhältnis zur Seitenzahl stehenden Level
	 */
	private String[] getShowableLevels() {
		int begin = pageIndex * ENTRIES_PER_PAGE;
		int length = Math.min(ENTRIES_PER_PAGE, levels.length - begin);
		
		String[] showLevels = new String[ENTRIES_PER_PAGE];
		System.arraycopy(levels, begin, showLevels, 0, length);
		return showLevels;
	}
	
}
