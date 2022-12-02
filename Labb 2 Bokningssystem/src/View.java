import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Highlighter;

import com.toedter.calendar.JCalendar;

public class View extends JFrame {
	
	private MyColorScheme color;
	private JPanel bookPanel, queuePanel, queueTextPanel, calendarPanel, queueScrollPanel, helpPanel, tooltipPanel;
	private JLayeredPane restaurantPanel;
	private JButton bookButton, unbookButton, helpButton, queueSubmitButton, queueRemoveButton, queueSearchButton, reserveCalendarButton, closeCalendarButton;
	private ImageIcon background;
	private ImageIcon[] roundTableGreen, squareTableGreen, roundHelpTables;
	private JLabel backgroundLabel, freeLabel, markedLabel, bookedLabel;
	private JLabel[] roundTableLabel, squareTableLabel, roundTableHelpLabel;
	private JList<String> queueList;
	private JTextArea queueText;
	//private JTextField[] guestNameText;
	private GuestQueue<String> guestQueue;
	private JScrollPane scrollPane;
	private JCalendar calendar;
	private Border border;

	public View(Controller controller) {
		color = new MyColorScheme();
		border = BorderFactory.createLineBorder(color.blue(), 6);
		
		// Queue Components
		guestQueue = new GuestQueue<String>();
		queueList = new JList<String>(guestQueue.getModel());
		queueList.setBorder(border);
		queueList.setFont(new Font("Verdana", Font.PLAIN, 20));
		queueList.setForeground(color.lavender());
		queueList.setBackground(color.grey());
		queueList.addMouseListener(controller);
		queueText = new JTextArea("Skriv gästens namn,\nantal personer");
		queueText.setBorder(border);
		queueText.setFont(new Font("Verdana", Font.PLAIN, 13));
		queueText.setLineWrap(true);
		queueText.setPreferredSize(new Dimension(200, 100));
		queueText.setForeground(color.lavender());
		queueText.setBackground(color.grey());
		queueText.addMouseListener(controller);
		/*
		// TextField
		guestNameText = new JTextField[16];
		for(int i = 0; i < guestNameText.length; i++) {
			guestNameText[i] = new JTextField("Namn, antal personer");
			guestNameText[i].setVisible(false);
			guestNameText[i].setBorder(border);
			guestNameText[i].setFont(new Font("Verdana", Font.PLAIN, 13));
		}*/
		
		// JLabel/Tables
		freeLabel = new JLabel("Ledigt: ");
		freeLabel.setForeground(color.lavender());
		freeLabel.setFont(new Font("Verdana", Font.BOLD, 13));
		markedLabel = new JLabel("Markerat: ");
		markedLabel.setForeground(color.lavender());
		markedLabel.setFont(new Font("Verdana", Font.BOLD, 13));
		bookedLabel = new JLabel("Bokat: ");
		bookedLabel.setForeground(color.lavender());
		bookedLabel.setFont(new Font("Verdana", Font.BOLD, 13));
		roundTableGreen = new ImageIcon[7];
		squareTableGreen = new ImageIcon[9];
		roundTableLabel = new JLabel[7];
		squareTableLabel = new JLabel[9];
		roundTableGreen[0] = new ImageIcon("images/round-tables/round-green-table-" + 1 + ".png");
		roundTableLabel[0] = new JLabel();
		roundTableLabel[0].setIcon(roundTableGreen[0]);
		roundTableLabel[0].setBounds(62, 83, 65, 65);
		roundTableLabel[0].addMouseListener(controller);
		for(int i = 1; i < 7; i++) {
			roundTableGreen[i] = new ImageIcon("images/round-tables/round-green-table-" + (i+1) + ".png");
			roundTableLabel[i] = new JLabel();
			roundTableLabel[i].setIcon(roundTableGreen[i]);
			roundTableLabel[i].addMouseListener(controller);
			if(i % 2 == 1) {
				roundTableLabel[i].setBounds(62, 137 + i * 50, 65, 65);
			} else if(i == 6) {
				roundTableLabel[i].setBounds(386, 322, 65, 65);
			} else {
				roundTableLabel[i].setBounds(130, 134 + i * 51, 65, 65);
			}
		}
		// OBS Siffran indikerar antal platser vid bordet.
		roundTableLabel[0].setName("Green First 4");
		roundTableLabel[1].setName("Green Second 4");
		roundTableLabel[2].setName("Green Third 2");
		roundTableLabel[3].setName("Green Fourth 4");
		roundTableLabel[4].setName("Green Fifth 2");
		roundTableLabel[5].setName("Green Sixth 4");
		roundTableLabel[6].setName("Green Thirteenth 6");
		for(int i = 0; i < 9; i++) {
			squareTableGreen[i] = new ImageIcon("images/square-tables/square-green-table-" + (i+7) + ".png");
			squareTableLabel[i] = new JLabel();
			squareTableLabel[i].setIcon(squareTableGreen[i]);
			squareTableLabel[i].addMouseListener(controller);
			if(i <= 3) {
				squareTableLabel[i].setBounds(245 + i * 75, 494, 73, 60);
			}
			else if(i > 3 && i <= 5){
				squareTableLabel[i].setBounds(-65 + i * 80, 408, 60, 60);
			} else {
				squareTableLabel[i].setBounds(481, 845 - i * 75, 60, 60);
			}
		}
		squareTableLabel[0].setName("Green Seventh 4");
		squareTableLabel[1].setName("Green Eighth 4");
		squareTableLabel[2].setName("Green Ninth 4");
		squareTableLabel[3].setName("Green Tenth 4");
		squareTableLabel[4].setName("Green Eleventh 2");
		squareTableLabel[5].setName("Green Twelveth 2");
		squareTableLabel[6].setName("Green Fourteenth 2");
		squareTableLabel[7].setName("Green Fifteenth 2");
		squareTableLabel[8].setName("Green Sixteenth 2");
		
		// JLabel/Background
		background = new ImageIcon("images/background.png");
		backgroundLabel = new JLabel();
		backgroundLabel.setIcon(background);
		backgroundLabel.setBounds(0, 0, 600, 600);
		
		// Buttons
		queueSearchButton = new JButton("Sök bord");
		queueSearchButton.setOpaque(true);
		queueSearchButton.setBackground(color.black());
		queueSearchButton.addActionListener(controller);
		queueSearchButton.setActionCommand("search");
		queueSearchButton.setFont(new Font("Verdana", Font.PLAIN, 12));
		bookButton = new JButton("BOKA");
		bookButton.setOpaque(true);
		bookButton.setBackground(color.black());
		bookButton.addActionListener(controller);
		bookButton.setFont(new Font("Verdana", Font.PLAIN, 12));
		bookButton.setVisible(false);
		helpButton = new JButton("Hjälp");
		helpButton.addActionListener(controller);
		helpButton.setActionCommand("help");
		helpButton.setOpaque(true);
		helpButton.setBackground(color.black());
		helpButton.setFont(new Font("Verdana", Font.PLAIN, 12));
		helpButton.setSize(50, 50);
		queueSubmitButton = new JButton("Lägg till");
		queueSubmitButton.setOpaque(true);
		queueSubmitButton.setBackground(color.black());
		queueSubmitButton.addActionListener(controller);
		queueSubmitButton.setActionCommand("enqueue");
		queueSubmitButton.setFont(new Font("Verdana", Font.PLAIN, 12));
		queueRemoveButton = new JButton("Ta bort");
		queueRemoveButton.setOpaque(true);
		queueRemoveButton.setBackground(color.black());
		queueRemoveButton.addActionListener(controller);
		queueRemoveButton.setActionCommand("dequeue");
		queueRemoveButton.setFont(new Font("Verdana", Font.PLAIN, 12));
		unbookButton = new JButton("AVBOKA");
		unbookButton.setOpaque(true);
		unbookButton.setBackground(color.black());
		unbookButton.addActionListener(controller);
		unbookButton.setFont(new Font("Verdana", Font.PLAIN, 12));
		unbookButton.setVisible(false);
		reserveCalendarButton = new JButton("Reservera");
		reserveCalendarButton.addActionListener(controller);
		reserveCalendarButton.setActionCommand("reserve");
		closeCalendarButton = new JButton("Stäng");
		closeCalendarButton.addActionListener(controller);
		closeCalendarButton.setActionCommand("close");
		
		// Calendar
		calendar = new JCalendar();
		calendar.setVisible(false);
		calendar.setBounds(0, 100, 600, 300);
		calendar.setLayout(new FlowLayout());
		calendarPanel = new JPanel();
		calendarPanel.add(reserveCalendarButton);
		calendarPanel.add(closeCalendarButton);
		calendar.add(calendarPanel, BorderLayout.SOUTH);

		// Help panel images/labels
		roundHelpTables = new ImageIcon[3];
		roundTableHelpLabel = new JLabel[3];
		roundHelpTables[0] = new ImageIcon("images/round-tables/round-green-table-1.png");
		roundHelpTables[1] = new ImageIcon("images/round-tables/round-marked-table-1.png");
		roundHelpTables[2] = new ImageIcon("images/round-tables/round-red-table-1.png");
		for(int i = 0; i < 3; i++) {
			roundTableHelpLabel[i] = new JLabel();
			roundTableHelpLabel[i].setIcon(roundHelpTables[i]);
			roundTableHelpLabel[i].setSize(150, 150);
		}
		
		// Panels
		tooltipPanel = new JPanel();
		tooltipPanel.setVisible(false);
		tooltipPanel.setBounds(100, 100, 400, 300);
		tooltipPanel.setBackground(color.grey());
		tooltipPanel.setBorder(border);
		tooltipPanel.setLayout(new BoxLayout(tooltipPanel, BoxLayout.X_AXIS));
		tooltipPanel.add(Box.createHorizontalGlue());
		tooltipPanel.add(freeLabel);
		tooltipPanel.add(roundTableHelpLabel[0]);
		tooltipPanel.add(Box.createHorizontalGlue());
		tooltipPanel.add(markedLabel);
		tooltipPanel.add(roundTableHelpLabel[1]);
		tooltipPanel.add(Box.createHorizontalGlue());
		tooltipPanel.add(bookedLabel);
		tooltipPanel.add(roundTableHelpLabel[2]);
		tooltipPanel.add(Box.createHorizontalGlue());
		helpPanel = new JPanel();
		helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.X_AXIS));
		helpPanel.add(Box.createHorizontalGlue());
		helpPanel.add(helpButton);
		helpPanel.setBackground(color.black());
		bookPanel = new JPanel();
		bookPanel.setLayout(new GridLayout(1, 3));
		bookPanel.setPreferredSize(new Dimension(600, 75));
		bookPanel.setBackground(color.black());
		bookPanel.add(bookButton);
		bookPanel.add(unbookButton);
		bookPanel.add(helpPanel);
		restaurantPanel = new JLayeredPane();
		restaurantPanel.setBounds(0, 0, getWidth(), getHeight());
		restaurantPanel.setBackground(color.black());
		restaurantPanel.add(backgroundLabel, new Integer(1));
		for(int i = 0; i < 7; i++)
			restaurantPanel.add(roundTableLabel[i], new Integer(2));
		for(int i = 0; i < 9; i++)
			restaurantPanel.add(squareTableLabel[i], new Integer(2));
		restaurantPanel.add(calendar, new Integer(3));
		restaurantPanel.add(tooltipPanel, new Integer(3));
		/*for(int i = 0; i < guestNameText.length; i++)
			restaurantPanel.add(guestNameText[i], new Integer(4));*/
		queueTextPanel = new JPanel();
		queueTextPanel.setSize(200, 300);
		queueTextPanel.setBackground(color.black());
		queueTextPanel.add(queueText);
		queueTextPanel.add(queueSubmitButton);
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(queueList);
		scrollPane.setPreferredSize(new Dimension(200, 400));
		scrollPane.setBackground(color.black());
		queueScrollPanel = new JPanel();
		queueScrollPanel.add(queueSearchButton);
		queueScrollPanel.add(queueRemoveButton);
		queueScrollPanel.add(scrollPane);
		queueScrollPanel.setBackground(color.black());
		queuePanel = new JPanel();
		queuePanel.setPreferredSize(new Dimension(200, 100));
		queuePanel.setBackground(color.black());
		queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));
		queuePanel.add(queueTextPanel);
		queuePanel.add(queueScrollPanel);
		
		this.add(bookPanel, BorderLayout.NORTH);
		this.add(restaurantPanel, BorderLayout.CENTER);
		this.add(queuePanel, BorderLayout.EAST);

		this.setTitle("Boka Bord");
		this.setSize(800, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		controller.setView(this);
	}
	
	public void setRoundTableStatus(ImageIcon icon, int index, String name) {
		roundTableLabel[index].setIcon(icon);
		roundTableLabel[index].setName(name);
	}
	
	public void setRoundTableStatus(ImageIcon icon, int index) {
		roundTableLabel[index].setIcon(icon);
	}
	
	public void setSquareTableStatus(ImageIcon icon, int index, String name) {
		squareTableLabel[index].setIcon(icon);
		squareTableLabel[index].setName(name);
	}
	
	public void setSquareTableStatus(ImageIcon icon, int index) {
		squareTableLabel[index].setIcon(icon);
	}

	public void enableBookButton() {
		bookButton.setVisible(true);
	}

	public void disableBookButton() {
		bookButton.setVisible(false);
	}
	
	public void enableUnbookButton() {
		unbookButton.setVisible(true);
	}

	public void disableUnbookButton() {
		unbookButton.setVisible(false);
	}	
	
	public void setBookButtonActionCommand(String string) {
		bookButton.setActionCommand(string);
	}

	public void setUnbookButtonActionCommand(String string) {
		unbookButton.setActionCommand(string);
	}

	public String getGuestName() {
		return queueText.getText();
	}

	public void enqueueList(String name) {
		guestQueue.enqueue(name);
	}

	public void dequeueList() {
		guestQueue.dequeue(queueList.getSelectedIndex());
	}

	public void setDefaultText(String string) {
		queueText.setForeground(color.lavender());
		queueText.setText(string);
	}
	
	public void removeDefaultText(String string) {
		queueText.setForeground(color.lavender());
		queueText.setText(string);
	}

	public void setCalendarVisible(boolean bool) {
		calendar.setVisible(bool);
	}

	public String getMarkedList() {
		return queueList.getSelectedValue();
	}

	public boolean guestNotSelected() {
		return queueList.isSelectionEmpty();
	}

	public void setHelpFrameVisible(boolean bool) {
		tooltipPanel.setVisible(bool);
		if(bool) {
			helpButton.setBackground(color.blue());
		} else {
			helpButton.setBackground(color.black());
		}
	}
	
	public void getTableSpots(int spots) {
		for(int i = 0; i < roundTableLabel.length; i++) {
			String string = roundTableLabel[i].getName();
			String getSpots = string.replaceAll("\\D+", "");
			if(Integer.parseInt(getSpots) >= spots && roundTableLabel[i].getName().contains("Green")) {
			
			} else {
				roundTableLabel[i].setVisible(false);
				queueSearchButton.setText("Återgå");
				queueSearchButton.setActionCommand("return");
			}
		}
		for(int i = 0; i < squareTableLabel.length; i++) {
			String string = squareTableLabel[i].getName();
			String getSpots = string.replaceAll("\\D+", "");
			if(Integer.parseInt(getSpots) >= spots && squareTableLabel[i].getName().contains("Green")) {
				
			} else {
				squareTableLabel[i].setVisible(false);
				queueSearchButton.setText("Återgå");
				queueSearchButton.setActionCommand("return");
			}
		}
	}

	public void setTablesVisible(boolean bool) {
		for(int i = 0; i < squareTableLabel.length; i++) {
			squareTableLabel[i].setVisible(bool);
		}
		for(int i = 0; i < roundTableLabel.length; i++) {
			roundTableLabel[i].setVisible(bool);
		}
	}

	public void setSearchButtonActionCommand(String string) {
		queueSearchButton.setActionCommand(string);
		queueSearchButton.setText("Sök bord");
	}

	public void setListSelected(boolean selected) {
		if(selected) {
			
		} else {
			queueList.clearSelection();
		}
	}

	/*public void setRoundTableTextVisible(boolean bool, int i) {
		guestNameText[i].setVisible(bool);
		guestNameText[i].setBounds(roundTableLabel[i].getX() - 50, roundTableLabel[i].getY() - 15, 170, 35);
	}
	
	public void setRoundTableTextEnabled(boolean bool, int i) {
		guestNameText[i].setEnabled(bool);
		if(bool) {
			guestNameText[i].setText("Namn, antal personer");
		}
	}

	public String getRoundTableText(int i) {
		return guestNameText[i].getText();
	}*/
}
