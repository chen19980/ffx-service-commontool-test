package tw.com.firstbank.fcbcore.ffx.service.commontool.spring.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import tw.com.firstbank.fcbcore.fcbframework.core.spring.config.DataSourceMain;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "mainEntityManager",
		transactionManagerRef = "mainTransactionManager", basePackages = {"tw.com.firstbank.fcbcore"},
		includeFilters = {
				@ComponentScan.Filter(type = FilterType.ANNOTATION, value = DataSourceMain.class)})
public class DataSourceMainConfig {

	@Bean
	@Primary
	public PlatformTransactionManager mainTransactionManager(Environment env) {
		final var transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(mainEntityManager(env).getObject());
		return transactionManager;
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean mainEntityManager(Environment env) {
		final var em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(mainDataSource());
		em.setPackagesToScan("tw.com.firstbank.fcbcore");

		final var vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(
				Boolean.parseBoolean(env.getProperty("spring.jpa.hibernate.generate-ddl", "false")));
		vendorAdapter.setShowSql(false);
		em.setJpaVendorAdapter(vendorAdapter);

		final HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.format_sql", false);
		properties.put("hibernate.generate_statistics", false);
		properties.put("hibernate.order_inserts", true);
		properties.put("hibernate.order_updates", true);
		properties.put("hibernate.jdbc.batch_size",
				env.getProperty("spring.jpa.properties.hibernate.jdbc.batch_size"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.main")
	public DataSource mainDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

}
