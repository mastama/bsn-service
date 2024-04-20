package id.co.anfal.bsn.service;

import id.co.anfal.bsn.dto.RegistrationRequestDto;
import id.co.anfal.bsn.entity.EmailTemplateName;
import id.co.anfal.bsn.entity.Token;
import id.co.anfal.bsn.entity.User;
import id.co.anfal.bsn.repository.RoleRepository;
import id.co.anfal.bsn.repository.TokenRepository;
import id.co.anfal.bsn.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequestDto req) throws MessagingException {
        log.info("START registration request: {}",  req.getFirstName());
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));
        User user = new User();
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setAccountLocked(false);
        user.setEnabled(false);
        user.setRoles(List.of(userRole));

        //save to db
        userRepository.save(user);
        log.info("Save User to db is success");
        sendValidationEmail(user);
        log.info("END registration request: {}",  req.getFirstName());
    }

    private void sendValidationEmail(User user) throws MessagingException {
        log.info("START validation email: {}", user.getEmail());
        var newToken = generateAndSaveActivationToken(user);
        log.info("Validation email newToken: {}", newToken);
        // send email
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );
        log.info("END send validation email is success: {}", newToken);
    }

    private String generateAndSaveActivationToken(User user) {
        log.info("START generateAndSaveActivationToken");
        //generate a token
        String generatedToken = generateActivationCode(6);
        Token token = new Token();
        token.setToken(generatedToken);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        token.setUser(user);
        // save token
        tokenRepository.save(token);
        log.info("END generateAndSaveActivationToken is success: {}", generatedToken);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        log.info("START generateActivationCode: {}", length);
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length()); // 0 to 9
            codeBuilder.append(characters.charAt(randomIndex));
        }
        log.info("END generateActivationCode is success: {}", codeBuilder);
        return codeBuilder.toString();
    }
}
