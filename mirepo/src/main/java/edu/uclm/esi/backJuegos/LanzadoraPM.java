package edu.uclm.esi.backJuegos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ServletComponentScan
@Import(CorsConfig.class)
public class LanzadoraPM extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(LanzadoraPM.class, args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LanzadoraPM.class);
    }
}
