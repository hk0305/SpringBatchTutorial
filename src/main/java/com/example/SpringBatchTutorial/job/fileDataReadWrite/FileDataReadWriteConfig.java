package com.example.SpringBatchTutorial.job.fileDataReadWrite;

import com.example.SpringBatchTutorial.job.fileDataReadWrite.dto.Player;
import com.example.SpringBatchTutorial.job.fileDataReadWrite.dto.PlayerYears;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * 파일 처리
 * --spring.batch.job.names=fileReadWriteJob
 */
@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fileReadWriteJob(Step fileReadWriteStep) {
        return jobBuilderFactory.get("fileReadWriteJob")
                .incrementer(new RunIdIncrementer())
                .start(fileReadWriteStep)
                .build();
    }

    @JobScope
    @Bean
    public Step fileReadWriteStep(ItemReader<Player> playerItemReader
            , ItemProcessor<Player, PlayerYears> playerItemProcessor
            , ItemWriter<PlayerYears> playerItemWriter
    ) {
        return stepBuilderFactory.get("fileReadWriteStep")
                .<Player, PlayerYears>chunk(5)
                .reader(playerItemReader)
//                .writer(System.out::println)  // 기본 동작 확인
                .processor(playerItemProcessor)
                .writer(playerItemWriter)
                .build();
    }

    /**
     * csv 파일을 읽어와 Player 객체로 만들기
     */
    @StepScope
    @Bean
    public FlatFileItemReader<Player> playerItemReader() {
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerItemReader")
                .resource(new FileSystemResource("Players.csv"))    // 프로젝트 root 기준
                .lineTokenizer(new DelimitedLineTokenizer())    // 라인 구분자
                .fieldSetMapper(new PlayerFieldSetMapper())
                .linesToSkip(1) // 첫번째 줄은 타이틀이므로 스킵
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Player, PlayerYears> playerItemProcessor() {
        return PlayerYears::new;
    }

    /**
     * PlayerYears를 csv 파일로 만들기
     */
    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerItemWriter() {
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"ID", "lastName", "position", "yearsExperience"});
        fieldExtractor.afterPropertiesSet();

        // csv 구분자 설정
        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FileSystemResource outputResource = new FileSystemResource("players_output.txt");
        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }

}
