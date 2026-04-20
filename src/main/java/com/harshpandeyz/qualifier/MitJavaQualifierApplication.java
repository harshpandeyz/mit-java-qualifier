package com.harshpandeyz.qualifier;

import com.harshpandeyz.qualifier.config.QualifierProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(QualifierProperties.class)
public class MitJavaQualifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(MitJavaQualifierApplication.class, args);
    }
}
