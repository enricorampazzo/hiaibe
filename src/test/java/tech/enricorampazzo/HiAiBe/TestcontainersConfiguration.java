package tech.enricorampazzo.HiAiBe;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    @Bean
    @ServiceConnection
    public MySQLContainer<?> neo4jContainer() {
        MySQLContainer<?> container = new MySQLContainer<>("mysql").withExposedPorts(3306).
                withPassword("password").withUsername("root");
        return container;
    }
}
