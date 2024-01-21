    package abbah.ahmed.indatacoretestproject.config;


    import abbah.ahmed.indatacoretestproject.repositories.UserIndatacoreRepo;
    import com.nimbusds.jose.jwk.source.ImmutableSecret;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.ProviderManager;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
    import org.springframework.security.oauth2.jwt.JwtDecoder;
    import org.springframework.security.oauth2.jwt.JwtEncoder;
    import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
    import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import javax.crypto.spec.SecretKeySpec;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(prePostEnabled = true)
    public class SecurityConfig {

        @Value("${secretKey}")
        private String secretKey;

        private final UserIndatacoreRepo repository;

        private static final String[] WHITE_LIST_URL = {
                "/auth/login/**",
                "/auth/register/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"};
        @Autowired
        public SecurityConfig(UserIndatacoreRepo repository) {
            this.repository = repository;
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return username -> (UserDetails) repository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
            return  httpSecurity
                    .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(csrf->csrf.disable())
                    .cors(Customizer.withDefaults())
                    .authorizeHttpRequests(ar->ar.requestMatchers(WHITE_LIST_URL).permitAll())
                    .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults())
                        .oauth2ResourceServer(oa->oa.jwt(Customizer.withDefaults()))
                    .build();
        }
        @Bean
        public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
            var authProvider = new DaoAuthenticationProvider();
            authProvider.setPasswordEncoder(passwordEncoder());
            authProvider.setUserDetailsService(userDetailsService);
            return new ProviderManager(authProvider);
        }


        @Bean
        JwtEncoder jwtEncoder(){
            return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
        }
        @Bean
        JwtDecoder jwtDecoder(){
            SecretKeySpec secretKeySpec=new SecretKeySpec(secretKey.getBytes(),"RSA");
            return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource(){
            CorsConfiguration corsConfiguration=new CorsConfiguration();
            corsConfiguration.addAllowedOrigin("*");
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.addAllowedHeader("*");
            //corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**",corsConfiguration);
            return source;
        }
    }