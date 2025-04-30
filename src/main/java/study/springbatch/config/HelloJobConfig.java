package study.springbatch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class HelloJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job helloJob() {
        return new JobBuilder("hello_job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(helloStep1())
                .next(helloStep2())
                .build();
    }

    @Bean // step은 StepBuilder로 생성 가능
    public Step helloStep1() {
        return new StepBuilder("hello_step_1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("helloStep1() call");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean // step은 StepBuilder로 생성 가능
    public Step helloStep2() {
        return new StepBuilder("hello_step_2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("helloStep2() call");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

}
