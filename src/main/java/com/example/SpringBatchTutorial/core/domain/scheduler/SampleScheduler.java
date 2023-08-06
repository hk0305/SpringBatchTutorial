package com.example.SpringBatchTutorial.core.domain.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SampleScheduler {

    private final Job helloWorldJob;

    private final JobLauncher jobLauncher;


    /**
     * jobParameters 값이 없으면
     * 동일한 job으로 인식하여
     * job이 실행되지 않을 수 있다.
     *
     */
    @Scheduled(cron = "0 */1 * * * *") // run per minutes
    public void helloWorldJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestItem", new JobParameter(System.currentTimeMillis()))
        );
        jobLauncher.run(helloWorldJob, jobParameters);
    }

}
