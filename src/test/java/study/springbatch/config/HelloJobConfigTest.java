package study.springbatch.config;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest // batch 테스트 할 때 필요함
@SpringBootTest
class HelloJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    // 이거는 빈 생성이 자체적으로 안돼있기 때문에 간단하게 작성해서 사용해주자 라는데 안해도 됨 버전 바뀌면서 되는듯

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    @Qualifier("helloJob") // 특정 Job을 명시적으로 주입
    private Job helloJob;


    @Test
    void helloJobTest() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .addString("datetime", LocalDateTime.now().toString())
                .toJobParameters();
        jobLauncherTestUtils.setJob(helloJob);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

}