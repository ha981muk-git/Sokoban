package view;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

/**
 * Spielzustand, der die Hauptmenue-Ansicht graphisch darstellt
 * 
 * @author Andrej Felde, Daniel Stein, Dominik Renkel, Markus Bommer, Markus Wedel, Tim Lukas Kessel
 *
 */
public class MainMenuState extends BasicGameState {

	private int stateID;
	private StateBasedEntityManager entityManager;
	private static final String BG_NAME = "MainMenuBG.png";

	/**
	 * Konstruktor
	 * 
	 * @param stateID
	 *            Die Entity ID
	 */
	public MainMenuState(int stateID) {
		this.stateID = stateID;
		this.entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("MainMenuState");
	}
	
	@Override
	public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException	{	
		
		this.entityManager.clearEntitiesFromState(this.stateID);
		addBackground(gameContainer); 
		addButtons(gameContainer);
	}

	@Override
	public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
			throws SlickException {
		entityManager.renderEntities(gameContainer, stateBasedGame, graphics);
	}

	@Override
	public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
		entityManager.updateEntities(gameContainer, stateBasedGame, delta);
	}

	@Override
	public int getID() {
		return this.stateID;
	}
	
	/**
	 * Fügt dem Fenster einen Hintergrund hinzu
	 * 
	 * @param gameContainer Das Spiel-Fenster von Sokoban
	 * @throws SlickException
	 */
	private void addBackground(GameContainer gameContainer) throws SlickException 
	{
		Entity background = new Entity("Background");
		background.setPosition(new Vector2f(gameContainer.getWidth() / 2, gameContainer.getHeight() / 2));
		background.addComponent(new ImageRenderComponent(new Image(ChangeSkinState.getTexturePath() + BG_NAME)));
		//background.setScale(0.4f);
		entityManager.addEntity(stateID, background);
	}
	
	/**
	 * Bereitet die Buttons vor, mit denen die Optionen im Hauptmenue zur Auswahl geziegt werden
	 * 
	 * @param gameContainer Das Spiel-Fenster von Sokoban
	 */
	private void addButtons(GameContainer gameContainer) {
		String[] str = { "New Game", "Highscore", "Change Skin", "Exit" };


		int xPos = gameContainer.getWidth() / 5;
		float scale = (float) 0.125;
		
		TrueTypeFont font = new TrueTypeFont(new Font("Courier New", Font.BOLD, 40), false);

		MenuButton newGameButton = new MenuButton(str[0], new Vector2f(xPos - font.getWidth(str[0]) / 2, 300), scale, font);
		ANDEvent newGameE = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		newGameE.addAction(new ChangeStateAction(Launch.LEVELSELECTION_STATE));
		newGameButton.addComponent(newGameE);
		entityManager.addEntity(stateID, newGameButton);

		MenuButton hsButton = new MenuButton(str[1], new Vector2f(xPos - font.getWidth(str[1]) / 2, 350), scale, font);
		ANDEvent hsE = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		hsE.addAction(new ChangeStateAction(Launch.HIGHSCORE_STATE));
		hsButton.addComponent(hsE);
		entityManager.addEntity(stateID, hsButton);

		MenuButton changeSkinButton = new MenuButton(str[2], new Vector2f(xPos - font.getWidth(str[2]) / 2, 400), scale, font);
		ANDEvent changeSkinE = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		changeSkinE.addAction(new ChangeStateAction(Launch.CHANGE_SKIN_STATE));
		changeSkinButton.addComponent(changeSkinE);
		entityManager.addEntity(stateID, changeSkinButton);
		
		MenuButton exitButton = new MenuButton(str[3], new Vector2f(xPos - font.getWidth(str[3]) / 2, 450), scale, font);
		ANDEvent exitE = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		exitE.addAction(new QuitAction());
		exitButton.addComponent(exitE);
		entityManager.addEntity(stateID, exitButton);
	}
	
	
}