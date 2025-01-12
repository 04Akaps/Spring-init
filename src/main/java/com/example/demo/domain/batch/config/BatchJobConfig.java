package com.example.demo.domain.batch.config;


import com.example.demo.domain.batch.model.InputType;
import com.example.demo.domain.batch.model.OutputType;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowStep;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.StepBuilderException;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job simpleJob() {
        /*
            동기적 실행
         */
        return new JobBuilder("simpleJob", jobRepository)
                .start(simpleStep()).next(processStep())
                .build();
    }


    @Bean
    public Job parallelJob() {
        Flow simpleFlow = new FlowBuilder<Flow>("simpleFlow")
                .start(simpleStep()) // simpleStep 먼저 실행
                .build();

        Flow processFlow = new FlowBuilder<Flow>("processFlow")
                .start(processStep()) // processStep을 병렬로 실행
                .build();


        return new JobBuilder("parallelJob", jobRepository)
                .start(processFlow)
                .split(new SimpleAsyncTaskExecutor())
                .add(simpleFlow)
                .end()
                .build();
    }

    @Bean
    public Step simpleStep() {
        return new StepBuilder("simpleStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Executing simple step...");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


    @Bean
    public Step processStep() {
        return new StepBuilder("processStep", jobRepository)
                .<InputType, OutputType>chunk(10, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    private ItemReader<InputType> itemReader() {
        return new ListItemReader<>(List.of(new InputType("data1"), new InputType("data2")));
    }

    private ItemProcessor<InputType, OutputType> itemProcessor() {
        return input -> {
            // 필요한 데이터 변환 로직 추가
            System.out.println("Processing item: " + input.getData());
            return new OutputType(input.getData() + "out put");  // 예시로 변환된 OutputType 객체 반환
        };
    }

    private ItemWriter<OutputType> itemWriter() {
        return items -> {
            for (OutputType item : items) {
                System.out.println("Writing item: " + item.getData());
            }
        };
    }
}
