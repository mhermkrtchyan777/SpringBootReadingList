package com.example.readinglist;

import com.example.reader.Reader;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
public class ReadingListApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).apply(springSecurity()).build();
    }

    @org.junit.Test
    public void homepage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/readingList")).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.view().name("readingList")).
                andExpect(MockMvcResultMatchers.model().attributeExists("books")).
                andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.is(Matchers.empty())));
    }

    @org.junit.Test
    public void postBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/readingList").
                        contentType(MediaType.APPLICATION_FORM_URLENCODED).
                        param("title", "BOOK TITLE").
                        param("author", "BOOK AUTHOR").
                        param("isbn", "12345657890").
                        param("description", "DESCRIPTION")).
                andExpect(MockMvcResultMatchers.status().is3xxRedirection()).
                andExpect(MockMvcResultMatchers.header().string("Location", "/readingList"));
        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setReader("craig");
        expectedBook.setTitle("BOOK TITLE");
        expectedBook.setAuthor("BOOK AUTHOR");
        expectedBook.setIsbn("1234567890");
        expectedBook.setDescription("DESCRIPTION");

        mockMvc.perform(MockMvcRequestBuilders.get("/readingList")).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.view().name("readingList")).
                andExpect(MockMvcResultMatchers.model().attributeExists("books")).
                andExpect(MockMvcResultMatchers.model().attribute("books", hasSize(1))).
                andExpect(MockMvcResultMatchers.model().attribute("books", contains(samePropertyValuesAs(expectedBook))));
    }

    @org.junit.Test
    @WithUserDetails("craig")
    public void homePage_authenticatedUser() throws Exception{
        Reader expextedReader=new Reader();
        expextedReader.setUsername("craig");
        expextedReader.setPassword("password");
        expextedReader.setFullname("Craig Walls");

        mockMvc.perform(MockMvcRequestBuilders.get("/")).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.view().name("readingList")).
                andExpect(MockMvcResultMatchers.model().attribute("reader",samePropertyValuesAs(expextedReader))).
                andExpect(MockMvcResultMatchers.model().attribute("books",hasSize(0)));
    }
}
