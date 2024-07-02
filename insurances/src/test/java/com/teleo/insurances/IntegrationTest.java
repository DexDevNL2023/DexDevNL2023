package com.teleo.insurances;

import com.teleo.insurances.config.AsyncSyncConfiguration;
import com.teleo.insurances.config.EmbeddedKafka;
import com.teleo.insurances.config.EmbeddedSQL;
import com.teleo.insurances.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { InsurancesApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
