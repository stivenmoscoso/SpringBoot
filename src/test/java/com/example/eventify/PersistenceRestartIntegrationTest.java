package com.example.eventify;

import com.example.eventify.model.Event;
import com.example.eventify.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersistenceRestartIntegrationTest {
    @TempDir
    Path tempDir;

    @Test
    void eventPersistsAfterApplicationRestart() {
        String databaseUrl = "jdbc:h2:file:" + tempDir.resolve("eventify-restart-db").toAbsolutePath().toString().replace("\\", "/");

        Long eventId;
        try (ConfigurableApplicationContext context = startApplication(databaseUrl)) {
            EventRepository eventRepository = context.getBean(EventRepository.class);
            Event event = eventRepository.save(new Event(null, "Evento persistente", LocalDate.of(2026, 6, 11), "Sigue disponible"));
            eventId = event.getId();
        }

        try (ConfigurableApplicationContext context = startApplication(databaseUrl)) {
            EventRepository eventRepository = context.getBean(EventRepository.class);

            assertTrue(eventRepository.findById(eventId).isPresent());
            assertEquals("Evento persistente", eventRepository.findById(eventId).orElseThrow().getNombre());
        }
    }

    private ConfigurableApplicationContext startApplication(String databaseUrl) {
        return new SpringApplicationBuilder(EventifyApplication.class)
                .web(WebApplicationType.NONE)
                .run(
                        "--spring.datasource.url=" + databaseUrl,
                        "--spring.datasource.driver-class-name=org.h2.Driver",
                        "--spring.datasource.username=sa",
                        "--spring.datasource.password=",
                        "--spring.jpa.hibernate.ddl-auto=update",
                        "--spring.jpa.show-sql=false",
                        "--spring.h2.console.enabled=false"
                );
    }
}
