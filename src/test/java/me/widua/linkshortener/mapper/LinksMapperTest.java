package me.widua.linkshortener.mapper;

import me.widua.linkshortener.entity.LinkDTO;
import me.widua.linkshortener.entity.LinkModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LinksMapperTest {

    private final LinksMapper mapper;

    private LinkDTO exampleDTO;
    private LinkModel exampleModel;


    @Autowired
    public LinksMapperTest(LinksMapper mapper){
        this.mapper = mapper;
    }

    @BeforeEach
    void setUp() throws MalformedURLException {
        exampleDTO = new LinkDTO("xfxce3pox", new URL("https://www.google.com"));
        exampleModel = new LinkModel("xfxce3pox","https://www.google.com", LocalDateTime.now());
    }

    @Test
    public void DTOToModelMappingTest(){
        //Given
        LinkModel expected = exampleModel;
        //When
        LinkModel mapped = mapper.linkDTOToLinkModel(exampleDTO);
        //Then

        assertAll(
                "Does properties mapped correctly",
                () -> {
                    assertEquals(
                            expected.getRealUrl(),
                            mapped.getRealUrl()
                    );
                },
                () -> {
                    assertEquals(
                            expected.getRedirectString(),
                            mapped.getRedirectString()
                    );
                },
                () -> {
                    assertNotNull(
                            mapped.getLastTimeUsed()
                    );
                }
        );
    }

    @Test
    public void ModelToDTOMappingTest(){
        //Given
        LinkDTO expected = exampleDTO ;
        //When
        LinkDTO mapped = mapper.linkModelToLinkDTO(exampleModel);

        assertAll(
                "Does properties mapped correctly",
                () -> {
                    assertEquals(
                            expected.getRealUrl(),
                            mapped.getRealUrl()
                    );
                },
                () -> {
                    assertEquals(
                            expected.getRedirectString(),
                            mapped.getRedirectString()
                    );
                }
        );
    }

}