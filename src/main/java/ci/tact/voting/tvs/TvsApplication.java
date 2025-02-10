package ci.tact.voting.tvs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TvsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TvsApplication.class, args);
	}

}
