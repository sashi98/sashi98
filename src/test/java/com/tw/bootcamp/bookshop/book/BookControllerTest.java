package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    UserService userService;

    @Test
    public void testWhenUrlIsNotFound() throws Exception {
        mockMvc.perform(get("/bookfs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHealthIsGood() throws Exception {
        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testWhenGetABook() throws Exception {
        mockMvc.perform(get("/books/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldListAllBooksWhenPresent() throws Exception {
        List<Book> books = new ArrayList<>();
        Book book = new Book("title", "author name", 300);
        books.add(book);
        when(bookService.fetchAll()).thenReturn(books);

        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(bookService, times(1)).fetchAll();
    }

    @Test
    void shouldBeEmptyListWhenNoBooksPresent() throws Exception {
        when(bookService.fetchAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        verify(bookService, times(1)).fetchAll();
    }
}