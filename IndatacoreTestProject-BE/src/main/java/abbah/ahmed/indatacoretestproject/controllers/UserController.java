package abbah.ahmed.indatacoretestproject.controllers;

import abbah.ahmed.indatacoretestproject.entities.User;
import abbah.ahmed.indatacoretestproject.repositories.UserIndatacoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@EnableMethodSecurity(prePostEnabled = true)
@CrossOrigin("*")
public class UserController {

    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;
    private UserIndatacoreRepo userIndatacoreRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserIndatacoreRepo userIndatacoreRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userIndatacoreRepo = userIndatacoreRepo;
        this.passwordEncoder = passwordEncoder;
    }

//    @PostMapping("/auth/login")
//    public Map<String,String> login (String email, String password){
//        System.out.println(email+" - "+password);
//        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
//        System.out.println("1");
//        Instant instant=Instant.now();
//        System.out.println("1");
//        String scope = authentication.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.joining(" "));
//        System.out.println("2");
//        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
//                .issuedAt(instant)
//                .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
//                .subject(email)
//                .claim("scope",scope)
//                .build();
//        System.out.println("3");
//        JwtEncoderParameters jwtEncoderParameters=JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(),jwtClaimsSet);
//        System.out.println("4");
//        String jwt=jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
//        System.out.println(jwt);
//        return Map.of("access-token",jwt);
//    }

    @PostMapping("/auth/login")
    public Map<String,String> login(String email, String password) {
        System.out.println("Received login request - Email: " + email + ", Password: " + password);
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            Instant instant=Instant.now();
            String scope = authentication.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.joining(" "));
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
                    .subject(email)
                    .claim("scope",scope)
                    .build();
            JwtEncoderParameters jwtEncoderParameters=JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(),jwtClaimsSet);
            String jwt=jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            System.out.println(jwt);
            return Map.of("access-token", jwt);
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return Map.of("error", "Authentication failed");
        }
    }


    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        try {
            System.out.println(user.getPassword());
            Optional<User> optionalUser = userIndatacoreRepo.findByEmail(user.getEmail());
            if(optionalUser.isPresent()){
                return new ResponseEntity<>(Collections.singletonMap("error", "Email Already Exist"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userIndatacoreRepo.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", "Failed to register user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
