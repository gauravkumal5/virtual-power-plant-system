package org.gaurav.virtualpowerplantsystem.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.batch")
@Component
@Getter
@Setter
public class BatchConfig {
    private int size;
}
