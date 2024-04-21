package id.co.anfal.bsn.controller;

import id.co.anfal.bsn.dto.AuthenticationRequest;
import id.co.anfal.bsn.dto.AuthenticationResponse;
import id.co.anfal.bsn.dto.RegistrationRequestDto;
import id.co.anfal.bsn.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequestDto req) throws MessagingException {
        log.info("Incoming registration user: {}", req.getLastName());
        authService.register(req);
        log.info("Outgoing registered user: {}", req.getLastName());
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest req) {
        log.info("Incoming authentication: {}", req.getEmail());
        log.info("Outgoing authentication: {}", req.getEmail());
        return ResponseEntity.ok(authService.authenticate(req));
    }

    @GetMapping(value = "/activate-account")
    public void confirm(@RequestParam String token) throws MessagingException {
        log.info("Incoming confirm activation account: {}", token);
        authService.activateAccount(token);
        log.info("Outgoing confirm activation account: {}", token);
    }
}
