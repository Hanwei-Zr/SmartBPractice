package unit.SmartBPractice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages ={"wen.smartPractice.model"})
@EnableJpaRepositories(basePackages = "wen.smartPractice.repository")
public class DaoConfig {
}