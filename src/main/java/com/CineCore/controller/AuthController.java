package com.CineCore.controller;

import com.CineCore.config.TokenService;
import com.CineCore.entity.User;
import com.CineCore.exception.UsernameOrPasswordInvalidException;
import com.CineCore.mapper.UserMapper;
import com.CineCore.request.LoginRequest;
import com.CineCore.request.UserRequest;
import com.CineCore.response.LoginResponse;
import com.CineCore.response.UserResponse;
import com.CineCore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinecore/auth")
@RequiredArgsConstructor
@Tag(name="Autenticação", description = "Recurso responsável pelo gerenciamento do cadastro e login de usuários.")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Operation(summary = "Cria um novo usuário", description = "Rota cria um novo usuário no banco de dados.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na criação do usuário.")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        User savedUser = userService.saveUser(UserMapper.toUser(userRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserResponse(savedUser));
    }

    @Operation(summary = "Realiza o login de um usuário cadastrado", description = "Rota realiza o login de um usuário cadastrado no banco de dados.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso. Gera um token JWT e devolve ao usuário na resposta da requisição."),
            @ApiResponse(responseCode = "400", description = "Erro na autenticação do usuário. Devolve uma mensagem de 'Usuário ou senha inválidos.'")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {

            UsernamePasswordAuthenticationToken userAndPassword = new UsernamePasswordAuthenticationToken(
                    loginRequest.email(),
                    loginRequest.password()
            );

            Authentication authenticate = authenticationManager.authenticate(userAndPassword);

            User user = (User) authenticate.getPrincipal();

            String token = tokenService.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (BadCredentialsException e) {
            throw new UsernameOrPasswordInvalidException("Usuário ou senha inválidos.");
        }
    }
}
