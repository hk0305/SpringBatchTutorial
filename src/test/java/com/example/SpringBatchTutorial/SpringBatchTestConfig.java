package com.example.SpringBatchTutorial;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * 전역 기본 Test Config
 */
@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class SpringBatchTestConfig {
}
