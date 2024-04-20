package id.co.anfal.bsn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //if u use @EntityListeners u need add this annotation
public class BsnApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsnApplication.class, args);
	}

}
