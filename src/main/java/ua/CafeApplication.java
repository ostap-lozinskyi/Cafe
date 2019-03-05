package ua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.model.entity.Role;
import ua.model.entity.User;
import ua.repository.UserRepository;

import java.util.List;

@SpringBootApplication
public class CafeApplication implements WebMvcConfigurer {

    private static final String ADMIN = "admin";

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(CafeApplication.class, args);
        addAdmin(run);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true);
        argumentResolvers.add(resolver);
        argumentResolvers.add(resolver);
    }

    private static void addAdmin(ConfigurableApplicationContext run) {
        UserRepository repository = run.getBean(UserRepository.class);
        User user = repository.findUserByEmail(ADMIN);
        if (user == null) {
            PasswordEncoder encoder = run.getBean(PasswordEncoder.class);
            user = new User();
            user.setEmail(ADMIN);
            user.setPassword(encoder.encode(ADMIN));
            user.setRole(Role.ROLE_ADMIN);
            repository.save(user);
        } else {
            user.setRole(Role.ROLE_ADMIN);
            repository.save(user);
        }
    }
}
