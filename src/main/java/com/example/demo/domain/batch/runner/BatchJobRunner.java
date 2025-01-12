package com.example.demo.domain.batch.runner;

import com.example.demo.domain.batch.config.BatchJobConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchJobRunner {

    private final JobLauncher jobLauncher;
    private final Job simpleJob;
    private final Job parallelJob;

    // constructor 역할을 초기에 한번만 실행
    @PostConstruct
    public void runBatchJobInBackground() {
        try {

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("timestamp", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();

            jobLauncher.run(parallelJob, jobParameters);
            System.out.println("Batch job completed successfully!");
        } catch (JobExecutionException e) {
            e.printStackTrace();
            System.out.println("Batch job failed: " + e.getMessage());
        }
    }




}