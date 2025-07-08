package fproject2;

import java.util.*;


interface BookActions {
    boolean isAvailable();
    double purchase(int quantity, String customerEmail, String deliveryAddress);
}


abstract class Book implements BookActions {
    final String isbn;
    final String title;
    final String author;
    final int publicationYear;
    final double price;

    Book(String isbn, String title, String author, int year, double price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = year;
        this.price = price;
    }

    String getIsbn() { return isbn; }
    int getPublicationYear() { return publicationYear; }
    double getPrice() { return price; }
}


class PaperBook extends Book {
    private int stock;

    PaperBook(String isbn, String title, String author, int year, double price, int stock) {
        super(isbn, title, author, year, price);
        this.stock = stock;
    }

    public boolean isAvailable() {
        return stock > 0;
    }

    public double purchase(int quantity, String email, String address) {
        if (stock < quantity) {
            throw new RuntimeException("Not enough copies of \"" + title + "\" in stock.");
        }
        stock -= quantity;
        Shipping.send(this, address);
        System.out.println("Quantum book store: " + quantity + " copy/copies of \"" + title + "\" will be shipped.");
        return quantity * price;
    }

    int getRemainingStock() {
        return stock;
    }
}


class EBook extends Book {
    private final String fileType;

    EBook(String isbn, String title, String author, int year, double price, String fileType) {
        super(isbn, title, author, year, price);
        this.fileType = fileType;
    }

    public boolean isAvailable() {
        return true;
    }

    public double purchase(int quantity, String email, String ignoredAddress) {
        if (quantity != 1) {
            throw new RuntimeException("Only one copy of an eBook can be purchased per transaction.");
        }
        EmailService.send(this, email);
        System.out.println("Quantum book store: \"" + title + "\" sent to " + email);
        return price;
    }
}


class DemoBook extends Book {
    DemoBook(String isbn, String title, String author, int year, double price) {
        super(isbn, title, author, year, price);
    }

    public boolean isAvailable() {
        return false;
    }

    public double purchase(int quantity, String email, String address) {
        throw new RuntimeException("Quantum book store: \"" + title + "\" is for display only.");
    }
}


class BookStore {
    private final Map<String, Book> catalog = new HashMap<>();

    void addBook(Book book) {
        catalog.put(book.getIsbn(), book);
        System.out.println("Quantum book store: Added \"" + book.title + "\" to the catalog.");
    }

    List<Book> removeOutdatedBooks(int maxAge) {
        List<Book> removed = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        Iterator<Book> iter = catalog.values().iterator();
        while (iter.hasNext()) {
            Book book = iter.next();
            if ((currentYear - book.getPublicationYear()) > maxAge) {
                removed.add(book);
                iter.remove();
                System.out.println("Quantum book store: \"" + book.title + "\" removed (outdated).");
            }
        }
        return removed;
    }

    double orderBook(String isbn, int qty, String email, String address) {
        Book book = catalog.get(isbn);
        if (book == null) {
            throw new RuntimeException("Book with ISBN " + isbn + " not found.");
        }
        if (!book.isAvailable()) {
            throw new RuntimeException("Quantum book store: \"" + book.title + "\" is not available.");
        }
        return book.purchase(qty, email, address);
    }
}


class Shipping {
    static void send(PaperBook book, String address) {
        System.out.println("Shipping \"" + book.title + "\" to " + address);
    }
}


class EmailService {
    static void send(EBook book, String recipient) {
        System.out.println("Emailing \"" + book.title + "\" to " + recipient);
    }
}


public class QuantumBookstoreFullTest {
    public static void main(String[] args) {
        BookStore store = new BookStore();

       //test adding
        store.addBook(new PaperBook("PB100", "A", "ali", 2008, 35.99, 10));
        store.addBook(new EBook("EB200", "B", "hassan", 2017, 29.99, "PDF"));
        store.addBook(new DemoBook("DB300", "C", "Unknown", 1850, 0.0));
//test deleteing
        store.removeOutdatedBooks(10);

    //test ordering
        try {
            double total = store.orderBook("PB100", 2, "etsh@yahoo.com", "123 Main St");
            System.out.printf("Quantum book store: Total charged: $%.2f%n", total);

            store.orderBook("EB200", 1, "ali@yahoo.com", "");
            store.orderBook("DB300", 1, "ahmed@yahoo.com", "Museum Lane");
        } catch (Exception ex) {
            System.out.println("Transaction error: " + ex.getMessage());
        }
    }
}
