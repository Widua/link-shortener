package me.widua.linkshortener.repository;

import me.widua.linkshortener.IntegrationTest;
import me.widua.linkshortener.entity.LinkModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LinkRepositoryTest extends IntegrationTest {

    private final LinkRepository repository ;
    private LinkModel exampleEntity;

    @BeforeEach
    public void setUp(){
        exampleEntity = new LinkModel("xfxce3pox","https://www.google.com", LocalDateTime.now());
    }

    @AfterEach
    public void tearDown(){
        repository.deleteAll();
    }

    @Autowired
    public LinkRepositoryTest(LinkRepository repository){
        this.repository = repository;
    }

    @Test
    public void doesItConnectsToDatabase(){
        assertDoesNotThrow( () -> { repository.findAll(); } );
    }

    @Test
    public void doesSavingAndRetrievingFromDatabaseWorks(){
        //When
        repository.save(exampleEntity);
        //Then
        assertEquals(1 , Stream.of(repository.findAll()).count() );
    }

    @Test
    public void doesDeletingFromDatabaseWorks(){
        //Given
        String id = exampleEntity.getRedirectString();
        //When
        repository.save(exampleEntity);
        repository.deleteById(id);
        //Then
        assertFalse(repository.existsById(id));
    }
}