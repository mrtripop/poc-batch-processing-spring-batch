package com.mrtripop.learn.SpringBatchProcessing.configs;

import com.mrtripop.learn.SpringBatchProcessing.models.PeopleEntity;
import com.mrtripop.learn.SpringBatchProcessing.processors.PersonItemProcessor;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class BatchConfig {

  @Bean
  public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  public FlatFileItemReader<PeopleEntity> peopleItemReader() {
    return new FlatFileItemReaderBuilder<PeopleEntity>()
        .name("peopleItemReader")
        .resource(new ClassPathResource("sample-data.csv"))
        .delimited() // Specify the delimiter
        .names("firstName", "lastName") // Specify the column names
        .targetType(PeopleEntity.class) // Specify the target type
        .linesToSkip(1) // Skip the header row
        .build();
  }

  @Bean
  public PersonItemProcessor peopleItemProcessor() {
    return new PersonItemProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<PeopleEntity> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<PeopleEntity>()
        .sql("INSERT INTO peoples (first_name, last_name) VALUES (:firstName, :lastName)")
        .dataSource(dataSource)
        .beanMapped()
        .build();
  }

  @Bean
  public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
    return new JobBuilder("importUserJob", jobRepository)
            .listener(listener)
            .start(step1)
            .build();
  }

  @Bean
  public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                    FlatFileItemReader<PeopleEntity> reader, PersonItemProcessor processor, JdbcBatchItemWriter<PeopleEntity> writer) {
    return new StepBuilder("step1", jobRepository)
            .<PeopleEntity, PeopleEntity>chunk(3, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
  }
}
