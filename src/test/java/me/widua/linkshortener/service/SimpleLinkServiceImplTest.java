package me.widua.linkshortener.service;

import me.widua.linkshortener.entity.LinkDTO;
import me.widua.linkshortener.entity.LinkModel;
import me.widua.linkshortener.exceptions.LinkNotFoundException;
import me.widua.linkshortener.repository.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SimpleLinkServiceImplTest {

    @MockBean
    private LinkRepository repository;
    @MockBean
    private Random mockedRandom;
    private final LinkService underTest;
    private List<LinkModel> exampleExistingLinks;
    private List<String> exampleNotExistingLinks;
    private String exampleURL;

    @Autowired
    public SimpleLinkServiceImplTest(SimpleLinkServiceImpl underTest) {
        this.underTest = underTest;
    }

    @BeforeEach
    void setUp() {
        exampleURL = "https://www.google.com";

        exampleExistingLinks = List.of(
                new LinkModel("hb5yy8", "https://www.google.com", LocalDateTime.now()),
                new LinkModel("yyy888bbbg", "https://www.github.com", LocalDateTime.now())
        );

        exampleNotExistingLinks = List.of(
                "ffff000bbb",
                "bbbbbbyyyy0"
        );
    }

    @Test
    public void ifShortenedWebsiteIsRequestedShouldReturnValidEntity() {
        //Given
        String validRedirectString = exampleExistingLinks.get(0).getRedirectString();
        //When
        when(repository.findById(validRedirectString)).thenReturn(Optional.of(exampleExistingLinks.get(0)));
        LinkDTO linkDTO = underTest.getWebsiteByRedirectString(validRedirectString);
        //Then
        assertEquals(
                exampleExistingLinks.get(0).getRealUrl(),
                linkDTO.getRealUrl().toString()
        );
    }

    @Test
    public void ifLinkIsNotShortenedThrowsAnError() {
        //Given
        String invalidRedirectString = exampleNotExistingLinks.get(0);
        //When
        when(repository.findById(invalidRedirectString)).thenReturn(Optional.empty());
        //
        assertThrows(
                LinkNotFoundException.class,
                () -> {
                    underTest.getWebsiteByRedirectString(invalidRedirectString);
                }
        );
    }

    @Test
    public void shortenLinkFunctionWorksProperly() throws MalformedURLException {
        //Given
        String linkToShorten = exampleURL;
        //When
        when(repository.existsById(anyString())).thenReturn(false);
        String shortenLink = underTest.shortenLink(new URL(linkToShorten));
        //
        assertEquals(1, shortenLink.length());
    }

    @Test
    public void shortenLinkFunctionGeneratesNewStringWhenGeneratedExistInDatabase() throws MalformedURLException {
        //Given
        String linkToShorten = exampleURL;
        //When
        when(mockedRandom.nextInt(anyInt()))
                .thenReturn(0, 1);
        when(repository.existsById("0")).thenReturn(true);
        when(repository.existsById("1")).thenReturn(false);

        String shortenLink = underTest.shortenLink(new URL(linkToShorten));
        //Then
        assertEquals(
                "1",
                shortenLink
        );
    }


}