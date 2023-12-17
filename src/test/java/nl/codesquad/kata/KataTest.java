package nl.codesquad.kata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class KataTest {

    Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Test
    void testThatLibraryHasEmptyCollection() {
        assertNotNull(library.getCollection());
        assertThat(library.getCollection().size()).isEqualTo(0);
    }

    @Test
    void testThatBookCanBeAddedToCollection() {
        Book firstBook = new Book("ISBN1");
        library.addBooks(firstBook);
        assertThat(library.getCollection())
                .hasSize(1)
                .containsEntry(firstBook, List.of(0));
    }

    @Test
    void testCollectionContainsMultipleBooks() {
        Book firstBook = new Book("ISBN1");
        Book secondBook = new Book("ISBN2");
        library.addBooks(firstBook, secondBook);
        assertThat(library.getCollection())
                .hasSize(2)
                .containsEntry(firstBook, List.of(0))
                .containsEntry(secondBook, List.of(0));
    }

    @Test
    void testCollectionCanContainSameBookMultipleTimes() {
        Book book = new Book("ISBN1");
        library.addBooks(book, book);
        assertThat(library.getCollection())
                .hasSize(1)
                .containsEntry(book, List.of(0, 0));
    }

    @Test
    void testInventoryCanContainMultipleBooks() {
        Book firstBook = new Book("ISBN1");
        Book secondBook = new Book("ISBN2");
        library.addBooks(firstBook, secondBook);
        assertThat(library.getInventory()).hasSize(2);
        assertThat(library.getInventory()).contains(firstBook, secondBook);
    }

    @Test
    void testInventoryCanBeListed() {
        Book firstBook = new Book("ISBN1");
        Book secondBook = new Book("ISBN2");
        Book copyOfFirstBook = new Book("ISBN1");
        library.addBooks(firstBook, secondBook, copyOfFirstBook);
        assertThat(library.getCollection())
                .hasSize(2)
                .containsEntry(firstBook, List.of(0, 0))
                .containsEntry(secondBook, List.of(0));
    }

    @Test
    void testInventoryExcludesLentOutBooks() {
        Book firstBook = new Book("ISBN1");
        Book secondBook = new Book("ISBN2");
        library.addBooks(firstBook, secondBook);
        library.lendBook(secondBook, 1);
        assertThat(library.getInventory()).hasSize(1);
        assertThat(library.getInventory()).containsOnly(firstBook);
    }

    @Test
    void testCollectionCanContainMultipleCopiesOfBook() {
        Book book = new Book("ISBN1");
        Book copyOfBook = new Book("ISBN1");
        library.addBooks(book, copyOfBook);
        assertThat(library.getCollection())
                .hasSize(1)
                .containsEntry(book, List.of(0, 0));
    }


    @Test
    void testCopiesOfBookCanBeInDifferentLocations() {
        Book book = new Book("ISBN1");
        Book copyOfBook = new Book("ISBN1");
        library.addBooks(book, copyOfBook);
        library.lendBook(book, 1);
        assertThat(library.getCollection())
                .hasSize(1)
                .containsEntry(book, List.of(1, 0));
    }

    @Test
    public void testLendBookWhenBookNotAvailable() {
        Book book = new Book("ISBN1");
        library.addBooks(book);
        library.lendBook(book, 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            library.lendBook(book, 2);
        });
        assertEquals("The requested book is not available in stock", exception.getMessage());
    }

    @Test
    void testCopiesOfBookCanBeAtDifferentMembers() {
        Book book = new Book("ISBN1");
        Book copyOfBook = new Book("ISBN1");
        library.addBooks(book, copyOfBook);
        library.lendBook(book, 1);
        library.lendBook(book, 2);
        assertThat(library.getCollection())
                .hasSize(1)
                .containsEntry(book, List.of(1, 2));
    }

    @Test
    public void testRemoveBookFromInventory() {
        Book book = new Book("ISBN1");
        library.addBooks(book);
        assertThat(library.getCollection().size()).isEqualTo(1);
        assertThat(library.getInventory()).containsOnly(book);
        library.removeBook(book);
        assertThat(library.getCollection().size()).isEqualTo(0);

    }

    @Test
    public void testRemoveBookWhichIsLendOut() {
        Book firstBook = new Book("ISBN1");
        Book secondBook = new Book("ISBN2");
        library.addBooks(firstBook, secondBook);
        library.lendBook(secondBook, 1);
        library.removeBook(secondBook);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            library.removeBook(secondBook);
        });
        assertEquals("The book is not in inventory, so cannot be removed from the collection", exception.getMessage());
    }

    @Test
    public void testRemoveOneCopyOfBook() {
        Book book = new Book("ISBN1");
        library.addBooks(book, book);
        library.removeBook(book);
        assertThat(library.getCollection().size()).isEqualTo(1);
        assertThat(library.getInventory()).containsOnly(book);
    }

    @Test
    public void testRemoveLastCopyOfBook() {
        Book firstBook = new Book("ISBN1");
        Book secondBook = new Book("ISBN2");
        library.addBooks(firstBook, firstBook, secondBook);
        library.removeBook(secondBook);
        assertThat(library.getCollection().size()).isEqualTo(1);
        assertThat(library.getInventory()).containsOnly(firstBook);
    }
}