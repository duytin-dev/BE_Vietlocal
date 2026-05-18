package com.vietlocal.app.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FlywayConfig {

	@Bean
	@ConditionalOnProperty(prefix = "app.flyway", name = "repair-on-start", havingValue = "true", matchIfMissing = true)
	FlywayMigrationStrategy flywayRepairThenMigrate() {
		return (Flyway flyway) -> {
			log.info("Flyway repair-on-start: repairing schema history (clears failed migrations)");
			flyway.repair();
			flyway.migrate();
		};
	}
}
