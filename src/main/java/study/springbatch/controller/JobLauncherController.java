package study.springbatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
public class JobLauncherController {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final Job helloJob;
    private final Job customerFileJob;

    @GetMapping("/batch/hello")
    public ResponseEntity<?> runHelloJob(){
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .addString("datetime", LocalDateTime.now().toString())
                .toJobParameters();

        JobExecution jobExecution = null;
        try {
            jobExecution = jobLauncher.run(helloJob, jobParameters);
            return ResponseEntity.ok("job 실행 완료. 상태 : "+jobExecution.getStatus());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("job 실행 실패. 상태 : "+e.getMessage());
        }

    }

    @GetMapping("/batch/customer-file")
    public ResponseEntity<?> runCustomerFileJob(){
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .addString("datetime", LocalDateTime.now().toString())
                .toJobParameters();

        JobExecution jobExecution = null;
        try {
            jobExecution = jobLauncher.run(customerFileJob, jobParameters);
            return ResponseEntity.ok("job 실행 완료. 상태 : "+jobExecution.getStatus());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("job 실행 실패. 상태 : "+e.getMessage());
        }

    }
}
