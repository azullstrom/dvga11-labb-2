import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.toedter.calendar.JCalendar;

public class Controller implements ActionListener, MouseListener {
	
	private Model model;
	private View view;
	private ImageIcon icon;

	public Controller(Model model) {
		this.model = model;
	}
	
	public void setView(View view) {
		this.view = view;
	}

	// ActionListener
	@Override
	public void actionPerformed(ActionEvent e) {
		// Book
		if(e.getActionCommand() == "roundBook") {
			/*if(view.getRoundTableText(model.getMarkedTableNumber() - 1).contains("Namn, antal personer")) {
				JOptionPane.showMessageDialog(view, "Skriv ett namn med minst 3 bokstäver och antal gäster 1-6.", "Felaktig inmatning", JOptionPane.INFORMATION_MESSAGE);
				return;
			}*/
			if(!view.guestNotSelected()) {
				view.dequeueList();
			}
			icon = new ImageIcon("images/round-tables/round-red-table-" + model.getMarkedTableNumber() + ".png");
			model.increaseAmountOfBookings();
			view.setRoundTableStatus(icon, model.getMarkedTableNumber() - 1, model.getNewTableName());
			view.disableBookButton();
			view.setTablesVisible(true);
			view.setSearchButtonActionCommand("search");
			//view.setRoundTableTextEnabled(false, model.getMarkedTableNumber() - 1);
			model.setGreenState(TableState.Unmarked);
			model.setRedState(TableState.Unmarked);
		} else if(e.getActionCommand() == "squareBook") {
			if(!view.guestNotSelected()) {
				view.dequeueList();
			}
			icon = new ImageIcon("images/square-tables/square-red-table-" + model.getMarkedTableNumber() + ".png");
			view.setSquareTableStatus(icon, model.getMarkedTableNumber() - 7, model.getNewTableName());
			model.increaseAmountOfBookings();
			view.disableBookButton();
			view.setTablesVisible(true);
			view.setSearchButtonActionCommand("search");
			model.setGreenState(TableState.Unmarked);
			model.setRedState(TableState.Unmarked);
		}
		// Unbook
		else if(e.getActionCommand() == "roundUnbook") {
			icon = new ImageIcon("images/round-tables/round-green-table-" + model.getMarkedTableNumber() + ".png");
			model.decreaseAmountOfBookings();
			view.setRoundTableStatus(icon, model.getMarkedTableNumber() - 1, model.getNewTableName());
			view.disableUnbookButton();
			//view.setRoundTableTextEnabled(true, model.getMarkedTableNumber() - 1);
			model.setGreenState(TableState.Unmarked);
			model.setRedState(TableState.Unmarked);
		} else if(e.getActionCommand() == "squareUnbook") {
			icon = new ImageIcon("images/square-tables/square-green-table-" + model.getMarkedTableNumber() + ".png");
			model.decreaseAmountOfBookings();
			view.setSquareTableStatus(icon, model.getMarkedTableNumber() - 7, model.getNewTableName());
			view.disableUnbookButton();
			model.setGreenState(TableState.Unmarked);
			model.setRedState(TableState.Unmarked);
		}
		// Search queue
		else if(e.getActionCommand() == "search") {
			if(view.guestNotSelected()) {
				JOptionPane.showMessageDialog(view, "Markera en gäst att söka bord till.", "Ogiltigt alternativ", JOptionPane.INFORMATION_MESSAGE);
			} else {
				String name = view.getMarkedList();
				String getSpots = name.replaceAll("\\D+", ""); // Detta tar bort alla non-digits
				view.getTableSpots(Integer.parseInt(getSpots));
			}
		}
		// Return from search queue
		else if(e.getActionCommand() == "return") {
			view.setSearchButtonActionCommand("search");
			view.setTablesVisible(true);
		}
		// Remove queue
		else if(e.getActionCommand() == "enqueue") {
			if(view.getGuestName().contains("Skriv gästens namn") || view.getGuestName().length() < 3 || !view.getGuestName().matches(".*[1-6].*")) {
				JOptionPane.showMessageDialog(view, "Skriv ett namn med minst 3 bokstäver och antal gäster 1-6.", "Felaktig inmatning", JOptionPane.INFORMATION_MESSAGE);
			} else {
				String name = view.getGuestName();
				view.enqueueList(name);
				view.setDefaultText("Skriv gästens namn,\nantal personer");
			}
		} else if(e.getActionCommand() == "dequeue") {
			String name = view.getMarkedList();
			if(view.guestNotSelected()) {
				JOptionPane.showMessageDialog(view, "Markera en gäst att ta bort.", "Ogiltigt alternativ", JOptionPane.INFORMATION_MESSAGE);
			} else {
				view.dequeueList();
				view.setTablesVisible(true);
				view.setSearchButtonActionCommand("search");
			}
		}
		// Calendar
		else if(e.getActionCommand() == "reserve") {
			JOptionPane.showMessageDialog(view, "Prototyp");
		} else if(e.getActionCommand() == "close") {
			view.setCalendarVisible(false);
		}
		// Help
		else if(e.getActionCommand() == "help") {
			view.setHelpFrameVisible(model.getHelpButtonControl());
		}
	}

