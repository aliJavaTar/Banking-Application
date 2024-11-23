package org.bro.banking.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockBean {
    // College class Bean
    @Bean
    public Clock createBean() {
        return Clock.systemDefaultZone();
    }
}
