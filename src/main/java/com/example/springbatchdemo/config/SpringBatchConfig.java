package com.example.springbatchdemo.config;

import com.example.springbatchdemo.listener.UserErrorSkipListener;
import com.example.springbatchdemo.listener.UserProcessJobListener;
import com.example.springbatchdemo.model.UserCsv;
import com.example.springbatchdemo.model.UserH2;
import com.example.springbatchdemo.processor.UserDataProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private UserProcessJobListener userProcessJobListener;

    @Autowired
    private UserErrorSkipListener userErrorSkipListener;

    @Autowired
    private UserDataProcessor userDataProcessor;

    @Autowired
    private DataSource h2DataSource;

    private static final String USER_PERSIST_JOB = "USER_PERSIST_JOB";

    private static final String USER_CSV_PROCESSING_STEP = "USER_CSV_PROCESSING_STEP";

    private static final int BATCH_SIZE = 5000;

    @Bean
    public Job persistUserJob() {
        return jobBuilderFactory.get(USER_PERSIST_JOB)
                .incrementer(new RunIdIncrementer())
                .start(getUserProcessingChunckStep())
                .listener(userProcessJobListener)
                .build();
    }

    @Bean
    public Step getUserProcessingChunckStep() {
        return stepBuilderFactory.get(USER_CSV_PROCESSING_STEP)
                .<UserCsv,UserH2>chunk(BATCH_SIZE)
                .reader(getUserCsvReader())
                .processor(userDataProcessor)
                .writer(getUserH2JdbcBatchItemWriter())
                .faultTolerant()
                .retryLimit(2)
                .retry(Throwable.class)
                .listener(userErrorSkipListener)
                .build();
    }

    @Bean
    public FlatFileItemReader<UserCsv> getUserCsvReader() {
        FlatFileItemReader<UserCsv> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setLineMapper(getUserCsvDefaultLineMapper());
        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }

    @Bean
    public DefaultLineMapper<UserCsv> getUserCsvDefaultLineMapper() {
        DefaultLineMapper<UserCsv> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(getDelimitedLineTokenizer());
        defaultLineMapper.setFieldSetMapper(getBeanWrapperFieldSetMapper());
        return defaultLineMapper;
    }

    @Bean
    public DelimitedLineTokenizer getDelimitedLineTokenizer() {
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("Index","User Id","First Name","Last Name","Sex","Email","Phone","Date of birth","Job Title");
        return delimitedLineTokenizer;
    }


    @Bean
    public BeanWrapperFieldSetMapper getBeanWrapperFieldSetMapper(){
        BeanWrapperFieldSetMapper<UserCsv> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(UserCsv.class);
        return beanWrapperFieldSetMapper;
    }

    @Bean
    public JdbcBatchItemWriter<UserH2> getUserH2JdbcBatchItemWriter(){
        JdbcBatchItemWriter<UserH2> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
        jdbcBatchItemWriter.setDataSource(h2DataSource);
        jdbcBatchItemWriter.setSql("INSERT INTO `USER` (ID, FIRST_NAME, LAST_NAME, GENDER, EMAIL, AGE, JOB_TITLE) VALUES (:id, :firstName, :lastName, :gender, :email, :age, :jobTitle)");
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<UserH2>());
        return jdbcBatchItemWriter;
    }

}
