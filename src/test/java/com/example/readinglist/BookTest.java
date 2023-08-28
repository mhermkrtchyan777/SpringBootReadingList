package com.example.readinglist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class BookTest {
    @Test
    public void testBookEntityFields() {
        Book book = new Book();
        book.setId(1L);
        book.setReader("testReader");
        book.setIsbn("1234567890");
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setDescription("Test Description");

        assertEquals(1L, book.getId());
        assertEquals("testReader", book.getReader());
        assertEquals("1234567890", book.getIsbn());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("Test Description", book.getDescription());
    }
}
