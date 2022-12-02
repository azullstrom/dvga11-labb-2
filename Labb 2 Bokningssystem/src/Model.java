import java.awt.event.MouseEvent;

public class Model {
	
	private int markedTableNumber, bookingAmount, helpButtonControl;
	private TableState greenState, redState;
	private String name;
	private boolean listSelected;
	
	public Model() {
		
	}
	
	public void setMarkedTableNumber(int table) {
		markedTableNumber = table;
	}
	
	public int getMarkedTableNumber() {
		return markedTableNumber;
	}

	public void increaseAmountOfBookings() {
		bookingAmount++;
	}

	public void decreaseAmountOfBookings() {
		bookingAmount--;
	}
	
	public int getAmountOfBookings() {
		return bookingAmount;
	}

	public void setGreenState(TableState state) {
		greenState = state;
	}
	
	public TableState getGreenState() {
		return greenState;
	}

	public void setRedState(TableState state) {
		redState = state;
	}

	public TableState getRedState() {
		return redState;
	}

	public boolean getHelpButtonControl() {
		helpButtonControl++;
		if(helpButtonControl % 2 == 1)
			return true;
		else
			return false;
	}

	public String getNewTableName() {
		return name;
	}

	public void setNewTableName(String string) {
		name = string;	
	}

	public boolean getListControl() {
		listSelected = !listSelected;
		return listSelected;
	}
}
