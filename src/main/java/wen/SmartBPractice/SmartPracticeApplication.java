package wen.SmartBPractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories
//@Slf4j
//public class SmartPracticeApplication {
public class SmartPracticeApplication {//implements ApplicationRunner {

//	@Autowired
//	ClientRepository cr;

	public static void main(String[] args) {
		SpringApplication.run(SmartPracticeApplication.class, args);
	}
/*
	@Override
	public void run(ApplicationArguments args) throws Exception {
		initClient();
	}

	public void initClient() {
		Client c = Client.builder().name("CHT")
				.address("台北市").create_by("wen")
				.build();

		cr.save(c);

		log.info("Client: {}", c);
	}
*/
}
