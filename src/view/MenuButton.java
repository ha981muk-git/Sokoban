package view;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

/**
 * 
 * @author Artur Seitz
 * @author Dennis Schirmer
 * @author Mahmoud El-Hindi
 * @author Darjush Siahdohoni
 * @author Igor Cherepanov
 * @author Hermann Berket
 * @version 1.0
 *
 */
public class MenuButton extends Entity {

	// Text Style
	private TrueTypeFont font;
	private Vector2f labelPosition;
	private static final String MENU_BUTTON_PATH = "resources/assets/MenuButton.png";

	/**
	 * Konstruktor
	 * 
	 * @param entityID
	 *            Die Entity ID
	 */
	public MenuButton(String entityID) {
		super(entityID);
	}

	/**
	 * Konstruktor
	 * 
	 * @param entityID
	 *            Die Entity ID
	 * @param position
	 *            position auf der Leinwand an welcher der Button gezeichnet
	 *            werden soll
	 * @param scale
	 *            Skalierungsfaktor der Bilder
	 * @param font
	 *            Text Style
	 */
	public MenuButton(String entityID, Vector2f position, float scale, TrueTypeFont font) {
		super(entityID);

		labelPosition = position;
		Vector2f entityPosition = new Vector2f(position.getX() + font.getWidth(entityID) / 2,
				position.getY() + font.getHeight(entityID) / 2);
		setPosition(entityPosition);
		setSize(new Vector2f(font.getWidth(entityID), font.getHeight(entityID)));

		this.font = font;

		try {
			addComponent(new ImageRenderComponent(new Image(MENU_BUTTON_PATH)));
		} catch (SlickException e) {
			System.out.println(MENU_BUTTON_PATH + " not found!");
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
		super.render(gameContainer, stateBasedGame, graphics);
		graphics.setFont(font);
		graphics.drawString(getID(), labelPosition.x, labelPosition.y);
	}
}