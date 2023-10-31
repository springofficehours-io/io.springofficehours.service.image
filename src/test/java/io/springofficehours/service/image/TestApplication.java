package io.springofficehours.service.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestApplication {

	@Bean
	public GenericContainer lokiContainer(DynamicPropertyRegistry registry) {
		GenericContainer container = new GenericContainer("grafana/loki").withExposedPorts(3100);
		return container;
	}

	@Bean
	public GenericContainer grafanaContainer(DynamicPropertyRegistry registry) {
		GenericContainer container = new GenericContainer("grafana/grafana")
			.withClasspathResourceMapping("grafana-datasources.yaml",
					"/etc/grafana/provisioning/datasources/datasource.yml", BindMode.READ_ONLY)
			.withClasspathResourceMapping("grafana-dashboards.yaml",
					"/etc/grafana/provisioning/dashboards/dashboard.yml", BindMode.READ_ONLY)
			.withClasspathResourceMapping("microservices-spring-boot-2-1_rev1.json",
					"/etc/grafana/provisioning/dashboards/microservices-spring-boot-2-1_rev1.json", BindMode.READ_ONLY)
			.withClasspathResourceMapping("grafana.ini", "/etc/grafana/grafana.ini", BindMode.READ_ONLY)
			.withExposedPorts(3000);
		return container;
	}

	public static void main(String[] args) {
		SpringApplication.from(Application::main).with(TestApplication.class).run(args);
	}

}
