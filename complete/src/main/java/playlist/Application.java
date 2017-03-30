package playlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableAutoConfiguration(exclude = {JndiConnectionFactoryAutoConfiguration.class,DataSourceAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class})

@SpringBootApplication
@EnableJpaRepositories//("playlist.repositories")
public class Application{
	
    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }
    
}