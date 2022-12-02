import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import javax.swing.DefaultListModel;

public class GuestQueue<T> implements QueueADT<T> {

	private DefaultListModel<T> list;
	private int size;
	private String position;
	
	public GuestQueue() {
		list = new DefaultListModel<T>();
	}

	@Override
	public void enqueue(T element) {
		list.add(size, element);
		size++;
	}

	@Override
	public void dequeue(int index) {
		if(!isEmpty()) {
			list.remove(index);
			size--;
		}
	}
	
	public boolean isEmpty() {
		return size <= 0;
	}
	
	public DefaultListModel getModel() {
		return list;
	}
}
