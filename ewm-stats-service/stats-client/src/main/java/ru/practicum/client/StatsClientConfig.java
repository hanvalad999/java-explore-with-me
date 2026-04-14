package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StatsClientConfig {
    @Bean
    public RestTemplate statsRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StatsClient statsClient(
            RestTemplate statsRestTemplate,
            @Value("${stats-server.url:http://localhost:8080}") String statsServerUrl
    ) {
        return new StatsClient(statsRestTemplate, statsServerUrl);
    }
}
