package view;

import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

/**
 * Spielzustand in dem das Aussehen des Spiels modifiziert werden kann
 * 
 * @author Tim Lukas Kessel
 *
 */
public class ChangeSkinState extends BasicGameState 
{
	private static final String BACKGROUND = "MainMenuBG.png";
	private static final String BASE_PATH = "resources/assets/";
	private static String selection = "sokoban"; //use folder as default
	
	private int stateID;
	private StateBasedEntityManager entityManager;
	
	private String[] options;
	
	/**
	 * Konstruktor des HighscoreStates
	 * 
	 * @param stateID
	 *            Uebergebene Nummer des zu aufrufenden States
	 * 
	 */
	public ChangeSkinState(int stateID) {
		this.stateID = stateID;
		this.entityManager = StateBasedEntityManager.getInstance();
		
		this.options = new String[] {""};
	}
	
	/**
	 * Wird benutzt um den aktuell gewählten Skin zu verwenden
	 * 
	 * @return den Pfad zum Ordner in dem die Texturen liegen die zum gewählten skin passen
	 */
	public static String getTexturePath()
	{
		return BASE_PATH + selection + "/";
	}
	
	@Override
	public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException 
	{
		System.out.println("SkinChangerState");
		
		this.options = getOptionsForSkins();
	}

	@Override
	public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException
	{
		entityManager.clearEntitiesFromState(this.stateID);
		addBackground(gameContainer);
		addSelectionButtons(gameContainer);
	}	

	@Override
	public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException 
	{
		entityManager.renderEntities(gameContainer, stateBasedGame, graphics);
	}

	@Override
	public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException 
	{
		entityManager.updateEntities(gameContainer, stateBasedGame, delta);
		
		entityManager.clearEntitiesFromState(this.stateID);
		addBackground(gameContainer);
		addSelectionButtons(gameContainer);
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
		background.addComponent(new ImageRenderComponent(new Image(getTexturePath() + BACKGROUND)));
		//background.setScale(0.4f);
		this.entityManager.addEntity(stateID, background);
	}

	/**
	 * Bereitet die Buttons vor, mit denen die Optionen für Skins zur Auswahl geziegt werden
	 * 
	 * @param gameContainer Das Spiel-Fenster von Sokoban
	 */
	private void addSelectionButtons(GameContainer gameContainer) 
	{
		final TrueTypeFont buttonFont = new TrueTypeFont(new Font("Courier New", Font.BOLD, 40), false);
		
		final int xPos = gameContainer.getWidth() / 5;
		final int yIncrement = 10 + buttonFont.getHeight();
		final int yPos = gameContainer.getHeight() * 3 / 5 - yIncrement * this.options.length / 2;
		
		//Initialisiere die Texte für die Auswahlmöglichkeiten und den Zurück button
		String[] buttons = new String[this.options.length + 1];
		System.arraycopy(this.options, 0, buttons, 0, this.options.length);		
		buttons[buttons.length -1] = "Back";
		
		//Füge die Buttons mit den Namen der Optionen hinzu
		for (int i = 0; i < buttons.length; i++)
		{
			String name = buttons[i];	//Den Namen speichern, da er auf dem Button groß geschrieben wird 
			String buttonText = name.substring(0, 1).toUpperCase() + name.substring(1);
			Vector2f pos = new Vector2f(xPos - buttonFont.getWidth(buttonText) /2, yPos + i * yIncrement);
			
			if (name.equals(selection))
				buttonText = buttonText + " *";
			
			MenuButton selectionButton = new MenuButton(buttonText, pos, 0.1f, buttonFont);
			ANDEvent andE = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
			
			//Der Back Button wechselt den Status, die anderen updaten welche Auswahl getroffen wurde
			if (i >= buttons.length -1)
				andE.addAction(new ChangeStateAction(Launch.MAINMENU_STATE));
			
			else
				andE.addAction(new Action() {
					
					@Override
					public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
						//der letzte Knopf ist für zurück und soll das Aussehen nicht verändern
						selection = name;
					}
				});

			
			selectionButton.addComponent(andE);

			this.entityManager.addEntity(stateID, selectionButton);
		}
	}

	/**
	 * Diese Methode sucht im Ordner für Texturen nach Ordnern.
	 * Jeder dieser Ordner sollte png Dateien für alle Spielemente beinhalten.
	 * Der Name des Ordners representiert den Anzeigenamen für die Auswahl
	 * 
	 * @return Array Mit allen Skins die zur Auswahl stehen
	 */
	private String[] getOptionsForSkins() 
	{
		File textureFolder = new File(BASE_PATH);
		File files[] = textureFolder.listFiles();
		
		List<String> texture_names = new ArrayList<>();
		
		for (File f : files)
		{
			if (f.isFile())
				continue;
			
			texture_names.add(f.getName());
		}
		
		return texture_names.toArray(new String[0]);
	}

}
