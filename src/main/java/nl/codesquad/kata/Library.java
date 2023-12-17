package nl.codesquad.kata;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Library {

    public Map<Book, List<Integer>> collection;

    public Library() {
        this.collection = new HashMap<>();
    }

    public Map<Book, List<Integer>> getCollection() {
        return collection;
    }

    public List<Book> getInventory() {
        List<Book> inventory = new ArrayList<>();

        for (Map.Entry<Book, List<Integer>> entry : collection.entrySet()) {
            List<Integer> integers = entry.getValue();
            if (integers != null && integers.contains(0)) {
                inventory.add(entry.getKey());
            }
        }
        return inventory;
    }

    public void addBooks(Book... books) {
        for (Book book : books) {
            collection.computeIfAbsent(book, k -> new ArrayList<>()).add(0);
        }
    }

    public void lendBook(Book book, int memberId) {
        List<Integer> locations = collection.get(book);
        if (locations != null && locations.contains(0)) {
            int indexOfZero = locations.indexOf(0);
            locations.set(indexOfZero, memberId);
        } else {
            throw new IllegalArgumentException("The requested book is not available in stock");
        }
    }

    public void removeBook(Book book) {
        List<Integer> locations = collection.get(book);
        if (locations != null && !locations.isEmpty()) {
            if (locations.size() > 1) {
                int indexToRemove = locations.indexOf(0);
                if (indexToRemove != -1) {
                    locations.remove(indexToRemove);
                }
            } else {
                // If it's the last copy, remove the book from the collection entirely
                collection.remove(book);
            }
        } else {
            throw new IllegalArgumentException("The book is not in inventory, so cannot be removed from the collection");
        }
    }
}




