package tw.com.firstbank.fcbcore.ffx.service.commontool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@ComponentScan(basePackages = "tw.com.firstbank.fcbcore")
@EnableFeignClients
@EnableRetry
@Slf4j
public class FfxServiceCommonToolServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FfxServiceCommonToolServiceApplication.class, args);
	}
}
