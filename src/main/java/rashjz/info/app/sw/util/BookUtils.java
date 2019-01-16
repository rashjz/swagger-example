package rashjz.info.app.sw.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

 import rashjz.info.app.sw.domain.Book;

public class BookUtils {

    private static List<Book> books = new ArrayList<Book>();

    private static final int NUM_BOOKS = 30;

    private static final int MIN_BOOK_NUM = 1000;

    public static List<Book> buildBooks() {
        if (books.isEmpty()) {
            IntStream.range(0, NUM_BOOKS).forEach(n -> {
                books.add(new Book(MIN_BOOK_NUM + n + 1, "Spring in Action"));
            });

        }

        return books;
    }

}