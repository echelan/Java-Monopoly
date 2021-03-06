/*
 * Monopoly Violet - A University Project by Andres Movilla
 * MONOPOLY COPYRIGHT
 * the distinctive design of the gameboard
 * the four corner squares
 * the Mr. Monopoly name and character
 * and each of the distinctive elements of the board
 * are trademarks of Hasbro, Inc.
 * for its property trading game and game equipment.
 * COPYRIGHT 1999 Hasbro, Inc. All Rights Reserved.
 * No copyright or trademark infringement is intended in using Monopoly content on Monopoly Violet.
 */
package monopolyviolet.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import monopolyviolet.control.MouseHandler;
import monopolyviolet.model.Handler;

/**
 *
 * @author Andres
 */
public abstract class Scene {

	protected static final int ssX = (int) (monopolyviolet.model.Handler.SCREEN_SIZE_X);
	protected static final int ssY = (int) (monopolyviolet.model.Handler.SCREEN_SIZE_Y);
	protected final Handler main;
	private final String name;
	private final boolean full;

	public Scene(Handler main, String name, boolean full) {
		this.main = main;
		this.name = name;
		this.full = full;
	}
	
	public void receiveAction(int action, int x, int y) {
		if (action == MouseHandler.EVENT_CLICK) {
			clickEvent(x,y);
		} else if (action == MouseHandler.EVENT_MOVE) {
			moveEvent(x,y);
		} else if (action == MouseHandler.EVENT_DRAG) {
			dragEvent(x,y);
		} else if (action == MouseHandler.EVENT_PRESS) {
			pressEvent(x,y);
		} else if (action == MouseHandler.EVENT_RELEASE) {
			releaseEvent(x,y);
		}
	}

	protected abstract void clickEvent(int x, int y);
	protected abstract void moveEvent(int x, int y);
	protected abstract void dragEvent(int x, int y);
	protected abstract void pressEvent(int x, int y);
	protected abstract void releaseEvent(int x, int y);
	
	public void dispose() {
		main.gameState.remove(main.gameState.size() - 1);
	}

	public abstract BufferedImage getDisplay() throws IOException;

        protected BufferedImage genTextRect(String text, int width, int height, int strokeWidth, Font font, Color strokeColor, Color backColor, Color textColor) {
            
		BufferedImage display = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = display.getGraphics();
                
		g.setColor(strokeColor);
		g.fillRect(0, 0, width, height);
		g.setColor(backColor);
		g.fillRect(strokeWidth, strokeWidth, width-(strokeWidth*2), height-(strokeWidth*2));
		g.setColor(textColor);
		g.setFont(font);
                
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int fontX = (width - metrics.stringWidth(text)) / 2;
		int fontY = ((height - metrics.getHeight()) / 2) + metrics.getAscent();

		g.drawString(text,fontX,fontY);
                
                return display;
        }
        
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the full
	 */
	public boolean isFull() {
		return full;
	}

}
