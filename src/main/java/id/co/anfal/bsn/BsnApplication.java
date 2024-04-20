package id.co.anfal.bsn;

import id.co.anfal.bsn.entity.Role;
import id.co.anfal.bsn.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing //if u use @EntityListeners u need add this annotation
@EnableAsync
public class BsnApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsnApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeRoles(@Autowired RoleRepository roleRepository){
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
		};
	}

}
