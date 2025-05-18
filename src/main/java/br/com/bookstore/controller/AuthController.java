package br.com.bookstore.controller;

import br.com.bookstore.config.security.JwtGenerator;
import br.com.bookstore.exception.error.ResourceDetails;
import br.com.bookstore.model.dtos.Auth;
import br.com.bookstore.model.entity.CustomUserDetails;
import br.com.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Services responsible for logging a user in or registering them in the bookstore application")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    
    private final JwtGenerator jwtGenerator;
    
    private final UserService userService;

    @Operation(
            summary = "Authenticated user",
            description = "Authenticated user",
            responses = {@ApiResponse(description = "Authenticated user", responseCode = "200")
            })
    @GetMapping("/login")
    public ResponseEntity<Auth> login(@RequestHeader String email,  @RequestHeader String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails)  userService.loadUserByUsername(email);

        String token = jwtGenerator.generateToken(userDetails);
        Date expiration = jwtGenerator.extractExpiration(token);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return ResponseEntity.ok(new Auth(token, authorities.toString(), expiration, userDetails.getName()));
    }

    @Operation(
            summary = "Register a user in the bookstore application",
            description = "Register a user in the bookstore application",
            responses = {@ApiResponse(description = "User created successfully", responseCode = "201"),
                         @ApiResponse(description = "Business rule violated", responseCode = "400", content = @Content(schema = @Schema(implementation = ResourceDetails.class)))
            })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestHeader String email,  @RequestHeader String password, @RequestHeader String name) {
        userService.save(email, password, name);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}