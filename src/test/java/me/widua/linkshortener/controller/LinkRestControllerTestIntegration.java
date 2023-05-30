package me.widua.linkshortener.controller;

import com.google.gson.Gson;
import me.widua.linkshortener.IntegrationTest;
import me.widua.linkshortener.entity.ShortenLinkRecord;
import me.widua.linkshortener.entity.URLRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
class LinkRestControllerTestIntegration extends IntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private Gson gson;


    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.gson = new Gson();
    }

    @Test
    void malformedURL() throws Exception {
        String thisURLIsMalformed = "this_url_is_not_an_url";
        String requestBody = gson.toJson(new URLRecord(thisURLIsMalformed));
        mockMvc.perform(
                        post("/shortener/api/v1/").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().string("Wrong URL provided!")
                );
    }

    @Test
    void doesNotExist() throws Exception {
        String fakeShorten = "gxgdqndq";
        mockMvc.perform(
                        get("/shortener/api/v1/" + fakeShorten))
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void applicationCanSaveAndReturnsLink() throws Exception {
        String link = "https://www.google.com";
        String requestBody = gson.toJson(new URLRecord(link));
        MvcResult postResult = mockMvc.perform(
                        post("/shortener/api/v1/").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.shortenLink").isString()
                )
                .andReturn();
        String resultJsonBody = postResult.getResponse().getContentAsString();
        ShortenLinkRecord linkRecord = gson.fromJson(resultJsonBody, ShortenLinkRecord.class);

        assertEquals(1, linkRecord.shortenLink().length());

        mockMvc.perform(
                        get("/shortener/api/v1/" + linkRecord.shortenLink()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(requestBody)
                );
    }
}