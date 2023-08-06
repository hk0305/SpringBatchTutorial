package com.example.SpringBatchTutorial.job.dbDataReadWrite;

import com.example.SpringBatchTutorial.SpringBatchTestConfig;
import com.example.SpringBatchTutorial.core.domain.account.AccountRepository;
import com.example.SpringBatchTutorial.core.domain.orders.OrderRepository;
import com.example.SpringBatchTutorial.core.domain.orders.Orders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigrationConfig.class})
class TrMigrationConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void cleanUpEach() {
        orderRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void success_noData() throws Exception {
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals(0, accountRepository.count());
    }


    @Test
    void success_existData() throws Exception {
        // given
        Orders order1 = new Orders(null, "KAKAO gift", 15_000L, LocalDate.now());
        Orders order2 = new Orders(null, "NAVER gift", 20_000L, LocalDate.now());

        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals(2, accountRepository.count());
    }

}
