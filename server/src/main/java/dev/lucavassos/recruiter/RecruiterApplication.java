package dev.lucavassos.recruiter;

import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.repository.ContractTypeRepository;
import dev.lucavassos.recruiter.modules.user.entities.ContractTypeName;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.entities.RoleName;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.security.RsaKeyProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class RecruiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruiterApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(
			RoleRepository roleRepository,
			ContractTypeRepository contractTypeRepository
			) {
		return args -> {
			createRoles(roleRepository);
			createContractTypes(contractTypeRepository);
		};
	}

	private static void createRoles(RoleRepository repository) {
		repository.save(Role.builder().name(RoleName.ROLE_RECRUITER).build());
		repository.save(Role.builder().name(RoleName.ROLE_ADMIN).build());
	}

	private static void createContractTypes(ContractTypeRepository repository) {
		repository.save(ContractType.builder().contractTypeName(ContractTypeName.PERMANENT).build());
		repository.save(ContractType.builder().contractTypeName(ContractTypeName.TEMPORARY).build());
	}
}
