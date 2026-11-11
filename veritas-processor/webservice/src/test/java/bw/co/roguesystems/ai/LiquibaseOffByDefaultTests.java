package bw.co.roguesystems.ai;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import bw.co.roguesystems.ai.config.LiquibaseAfterHibernate;
import bw.co.roguesystems.ai.profile.ProcessingProfileService;

/** Liquibase is off unless veritas.liquibase.enabled is set: Hibernate makes the tables. */
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:veritas-no-liquibase;DB_CLOSE_DELAY=-1")
@ActiveProfiles("test")
class LiquibaseOffByDefaultTests {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private ProcessingProfileService profiles;

    @Test
    void liquibaseIsOffAndHibernateMakesTheTables() {
        assertThat(context.getBeansOfType(LiquibaseAfterHibernate.class)).isEmpty();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM veritas_schema", Integer.class)).isZero();
        assertThat(profiles.exists("default")).isTrue();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.tables "
                + "WHERE UPPER(table_name) = 'DATABASECHANGELOG'", Integer.class)).isZero();
    }
}
