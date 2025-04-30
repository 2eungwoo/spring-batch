package study.springbatch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerFileJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job customerFileJob;

    @Scheduled(cron = "*/2 * * * * ?") // 매 2초마다 실행
    public void runJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("datetime", LocalDateTime.now().toString())
                .toJobParameters();

        log.info("========= 2초마다 실행 customerFileJob 실행 =========");
        jobLauncher.run(customerFileJob, jobParameters);
    }
}