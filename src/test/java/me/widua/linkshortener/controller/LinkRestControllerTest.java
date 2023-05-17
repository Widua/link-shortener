package me.widua.linkshortener.controller;

import com.google.gson.Gson;
import me.widua.linkshortener.entity.LinkDTO;
import me.widua.linkshortener.entity.URLRecord;
import me.widua.linkshortener.entity.ShortenLinkRecord;
import me.widua.linkshortener.exceptions.LinkNotFoundException;
import me.widua.linkshortener.service.LinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LinkRestControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private LinkService linkService;

    private Gson gson;

    @BeforeEach
    public void setup(){
        gson = new Gson();
    }

    @Autowired
    public LinkRestControllerTest(MockMvc mockMvc){
        this.mockMvc = mockMvc;
    }

    @Test
    public void tryingToShortenMalformedURLShouldReturnsBadRequest() throws Exception {
        //Given
        URLRecord linkRequest = new URLRecord("not_correct_url");
        String requestAsJson = gson.toJson(linkRequest);
        //When
        when(linkService.shortenLink(any())).thenThrow(LinkNotFoundException.class);
        //Then
        mockMvc
                .perform( post("/shortener/api/v1/").content( requestAsJson ).contentType(MediaType.APPLICATION_JSON) )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().string("Wrong URL provided!")
                );
    }

    @Test
    public void tryingToShortenNullReturnsBadRequest() throws Exception {
        mockMvc
                .perform( post("/shortener/api/v1/").content("") )
                .andDo(print())
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    public void tryingAccessToNonExistingShortenReturnsNotFound() throws Exception {
        when(linkService.getWebsiteByRedirectString(any())).thenThrow(LinkNotFoundException.class);

        mockMvc
                .perform( get("/shortener/api/v1/notExistingShorten"))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    public void tryingShortenValidLink() throws Exception{
        //Given
        URLRecord linkRequest = new URLRecord("https://www.google.com");
        ShortenLinkRecord wantedResponse = new ShortenLinkRecord("validShortenedLink");
        //When
        when(linkService.shortenLink(any())).thenReturn("validShortenedLink");

        //Then
        mockMvc
                .perform( post("/shortener/api/v1/").content(gson.toJson(linkRequest)).contentType(MediaType.APPLICATION_JSON) )
                .andDo(print())
                .andExpectAll(
                    status().isOk(),
                    content().json(gson.toJson(wantedResponse))
                );

    }

    @Test
    public void tryingRetrieveShortenedLink() throws Exception{
        //Given
        URLRecord wantedResponse = new URLRecord("https://www.google.com");
        LinkDTO mockedDTO = new LinkDTO("validRedirectString",new URL(wantedResponse.url()));
        //When
        when(linkService.getWebsiteByRedirectString(anyString())).thenReturn(mockedDTO);
        //Then
        mockMvc
                .perform( get(String.format("/shortener/api/v1/%s",mockedDTO.getRedirectString())))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(gson.toJson(wantedResponse))
                );
    }
}
