package com.behavioral.iterator.ex1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Menu {

	List<Item> menuItems;

	public Menu() {
		menuItems = new ArrayList<Item>();
	}

	public void addItem(Item item) {
		menuItems.add(item);
	}

	public Iterator<Item> iterator() {
		return new MenuIterator();
	}

	class MenuIterator implements Iterator<Item> {
		int currentIndex = 0;

		public boolean hasNext() {
			if (currentIndex >= menuItems.size()) {
				return false;
			} else {
				return true;
			}
		}

		public Item next() {
			return menuItems.get(currentIndex++);
		}

		public void remove() {
			menuItems.remove(--currentIndex);
		}

	}

}
