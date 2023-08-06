package com.example.SpringBatchTutorial.job.customStep.multipleStep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc: 다중 step을 사용하기 및 step to step 데이터 전달
 * run env: --spring.batch.job.namesmultipleStepJob
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultipleStepJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job multipleStepJob(Step multipleStep1, Step multipleStep2, Step multipleStep3) {
        return jobBuilderFactory.get("multipleStepJob")
                .incrementer(new RunIdIncrementer())
                .start(multipleStep1)
                .next(multipleStep2)
                .next(multipleStep3)
                .build();
    }

    @JobScope
    @Bean
    public Step multipleStep1() {
        return stepBuilderFactory.get("multipleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @JobScope
    @Bean
    public Step multipleStep2() {
        return stepBuilderFactory.get("multipleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("step2");

                    ExecutionContext executionContext = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    executionContext.put("someKey", "hello!!");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @JobScope
    @Bean
    public Step multipleStep3() {
        return stepBuilderFactory.get("multipleStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info("step3");

                    ExecutionContext executionContext = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    log.info(executionContext.get("someKey") + "");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
