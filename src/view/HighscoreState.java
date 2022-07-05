package view;

import java.util.ArrayList;
import java.awt.Font;

import org.newdawn.slick.Color;
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
import model.Highscore;
import model.HighscoreEntry;

/**
 * Klasse die, die graphische Darstellung der HighscoreSeiten regelt. Erzeugt
 * Hintergrund, die Darstellung der einzelnen Highscore-Eintraege und der
 * Buttons.
 * 
 * @author Andrej Felde, Markus Bommer, Tim Lukas Kessel
 */
public class HighscoreState extends BasicGameState {
	private int stateID;
	private StateBasedEntityManager entityManager;
	private static final String HS_BACKGROUND = "resources/assets/PlainBG.png";

	public static String currentLevelID;
	public static Highscore highscore;
	private ArrayList<String> allLevelIDs;

	private static final int MAX_ENTRIES = 10;
	
	private static TrueTypeFont font = new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false);;

	/**
	 * Konstruktor des HighscoreStates
	 * 
	 * @param stateID
	 *            Uebergebene Nummer des zu aufrufenden States
	 * 
	 */
	public HighscoreState(int stateID) {
		this.stateID = stateID;
		entityManager = StateBasedEntityManager.getInstance();
	}

	/**
	 * Methode zur Initialisierung des HighscoreStates legt neuen Highscore an,
	 * falls noch nicht vorhanden
	 */
	@Override
	public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("HighscoreState");
		
		if (highscore == null)
			highscore = new Highscore();
		
		allLevelIDs = highscore.getAllIDs();
		currentLevelID = allLevelIDs.get(0);
	}

	/**
	 * Methode die aufgerufen wird, wenn der HighscoreState betreten wird
	 * Erzeugt sichtbare Elemente auf der Seite
	 */
	@Override
	public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {		
		allLevelIDs = highscore.getAllIDs();
		String[] str = { "BACK", "Previous", "Next" };
		
		TrueTypeFont font = new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false);

		Entity background = new Entity("background");
		background.setPosition(new Vector2f(gameContainer.getWidth() / 2, gameContainer.getHeight() / 2));
		background.addComponent(new ImageRenderComponent(new Image(HS_BACKGROUND)));
		background.setScale(1.104f);
		this.entityManager.addEntity(this.stateID, background);

		MenuButton backButton = new MenuButton(str[0], new Vector2f(10, 10), 0.1f, font);
		ANDEvent andE = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		andE.addAction(new ChangeStateAction(Launch.MAINMENU_STATE));
		backButton.addComponent(andE);
		entityManager.addEntity(stateID, backButton);

		int gap = 20;
		int xMiddle = gameContainer.getWidth() / 2;
		MenuButton prevButton = new MenuButton(str[1], new Vector2f(xMiddle - font.getWidth(str[1]) - gap, 400), 0.25f,
				font);
		ANDEvent andE2 = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		andE2.addAction(new Action() {

			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				int index = allLevelIDs.indexOf(currentLevelID);
				if (index > 0)
					currentLevelID = allLevelIDs.get(index - 1);
			}
		});
		prevButton.addComponent(andE2);
		entityManager.addEntity(stateID, prevButton);

		MenuButton nxtButton = new MenuButton(str[2], new Vector2f(xMiddle + gap, 400), 0.25f, font);
		ANDEvent andE4 = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		andE4.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				int index = allLevelIDs.indexOf(currentLevelID);
				if (index + 1 < allLevelIDs.size())
					currentLevelID = allLevelIDs.get(index + 1);
			}
		});
		nxtButton.addComponent(andE4);
		entityManager.addEntity(stateID, nxtButton);
	}

	@Override
	public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
			throws SlickException {
		this.entityManager.renderEntities(gameContainer, stateBasedGame, graphics);
	
		graphics.setFont(font);
		graphics.setColor(Color.red);
		String[] str = { "Highscore Level " + currentLevelID, "PLATZ   ZÜGE | ZEIT" };

		graphics.drawString(str[0], (gameContainer.getWidth() - font.getWidth(str[0])) / 2, 10);

		int leftOffset = (gameContainer.getWidth() - font.getWidth(str[1])) / 2;
		graphics.drawString(str[1], leftOffset, 75);
		graphics.setColor(Color.white);

		for (int i = 0; i < MAX_ENTRIES; i++) {
			if (i < highscore.getHighscore(currentLevelID).size()) {
				HighscoreEntry entry = highscore.getHighscore(currentLevelID).get(i);
				String s = String.format("%4d:%7s | %s", i + 1, entry.getMoveCount(), entry.getTime());
				graphics.drawString(s, leftOffset, 100 + (25 * i));
			} else
				graphics.drawString(String.format("%4d:%9s", (i + 1), "/"), leftOffset, 100 + (25 * i));
		}
	}

	@Override
	public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
		this.entityManager.updateEntities(gameContainer, stateBasedGame, delta);
	}

	@Override
	public void leave(GameContainer gc, StateBasedGame sb) throws SlickException {
		this.entityManager.clearEntitiesFromState(stateID);
	}

	@Override
	public int getID() {
		return stateID;
	}
}