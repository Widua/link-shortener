package me.widua.linkshortener;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
public abstract class IntegrationTest {

    static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisPropertySet(DynamicPropertyRegistry registry){
        redisContainer.start();
        registry.add("spring.data.redis.port",redisContainer::getFirstMappedPort);
        registry.add("spring.data.redis.host",redisContainer::getHost);
    }

}
