package group.mesh.demo.controller;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "localtest")
@Testcontainers
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(initializers = {UserSearchControllerTest.Initializer.class})
class UserSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("mydb")
            .withUsername("myuser")
            .withPassword("mypass")
            .withInitScript("tc-initscript.sql")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd
                    .withHostName("localhost")
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432))));

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    @Order(1)
    @Transactional
    void testFindUsersByBirthDate() throws Exception {
        // given
        String response = uploadResponse();

        // when - then
        this.mockMvc.perform(get("/search/birthDate?birthDate=1990-07-07"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(response)));
    }

    @Test
    @Order(2)
    @Transactional
    void testFindUsersByName() throws Exception {
        // given
        String response = uploadResponse();

        // when - then
        this.mockMvc.perform(get("/search/name?name=T"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(response)));
    }

    private String uploadResponse() throws IOException {
        return new String(Objects.requireNonNull(getClass()
                .getClassLoader()
                .getResourceAsStream("response/find_users_by_birth_date_and_name.json"))
                .readAllBytes());
    }
}
