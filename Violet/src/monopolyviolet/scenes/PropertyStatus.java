/*
 *  Monopoly Violet - A University Project by Andres Movilla
 *  MONOPOLY COPYRIGHT
 *  the distinctive design of the gameboard
 *  the four corner squares
 *  the Mr. Monopoly name and character
 *  and each of the distinctive elements of the board
 *  are trademarks of Hasbro, Inc.
 *  for its property trading game and game equipment.
 *  COPYRIGHT 1999 Hasbro, Inc. All Rights Reserved.
 *  No copyright or trademark infringement is intended in using Monopoly content on Monopoly Violet.
 */
package monopolyviolet.scenes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import monopolyviolet.model.Button;
import monopolyviolet.model.Handler;
import monopolyviolet.model.Node;
import monopolyviolet.model.Place;
import monopolyviolet.model.Player;

/**
 *
 * @author Andres
 */
public class PropertyStatus extends Scene {

    private Player player;
	private Player owner;
    private Place place;
    private Node<Button> buttons;
    private int selected;
	private int amount;
        
    public PropertyStatus(Handler main, Place place, Player player) {
        super(main, "PROPERTY", false);
        this.place = place;
        this.player = player;
        this.selected = -1;
        buttons = new Node();
		
        if (place.getProperty().getOwner() == -1) {
			Button newButton;
			
			newButton = new Button(50, 210, 200, 40);
			newButton.setText("Buy property");
			newButton.setInternalName("BUY");
			newButton.setEnabled(main.getPlayerWorth(player) >= place.getProperty().getBuyPrice());
			buttons.add(newButton);

			newButton = new Button(50, 280, 200, 40);
			newButton.setText("Back");
			newButton.setInternalName("BACK");
			buttons.add(newButton);
			
        } else if (!place.getProperty().isMortgaged()) {
			Button newButton;
			
			newButton = new Button(50, 280, 200, 40);
			newButton.setText("Pay Rent");
			newButton.setInternalName("RENT");
			buttons.add(newButton);
			
			this.owner = main.findPlayerWithID(place.getProperty().getOwner());
			this.amount = place.getProperty().getRent();
			if (place.getType() == Place.UTILITY_TYPE) {
				this.amount = this.amount * player.getLastRoll();
			}
			
			if (owner == player) {
				buttons.get(0).setText("Back");
				buttons.get(0).setInternalName("BACK");
			}
			
		} else {
			Button newButton;
			
			newButton = new Button(50, 280, 200, 40);
			newButton.setText("Back");
			newButton.setInternalName("BACK");
			buttons.add(newButton);
		}
    }

    @Override
    protected void clickEvent(int x, int y) {
		if (selected != -1) {
			if (buttons.get(selected).isEnabled()) {
				String internalName = buttons.get(selected).getInternalName();
				
				if (internalName.compareTo("BACK") == 0) {
					this.dispose();
				} else if (internalName.compareTo("BUY") == 0) {
					buyProperty();
				} else if (internalName.compareTo("RENT") == 0) {
					this.dispose();
					payRent();
				}
			}
		}
    }
	
	public void bought() {
		if (place.getProperty().getOwner() == -1) {
			
			this.dispose();
			
			place.getProperty().setOwnerName(player.getName());
			place.getProperty().setOwner(player.getId());
			place.getProperty().setOwnerColor(player.getColor());
			
			try {
				main.getGame().buildMapDisplay();
			} catch (IOException ex) {
			}

			main.refreshMonopolies();
			
		}
	}
	
	private void buyProperty() {
		
		main.gameState.add(new HandleAmount(main, place.getProperty().getBuyPrice(), player, null, true, true));

	}
	
	private void payRent() {
		int monopolyBonus = place.getProperty().getMonopolyBonus();
		if (place.getType() == Place.PROPERTY_TYPE) {
			if (place.getProperty().getNumHouses() != 0) {
				monopolyBonus = 1;
			}
		}
		main.gameState.add(new HandleAmount(main, amount*monopolyBonus, player, owner, true, false));

	}
    
    @Override
    protected void moveEvent(int x, int y) {

        int placement = -1;
        int counter = 0;

        while (counter < buttons.size()) {
			if (buttons.get(counter).isContained(x, y)) {
				placement = counter;
				buttons.get(counter).setHovered(true);
			} else {
				buttons.get(counter).setHovered(false);
			}
			counter = counter + 1;
        }

        selected = placement;
    }

    @Override
    protected void dragEvent(int x, int y) {

    }

    @Override
    protected void pressEvent(int x, int y) {

    }

    @Override
    protected void releaseEvent(int x, int y) {

    }

    @Override
    public BufferedImage getDisplay() throws IOException {
        BufferedImage display = new BufferedImage(ssX, ssY, BufferedImage.TYPE_INT_ARGB);
        Graphics g = display.getGraphics();

		int counter = 0;
		while (counter < buttons.size()) {
			Button thisButton = buttons.get(counter);
			g.drawImage(thisButton.getDisplay(),thisButton.getX(),thisButton.getY(), null);
			counter = counter + 1;
		}

		BufferedImage propertyCard = place.getProperty().getPropertyCard();

		g.drawImage(propertyCard, ssX-300, (ssY-propertyCard.getHeight())/2, null);
		
        return display;
    }
	
}
