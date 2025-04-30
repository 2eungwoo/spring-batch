package study.springbatch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener  implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("job 시작 : {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("job 종료 : {}, 상태 : {}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus()
        );
    }
}

// 여기서는 단순히 로그만 찍지만, 메일이나 슬랙으로 알람을 쏜다던지 하는 작업을 할 수 있음