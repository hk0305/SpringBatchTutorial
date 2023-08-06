package com.example.SpringBatchTutorial.job.dbDataReadWrite;

import com.example.SpringBatchTutorial.core.domain.account.AccountRepository;
import com.example.SpringBatchTutorial.core.domain.account.Accounts;
import com.example.SpringBatchTutorial.core.domain.orders.OrderRepository;
import com.example.SpringBatchTutorial.core.domain.orders.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

/**
 * desc: 주문 테이블 -> 정산 테이블 데이터 이관
 * run env: --spring.batch.job.names=trMigrationJob
 */
@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {

    private final OrderRepository orderRepository;

    private final AccountRepository accountRepository;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(ItemReader<Orders> trOrdersReader,
                                ItemProcessor<Orders, Accounts> trOrdersProcessor,
                                ItemWriter<Accounts> trOrdersWriter
    ) {
        return stepBuilderFactory.get("trMigrationStep")
                // Orders Entity로 읽어오며, Account Entity로 처리. 5개 데이터 단위로 처리
                .<Orders, Accounts>chunk(5)
                .reader(trOrdersReader)
//                .writer(System.out::println)
                .processor(trOrdersProcessor)
                .writer(trOrdersWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader")
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(5)    // chunk size와 동일하게 작성
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> trOrdersProcessor() {
        return Accounts::new;
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<Accounts> trOrdersWriter() {
        return new RepositoryItemWriterBuilder<Accounts>()
                .repository(accountRepository)
                .methodName("save")
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<Accounts> trOrdersWriterCustom() {
        return items -> items.forEach(accountRepository::save);
    }

}