	// MouseListener
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			controlTables(e);
		
			if(e.getComponent().toString().contains("JTextArea") && view.getGuestName().contains("Skriv gästens namn")) {
				view.removeDefaultText("");
			}
			if(e.getComponent().toString().contains("JList")) {
				view.setListSelected(model.getListControl());
			}
		} else if(SwingUtilities.isRightMouseButton(e)) {
			calendar(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	private void controlTables(MouseEvent e) {
		// Green tables
		if(e.toString().contains("Green First 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-1.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(1);
			model.setNewTableName("Red First 4");
			view.enableBookButton();
			view.setRoundTableStatus(icon, 0);
			view.setBookButtonActionCommand("roundBook");
			//view.setRoundTableTextVisible(true, 0);
		}
		else if(e.toString().contains("Green Second 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-2.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(2);
			model.setNewTableName("Red Second 4");
			view.enableBookButton();
			view.setRoundTableStatus(icon, 1);
			view.setBookButtonActionCommand("roundBook");
		}
		else if(e.toString().contains("Green Third 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-3.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(3);
			model.setNewTableName("Red Third 2");
			view.enableBookButton();
			view.setRoundTableStatus(icon, 2);
			view.setBookButtonActionCommand("roundBook");
		}
		else if(e.toString().contains("Green Fourth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-4.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(4);
			model.setNewTableName("Red Fourth 4");
			view.enableBookButton();
			view.setRoundTableStatus(icon, 3);
			view.setBookButtonActionCommand("roundBook");
		}
		else if(e.toString().contains("Green Fifth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-5.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(5);
			model.setNewTableName("Red Fifth 2");
			view.enableBookButton();
			view.setRoundTableStatus(icon, 4);
			view.setBookButtonActionCommand("roundBook");
		}
		else if(e.toString().contains("Green Sixth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-6.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(6);
			model.setNewTableName("Red Sixth 4");
			view.enableBookButton();
			view.setRoundTableStatus(icon, 5);
			view.setBookButtonActionCommand("roundBook");
		}
		else if(e.toString().contains("Green Thirteenth 6") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-7.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(7);
			model.setNewTableName("Red Thirteenth 6");
			view.enableBookButton();
			view.setRoundTableStatus(icon, 6);
			view.setBookButtonActionCommand("roundBook");
		}
		// Square green tables
		else if(e.toString().contains("Green Seventh 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-7.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(7);
			model.setNewTableName("Red Seventh 4");
			view.enableBookButton();
			view.setSquareTableStatus(icon, 0);
			view.setBookButtonActionCommand("squareBook");
		}
		else if(e.toString().contains("Green Eighth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-8.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(8);
			model.setNewTableName("Red Eighth 4");
			view.enableBookButton();
			view.setSquareTableStatus(icon, 1);
			view.setBookButtonActionCommand("squareBook");
		}
		else if(e.toString().contains("Green Ninth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-9.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(9);
			model.setNewTableName("Red Ninth 4");
			view.enableBookButton();
			view.setSquareTableStatus(icon, 2);
			view.setBookButtonActionCommand("squareBook");
		}
		else if(e.toString().contains("Green Tenth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-10.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(10);
			model.setNewTableName("Red Tenth 4");
			view.enableBookButton();
			view.setSquareTableStatus(icon, 3);
			view.setBookButtonActionCommand("squareBook");
		}
		else if(e.toString().contains("Green Eleventh 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-11.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(11);
			model.setNewTableName("Red Eleventh 2");
			view.enableBookButton();
			view.setSquareTableStatus(icon, 4);
			view.setBookButtonActionCommand("squareBook");
		}
		else if(e.toString().contains("Green Twelveth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-12.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(12);
			model.setNewTableName("Red Twelveth 2");
			view.enableBookButton();
			view.setSquareTableStatus(icon, 5);
			view.setBookButtonActionCommand("squareBook");
		}
		else if(e.toString().contains("Green Fourteenth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-13.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(13);
			model.setNewTableName("Red Fourteenth 2");
			view.enableBookButton();
			view.setSquareTableStatus(icon, 6);
			view.setBookButtonActionCommand("squareBook");
		}
		else if(e.toString().contains("Green Fifteenth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-14.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(14);
			model.setNewTableName("Red Fifteenth 2");
			view.enableBookButton();
			view.setSquareTableStatus(icon, 7);
			view.setBookButtonActionCommand("squareBook");
		}
		else if(e.toString().contains("Green Sixteenth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-15.png");
			model.setGreenState(TableState.Marked);
			model.setMarkedTableNumber(15);
			model.setNewTableName("Red Sixteenth 2");
			view.enableBookButton();
			view.setSquareTableStatus(icon, 8);
			view.setBookButtonActionCommand("squareBook");
		}
		// Red tables
		else if(e.toString().contains("Red First 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-1.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(1);
			model.setNewTableName("Green First 4");
			view.enableUnbookButton();
			view.setRoundTableStatus(icon, 0);
			view.setUnbookButtonActionCommand("roundUnbook");
			//view.setRoundTableTextVisible(true, 0);
		}
		else if(e.toString().contains("Red Second 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-2.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(2);
			model.setNewTableName("Green Second 4");
			view.enableUnbookButton();
			view.setRoundTableStatus(icon, 1);
			view.setUnbookButtonActionCommand("roundUnbook");
		}
		else if(e.toString().contains("Red Third 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-3.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(3);
			model.setNewTableName("Green Third 2");
			view.enableUnbookButton();
			view.setRoundTableStatus(icon, 2);
			view.setUnbookButtonActionCommand("roundUnbook");
		}
		else if(e.toString().contains("Red Fourth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-4.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(4);
			model.setNewTableName("Green Fourth 4");
			view.enableUnbookButton();
			view.setRoundTableStatus(icon, 3);
			view.setUnbookButtonActionCommand("roundUnbook");
		}
		else if(e.toString().contains("Red Fifth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-5.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(5);
			model.setNewTableName("Green Fifth 2");
			view.enableUnbookButton();
			view.setRoundTableStatus(icon, 4);
			view.setUnbookButtonActionCommand("roundUnbook");
		}
		else if(e.toString().contains("Red Sixth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-6.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(6);
			model.setNewTableName("Green Sixth 4");
			view.enableUnbookButton();
			view.setRoundTableStatus(icon, 5);
			view.setUnbookButtonActionCommand("roundUnbook");
		}
		else if(e.toString().contains("Red Thirteenth 6") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-marked-table-7.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(7);
			model.setNewTableName("Green Thirteenth 6");
			view.enableUnbookButton();
			view.setRoundTableStatus(icon, 6);
			view.setUnbookButtonActionCommand("roundUnbook");
		}
		// Square red tables
		else if(e.toString().contains("Red Seventh 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-7.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(7);
			model.setNewTableName("Green Seventh 4");
			view.enableUnbookButton();
			view.setSquareTableStatus(icon, 0);
			view.setUnbookButtonActionCommand("squareUnbook");
		}
		else if(e.toString().contains("Red Eighth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-8.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(8);
			model.setNewTableName("Green Eight 4");
			view.enableUnbookButton();
			view.setSquareTableStatus(icon, 1);
			view.setUnbookButtonActionCommand("squareUnbook");
		}
		else if(e.toString().contains("Red Ninth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-9.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(9);
			model.setNewTableName("Green Ninth 4");
			view.enableUnbookButton();
			view.setSquareTableStatus(icon, 2);
			view.setUnbookButtonActionCommand("squareUnbook");
		}
		else if(e.toString().contains("Red Tenth 4") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-10.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(10);
			model.setNewTableName("Green Tenth 4");
			view.enableUnbookButton();
			view.setSquareTableStatus(icon, 3);
			view.setUnbookButtonActionCommand("squareUnbook");
		}
		else if(e.toString().contains("Red Eleventh 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-11.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(11);
			model.setNewTableName("Green Eleventh 2");
			view.enableUnbookButton();
			view.setSquareTableStatus(icon, 4);
			view.setUnbookButtonActionCommand("squareUnbook");
		}
		else if(e.toString().contains("Red Twelveth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-12.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(12);
			model.setNewTableName("Green Twelveth 2");
			view.enableUnbookButton();
			view.setSquareTableStatus(icon, 5);
			view.setUnbookButtonActionCommand("squareUnbook");
		}
		else if(e.toString().contains("Red Fourteenth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-13.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(13);
			model.setNewTableName("Green Fourteenth 2");
			view.enableUnbookButton();
			view.setSquareTableStatus(icon, 6);
			view.setUnbookButtonActionCommand("squareUnbook");
		}
		else if(e.toString().contains("Red Fifteenth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-14.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(14);
			model.setNewTableName("Green Fifteenth 2");
			view.enableUnbookButton();
			view.setSquareTableStatus(icon, 7);
			view.setUnbookButtonActionCommand("squareUnbook");
		}
		else if(e.toString().contains("Red Sixteenth 2") 
				&& model.getGreenState() != TableState.Marked && model.getRedState() != TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-marked-table-15.png");
			model.setRedState(TableState.Marked);
			model.setMarkedTableNumber(15);
			model.setNewTableName("Green Sixteenth 2");
			view.enableUnbookButton();
			view.setSquareTableStatus(icon, 8);
			view.setUnbookButtonActionCommand("squareUnbook");
		}
		// Green click marked tables
		else if(e.toString().contains("Green First 4") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-green-table-1.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green First 4");
			view.disableBookButton();
			view.setRoundTableStatus(icon, 0);
			//view.setRoundTableTextVisible(false, 0);
		}
		else if(e.toString().contains("Green Second 4") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-green-table-2.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Second 4");
			view.disableBookButton();
			view.setRoundTableStatus(icon, 1);
		}
		else if(e.toString().contains("Green Third 2") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-green-table-3.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Third 2");
			view.disableBookButton();
			view.setRoundTableStatus(icon, 2);
		}
		else if(e.toString().contains("Green Fourth 4") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-green-table-4.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Fourth 4");
			view.disableBookButton();
			view.setRoundTableStatus(icon, 3);
		}
		else if(e.toString().contains("Green Fifth 2") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-green-table-5.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Fifth 2");
			view.disableBookButton();
			view.setRoundTableStatus(icon, 4);
		}
		else if(e.toString().contains("Green Sixth 4") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-green-table-6.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Sixth 4");
			view.disableBookButton();
			view.setRoundTableStatus(icon, 5);
		}
		else if(e.toString().contains("Green Thirteenth 6") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-green-table-7.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Thirteenth 6");
			view.disableBookButton();
			view.setRoundTableStatus(icon, 6);
		}
		// Green click square marked tables
		else if(e.toString().contains("Green Seventh 4") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-green-table-7.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Seventh 4");
			view.disableBookButton();
			view.setSquareTableStatus(icon, 0);
		}
		else if(e.toString().contains("Green Eighth 4") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-green-table-8.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Eighth 4");
			view.disableBookButton();
			view.setSquareTableStatus(icon, 1);
		}
		else if(e.toString().contains("Green Ninth 4") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-green-table-9.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Ninth 4");
			view.disableBookButton();
			view.setSquareTableStatus(icon, 2);
		}
		else if(e.toString().contains("Green Tenth 4") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-green-table-10.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Tenth 4");
			view.disableBookButton();
			view.setSquareTableStatus(icon, 3);
		}
		else if(e.toString().contains("Green Eleventh 2") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-green-table-11.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Eleventh 2");
			view.disableBookButton();
			view.setSquareTableStatus(icon, 4);
		}
		else if(e.toString().contains("Green Twelveth 2") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-green-table-12.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Twelveth 2");
			view.disableBookButton();
			view.setSquareTableStatus(icon, 5);
		}
		else if(e.toString().contains("Green Fourteenth 2") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-green-table-13.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Fourteenth 2");
			view.disableBookButton();
			view.setSquareTableStatus(icon, 6);
		}
		else if(e.toString().contains("Green Fifteenth 2") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-green-table-14.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Fifteenth 2");
			view.disableBookButton();
			view.setSquareTableStatus(icon, 7);
		}
		else if(e.toString().contains("Green Sixteenth 2") && model.getGreenState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-green-table-15.png");
			model.setGreenState(TableState.Unmarked);
			model.setNewTableName("Green Sixteenth 2");
			view.disableBookButton();
			view.setSquareTableStatus(icon, 8);
		}
		// Red click marked tables
		else if(e.toString().contains("Red First 4") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-red-table-1.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red First 4");
			view.disableUnbookButton();
			view.setRoundTableStatus(icon, 0);
		}
		else if(e.toString().contains("Red Second 4") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-red-table-2.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Second 4");
			view.disableUnbookButton();
			view.setRoundTableStatus(icon, 1);
		}
		else if(e.toString().contains("Red Third 2") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-red-table-3.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Third 2");
			view.disableUnbookButton();
			view.setRoundTableStatus(icon, 2);
		}
		else if(e.toString().contains("Red Fourth 4") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-red-table-4.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Fourth 4");
			view.disableUnbookButton();
			view.setRoundTableStatus(icon, 3);
		}
		else if(e.toString().contains("Red Fifth 2") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-red-table-5.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Fifth 2");
			view.disableUnbookButton();
			view.setRoundTableStatus(icon, 4);
		}
		else if(e.toString().contains("Red Sixth 4") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-red-table-6.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Sixth 4");
			view.disableUnbookButton();
			view.setRoundTableStatus(icon, 5);
		}	
		else if(e.toString().contains("Red Thirteenth 6") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/round-tables/round-red-table-7.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Thirteenth 6");
			view.disableUnbookButton();
			view.setRoundTableStatus(icon, 6);
		}	
		// Red click square marked tables
		else if(e.toString().contains("Red Seventh 4") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-red-table-7.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Seventh 4");
			view.disableUnbookButton();
			view.setSquareTableStatus(icon, 0);
		}
		else if(e.toString().contains("Red Eighth 4") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-red-table-8.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Eighth 4");
			view.disableUnbookButton();
			view.setSquareTableStatus(icon, 1);
		}
		else if(e.toString().contains("Red Ninth 4") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-red-table-9.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Ninth 4");
			view.disableUnbookButton();
			view.setSquareTableStatus(icon, 2);
		}
		else if(e.toString().contains("Red Tenth 4") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-red-table-10.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Tenth 4");
			view.disableUnbookButton();
			view.setSquareTableStatus(icon, 3);
		}
		else if(e.toString().contains("Red Eleventh 2") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-red-table-11.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Eleventh 2");
			view.disableUnbookButton();
			view.setSquareTableStatus(icon, 4);
		}	
		else if(e.toString().contains("Red Twelveth 2") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-red-table-12.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Twelveth 2");
			view.disableUnbookButton();
			view.setSquareTableStatus(icon, 5);
		}
		else if(e.toString().contains("Red Fourteenth 2") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-red-table-13.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Fourteenth 2");
			view.disableUnbookButton();
			view.setSquareTableStatus(icon, 6);
		}
		else if(e.toString().contains("Red Fifteenth 2") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-red-table-14.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Fifteenth 2");
			view.disableUnbookButton();
			view.setSquareTableStatus(icon, 7);
		}
		else if(e.toString().contains("Red Sixteenth 2") && model.getRedState() == TableState.Marked) {
			icon = new ImageIcon("images/square-tables/square-red-table-15.png");
			model.setRedState(TableState.Unmarked);
			model.setNewTableName("Red Sixteenth 2");
			view.disableUnbookButton();
			view.setSquareTableStatus(icon, 8);
		}
	}
	private void calendar(MouseEvent e) {
		if(e.toString().contains("Green")) {
			view.setCalendarVisible(true);
		}
	}
}

