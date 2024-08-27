package tech.enricorampazzo.HiAiBe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.enricorampazzo.HiAiBe.dtos.LoginResponse;
import tech.enricorampazzo.HiAiBe.dtos.LoginUserDto;
import tech.enricorampazzo.HiAiBe.dtos.RegisterUserDto;
import tech.enricorampazzo.HiAiBe.entities.User;
import tech.enricorampazzo.HiAiBe.services.AuthenticationService;
import tech.enricorampazzo.HiAiBe.services.JWTService;

@RestController
@RequestMapping(path="/auth")
public class AuthenticationController {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping(path="/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken,jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
