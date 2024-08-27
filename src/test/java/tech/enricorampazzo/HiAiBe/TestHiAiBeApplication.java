package tech.enricorampazzo.HiAiBe;

import org.springframework.boot.SpringApplication;

public class TestHiAiBeApplication {

	public static void main(String[] args) {
		SpringApplication.from(HiAiBeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
