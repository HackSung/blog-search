package blog.data.jpa.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackages = ["blog.data.jpa"])
@EnableJpaRepositories(basePackages = ["blog.data.jpa"])
@EnableJpaAuditing
class JpaConfig