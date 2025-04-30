package study.springbatch.config;

import study.springbatch.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CustomerFileJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job customerFileJob(){
        return new JobBuilder("customer-file-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener()) // 잡이 실행되고 종료되고 등의 리스너 등록 가능. 로거 리스너를 만들어서 로그 콜백을 받아보자
                .start(customerFileStep())
                .build();
    }

    @Bean
    public Step customerFileStep() {
        return new StepBuilder("customer-file-step",jobRepository)
                .<Customer, Customer>chunk(10, platformTransactionManager)
                .reader(customerFileReader())
                .processor(customerFileProcessor())
                .writer(customerFileWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerFileReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customer-file-reader")
                .resource(new ClassPathResource("customers.csv"))
                .delimited()
                .names("id", "name", "email")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Customer.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<Customer, Customer> customerFileProcessor() {
        return customer -> {
            // 등록 날짜만 프로세싱 하도록 예제 설정
            customer.setRegisteredAt(LocalDateTime.now());
            return customer;
        };
    }

    @Bean
    public ItemWriter<Customer> customerFileWriter() {
        return items -> {
            for (Customer c : items) {
                // db insert
                log.info("Customer 저장 : {}", c);
            }
        };
    }



}
