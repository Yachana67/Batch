package com.batchh.insurance.config;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;

import com.batchh.insurance.Model.Insurance;
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job fetchDataJob() {
        return jobBuilderFactory.get("fetchDataJob")
                .incrementer(new RunIdIncrementer())
                .start(fetchDataStep())
                .build();
    }

    @Bean
    public Step fetchDataStep() {
        return stepBuilderFactory.get("fetchDataStep")
                .<Insurance, Insurance>chunk(20)
                .reader(excelItemReader())
                .processor(yourItemProcessor())
                .writer(consoleItemWriter())
                .build();
    }

    @Bean
    public ItemProcessor<Insurance, Insurance> yourItemProcessor() {
        return new LimitingItemProcessor();
    }
    @Bean
    public ItemWriter<Insurance> consoleItemWriter() {
        return items -> {
            for (Insurance item : items) {
                System.out.println("Writing item: " + item);
            }
        };
    }
    @Bean
    public ItemReader<Insurance> excelItemReader() {
        PoiItemReader<Insurance> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource("InsuranceData.xlsx"));
        reader.setRowMapper(excelRowMapper());

        return reader;
    }

    public RowMapper<Insurance> excelRowMapper() {
        return rowSet -> {
            Insurance insuranceData = new Insurance();

            insuranceData.setCategory(rowSet.getCurrentRow()[6]);
            insuranceData.setNAME(rowSet.getCurrentRow()[7]);
            insuranceData.setEmail(rowSet.getCurrentRow()[16]);

            return insuranceData;
        };
    }
    @Scheduled(fixedDelay = 20000) // Run every hour
    public void runJob() throws Exception {
        
        jobLauncher.run(fetchDataJob(), new JobParametersBuilder().toJobParameters());
    }

  
}
