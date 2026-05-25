package com.example.eventify;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:eventify-context-test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false",
		"spring.jpa.hibernate.ddl-auto=validate"
})
class EventifyApplicationTests {

	@Test
	void contextLoads() {
	}

}
