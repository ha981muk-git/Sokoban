package view;

import java.awt.Font;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
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
import model.Direction;
import model.Grid;
import model.GridObjectType;
import model.HighscoreEntry;
import model.InvalidLevelException;

/**
 * Spiel-Zustand der die Spiellogik graphisch darstellt und die in model
 * beschriebenen Elemente umsetzt.
 * 
 * @author Andrej Felde, Daniel Stein, Dominik Renkel, Markus Bommer, Markus Wedel, Tim Lukas Kessel
 *
 */
public class GamePlayState extends BasicGameState {
	private int stateID;
	private StateBasedEntityManager entityManager;

	private Grid grid;
	public static boolean openAction = false;

	public static int time;
	private static final int MAX_LEVEL_WIDTH = 20 * 32;
	private static final int MAX_LEVEL_HEIGHT = 12 * 32;

	private float tileSize;
	private static final float MIN_TILE_SIZE = 16;

	private static final TrueTypeFont font = new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false);
	private static final TrueTypeFont winnerFont = new TrueTypeFont(new Font("Courier New", Font.BOLD, 40), false);
	private static final TrueTypeFont lvlFont = new TrueTypeFont(new Font("Courier New", Font.BOLD, 30), false);

	private HashMap<GridObjectType, Image> imageMap;

	private MenuButton highscoreButton;
	private MenuButton backButton;
	private MenuButton nextButton;
	
	private boolean hasDrawnWinFrame = false;
	
	
	public GamePlayState(int stateID) {
		this.stateID = stateID;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("GamePlay State");
	}

	@Override
	public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
		gameContainer.getInput().clearKeyPressedRecord();

		//für ein neues Level muss hasDrawnWinFrame resetet werden
		this.hasDrawnWinFrame = false;
		
		// Lade das ausgewaehlte Level
		try {
			this.grid = new Grid(LevelSelectionState.selectedCharGrid);
		} catch (InvalidLevelException e) {
			String msg = "Check level-size or level-data";
			JOptionPane.showMessageDialog(new JFrame(), msg, "Invalid Level", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		int levelWidth = grid.getLevelWidth();
		int levelHeight = grid.getLevelHeight();
		GamePlayState.time = 0;

		// Berechne Feldgroeße (abhaengig von der Levelgroeße)
		int tileSizeWidth = MAX_LEVEL_WIDTH / levelWidth;
		int tileSizeHeight = MAX_LEVEL_HEIGHT / levelHeight;

		this.tileSize = Math.max(Math.min(tileSizeWidth, tileSizeHeight), MIN_TILE_SIZE);

		initializeImageMap();
		createButtons(gameContainer);
		addDefaultEntities();
		updateGameBoard(gameContainer);
	}
	

	@Override
	public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
			throws SlickException {

		graphics.setFont(font);

		// Zeichne verschiedene Anzeigen auf den Bildschirm wenn das Spiel
		// gewonnnen wurde
		
		if (this.grid.getIsWon()) {
			
			graphics.drawString(secondsInClockFormat(GamePlayState.time / 1000), gameContainer.getWidth() - 75, 10);

			graphics.setFont(winnerFont);
			graphics.drawString("GEWONNEN", gameContainer.getWidth() / 2 - winnerFont.getWidth("GEWONNEN") / 2, 30);

			String msg = "in " + this.grid.getStepCount() + " Schritten";
			graphics.drawString(msg, gameContainer.getWidth() / 2 - winnerFont.getWidth(msg) / 2,
					30 + winnerFont.getHeight("GEWONNEN"));
			
			
		} else if (this.grid.getContainsDeadlock()) {

			// Zeichne Deadlock Anzeige auf den Bildschirm, wenn ein Deadlock
			// auftritt
			graphics.drawString(secondsInClockFormat(GamePlayState.time / 1000), gameContainer.getWidth() - 75, 10);

			graphics.drawString("Deadlock detected",
					gameContainer.getWidth() / 2 - font.getWidth("Deadlock detected") / 2, 30);
		} else {

			// Zeichne Standard Level anzeigen
			graphics.setFont(lvlFont);

			String lvlName = "Lvl. " + LevelSelectionState.selectedLevelName;
			graphics.drawString(lvlName, gameContainer.getWidth() / 2 - lvlFont.getWidth(lvlName) / 2, 30);

			String stepCount = grid.getStepCount() + "";
			graphics.drawString(stepCount, gameContainer.getWidth() / 2 - lvlFont.getWidth(stepCount) / 2,
					30 + lvlFont.getHeight(lvlName));

			graphics.setFont(font);
			graphics.drawString(secondsInClockFormat(GamePlayState.time / 1000), gameContainer.getWidth() - 75, 10);
		}
		this.entityManager.renderEntities(gameContainer, stateBasedGame, graphics);
	}

	@Override
	public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
		
		// Tastatur-Eingaben nur beruecksichtigen wenn das Spiel noch nicht
		// gewonnen ist
		if (!this.grid.getIsWon()) {

			// Zuruecksetzten der Zeit, falls Datei laden Dialog geoeffnet war
			if (openAction) {
				GamePlayState.time = 0;
				GamePlayState.openAction = false;
			} else {
				GamePlayState.time += delta;
			}

			// Tastatur-Eingaben ueberpruefen und Spielzuege ausfuehren
			boolean changed = false;
			Input input = gameContainer.getInput();

			if (input.isKeyPressed(Input.KEY_UP)) {
				changed = this.grid.performAction(Direction.UP);
			}

			if (input.isKeyPressed(Input.KEY_DOWN)) {
				changed = this.grid.performAction(Direction.DOWN);
			}

			if (input.isKeyPressed(Input.KEY_LEFT)) {
				changed = this.grid.performAction(Direction.LEFT);
			}

			if (input.isKeyPressed(Input.KEY_RIGHT)) {
				changed = this.grid.performAction(Direction.RIGHT);
			}

			if (input.isKeyPressed(Input.KEY_U)) {
				changed = true;
				this.grid.previousState();
			}

			if (input.isKeyPressed(Input.KEY_R)) {
				changed = true;
				this.grid.nextState();
			}
			if(input.isKeyPressed(Input.KEY_X)) {
				changed = true;
				resetGameplayState();
			}

			// Tastatur Eingabe -> zeichne Spielfeld neu
			if (changed) {
				updateGameBoard(gameContainer);
			}
		}
		
		//adding Buttons once after game is won
		else if (!this.hasDrawnWinFrame)
		{
			this.entityManager.addEntity(this.stateID, this.highscoreButton);
			this.entityManager.addEntity(this.stateID, this.nextButton);
			
			this.hasDrawnWinFrame = true;
		}
			
		this.entityManager.updateEntities(gameContainer, stateBasedGame, delta);
	}
	
	private void resetGameplayState() {
		time = 0; 
		this.grid.resetState();
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
	 * Zeichne das Spielfeld auf dem Bildschirm anhand des internen
	 * Spielfeld-Modells.
	 * 
	 * @param gameContainer
	 *            Das Spiel-Fenster von Sokoban
	 * 
	 * @throws SlickException
	 */
	private void updateGameBoard(GameContainer gameContainer) throws SlickException {
		this.entityManager.clearEntitiesFromState(this.stateID);
		addDefaultEntities();
		for (int y = 0; y < grid.getLevelHeight(); y++) {
			for (int x = 0; x < grid.getLevelWidth(); x++) {
				createEntity(gameContainer, x, y, grid.getGridObjectAt(x, y).getType());
			}
		}
	}
	
	/**
	 * Bereitet alle Buttons vor, die auf dem Spielfenster gezeigt werden
	 * 
	 * @param gameContainer Das Spiel-Fenster von Sokoban
	 */
	private void createButtons(final GameContainer gameContainer) {
		
		final String hsButton  = "Highscore";
		final String bckButton = "BACK";
		final String nxtButton = "Next Level";
		
		// Back Button
		this.backButton = new MenuButton(bckButton, new Vector2f(10, 10), 0.1f, font);
		ANDEvent clickedBack = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		clickedBack.addAction(new ChangeStateAction(Launch.LEVELSELECTION_STATE));
		this.backButton.addComponent(clickedBack);
		
		
		float midGap = 30.0f;
		float south_x = gameContainer.getWidth() / 2;
		float south_y = gameContainer.getHeight() - 30;
		
		
		// Next Level Button
		this.nextButton = new MenuButton(nxtButton, 
				new Vector2f(south_x  + midGap
							, south_y - font.getHeight(nxtButton)), 0.25f, font);
		ANDEvent clickedNext = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		clickedNext.addAction(new ChangeStateAction(Launch.GAMEPLAY_STATE));
		clickedNext.addAction(new Action() {
			
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				
				if (grid.getStepCount() > 0)
				{
					HighscoreState.highscore.addEntry(new HighscoreEntry(LevelSelectionState.selectedLevelName,
							grid.getStepCount(), time / 1000));
					HighscoreState.currentLevelID = LevelSelectionState.selectedLevelName;
				}
				
				LevelSelectionState.prepareNextLevel();
			}
		});
		this.nextButton.addComponent(clickedNext);
		
		
		//Display highscore Button
		this.highscoreButton = new MenuButton(hsButton,
				new Vector2f(south_x  - font.getWidth(hsButton) - midGap
							, south_y - font.getHeight(hsButton)), 0.25f, font);
		ANDEvent clickedHighscore = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		clickedHighscore.addAction(new ChangeStateAction(Launch.HIGHSCORE_STATE));
		
		// Erstelle den Highscore
		clickedHighscore.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				HighscoreState.highscore.addEntry(new HighscoreEntry(LevelSelectionState.selectedLevelName,
						grid.getStepCount(), time / 1000));
				HighscoreState.currentLevelID = LevelSelectionState.selectedLevelName;
			}
		});
		this.highscoreButton.addComponent(clickedHighscore);
	}

	/**
	 * Fuege dem Entitaeten-Verwalter die Standard-Entitaeten hinzu
	 */
	private void addDefaultEntities() {
		this.entityManager.addEntity(this.stateID, this.backButton);
	}
	
	/**
	 * Lade Bilddateien und speichere sie in einer HashMap. (Aus Performance Gründen)
	 * 
	 * @throws SlickException
	 */
	private void initializeImageMap() throws SlickException {
		final String basePath = ChangeSkinState.getTexturePath();
		
		this.imageMap = new HashMap<GridObjectType, Image>(7);
		this.imageMap.put(GridObjectType.BOX,			 new Image(basePath + "box.png"));
		this.imageMap.put(GridObjectType.BOX_ON_GOAL,	 new Image(basePath + "boxongoal.png"));
		this.imageMap.put(GridObjectType.PLAYER,		 new Image(basePath + "player.png"));
		this.imageMap.put(GridObjectType.PLAYER_ON_GOAL, new Image(basePath + "playerongoal.png"));
		this.imageMap.put(GridObjectType.FIELD,			 new Image(basePath + "floor.png"));
		this.imageMap.put(GridObjectType.GOAL,			 new Image(basePath + "goal.png"));
		this.imageMap.put(GridObjectType.WALL,			 new Image(basePath + "wall.png"));
	}

	/**
	 * Erstelle eine Entitaet auf dem Bildschirm an der gegebenen Position [x,
	 * y]. Das Bild der Entitaet haengt von dem uebergebenen Spielfeld Typ ab.
	 * 
	 * @param gameContainer
	 *            Das Spiel-Fenster von Sokoban
	 * @param x
	 *            Die x-Position an der Entität auf dem Spielfeld
	 * @param y
	 *            Die y-Position an der Entität auf dem Spielfeld
	 * @param mapKey
	 *            Der Schluessel für die HashMap um das Bild der Entitaet zu
	 *            finden
	 */
	private void createEntity(GameContainer gameContainer, int x, int y, Enum<GridObjectType> mapKey) {
		Entity entity = new Entity(mapKey + " at Pos: " + x + ", " + y);
		entity.setPosition(getPositionVector(gameContainer, x, y));
		entity.addComponent(new ImageRenderComponent(this.imageMap.get(mapKey)));

		// Setze die Skalierung auf "Skalierungsfaktor - Luecke"
		entity.setScale(1 / (Math.max(entity.getSize().getX(), entity.getSize().getY()) / this.tileSize) - 0.04f);
		this.entityManager.addEntity(stateID, entity);
	}

	/**
	 * 
	 * Berechne die Position der Spielfeld Entitaeten auf dem Bildschirm anhand
	 * ihrer Koordinaten.
	 * 
	 * @param gameContainer
	 *            Das Spiel-Fenster von Sokoban
	 * @param x
	 *            Die x-Position der Entität
	 * @param y
	 *            Die y-Position der Entität
	 * 
	 * @return Die Position der Entitaet auf dem Bildschirm.
	 */
	private Vector2f getPositionVector(GameContainer gameContainer, int x, int y) {
		float topOffset = 25;
		float xOffset = (gameContainer.getWidth() - (this.grid.getLevelWidth() - 1) * this.tileSize) / 2;
		float yOffset = (gameContainer.getHeight() - (this.grid.getLevelHeight() - 1) * this.tileSize) / 2 + topOffset;

		return new Vector2f(xOffset + x * this.tileSize, yOffset + y * this.tileSize);
	}

	/**
	 * Wandle uebergebene Sekunden in Minuten:Sekunden-Format um.
	 * 
	 * @param sec
	 *            Die Sekunden die im Spiel verbracht wurden
	 * 
	 * @return min:sek Repraesentation der Sekunden.
	 */
	private String secondsInClockFormat(int seconds) {
		return String.format("%02d:%02d", (seconds % 3600) / 60, (seconds % 60));
	}

}