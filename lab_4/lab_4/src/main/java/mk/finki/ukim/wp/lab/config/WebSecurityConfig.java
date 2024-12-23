//package mk.finki.ukim.wp.lab.config;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    private final PasswordEncoder passwordEncoder;
//
//    public WebSecurityConfig(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/login", "/songs", "/artists", "/songDetails").permitAll()
//                        .requestMatchers("/songs/add", "/songs/edit/**", "/songs/delete/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login") // URL за страната за најава
//                        .defaultSuccessUrl("/songs", true) // По успешна најава
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/songs")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    public CommandLineRunner createUsers(AuthenticationManagerBuilder auth) {
//        return args -> auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password(passwordEncoder.encode("admin")) // Енкодирај ја лозинката
//                .roles("ADMIN");
//    }
//}

//package mk.finki.ukim.wp.lab.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration
//public class WebSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/songs/**").permitAll() // Allow public access to /songs
//                        .requestMatchers("/songs/add", "/songs/edit/**").hasRole("ADMIN") // Only accessible to admin
//                        .requestMatchers("/login").permitAll() // Allow access to login page without authentication
//                        .anyRequest().authenticated() // Other pages require authentication
//                )
//                .formLogin(form -> form
//                        .loginPage("/login") // Use the custom login page
//                        .loginProcessingUrl("/login") // Ensure POST requests are processed correctly
//                        .defaultSuccessUrl("/songs", true) // Redirect to /songs after successful login
//                        .permitAll() // Allow access to login page for everyone
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login") // Redirect to login after logout
//                        .permitAll() // Allow logout access for everyone
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("admin123"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(admin); // Return the in-memory user details manager
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
//        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//        return authManagerBuilder.build();
//    }
//}


package mk.finki.ukim.wp.lab.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/songs", "/artists", "/songDetails").permitAll()
                        .requestMatchers("/songs/add", "/songs/edit/**", "/songs/delete/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // URL за страната за најава
                        .defaultSuccessUrl("/songs", true) // По успешна најава
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/songs")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Избегнување на конфликтите - конфигурирајте ги корисниците само еднаш
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // Криерање корисник "admin" во меморијата
        var admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

       UserDetails moderator = User.builder()
                .username("moderator")
                .password(passwordEncoder.encode("moderator"))
                .roles("MODERATOR") // Овој корисник нема ADMIN улога
                .build();
//
        return new InMemoryUserDetailsManager(admin, moderator);

       // return new InMemoryUserDetailsManager(admin);
    }

    // Користете CommandLineRunner за додавање на корисници (само ако ви е потребно за започнување со некои пример-данни)
    @Bean
    public CommandLineRunner createUsers(UserDetailsService userDetailsService) {
        return args -> {
            // Можете да го додадете корисникот во вашето `InMemoryUserDetailsManager` ако сакате
            // но веќе е конфигуриран преку @Bean
        };
    }
}

