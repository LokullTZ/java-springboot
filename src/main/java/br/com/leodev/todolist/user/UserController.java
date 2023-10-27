package br.com.leodev.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // HttpStatus do pacote http possui uma série de status code pré-definidos que podem ser utilizados como retorno das requisições.
import org.springframework.http.ResponseEntity; // ResponseEntity permite criar e gerenciar diferentes retornos para um mesmo tipo de requisição, como status code 200, status code 400 e assim por diante. (retornos HTTP)
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    
    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null){
            // Status Code para resposta em caso de erro (usuário já existe)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
        }

        // Bcrypt é a lib que possui as funções de criptografar a senha.
        // withDefaults().hashToString(força da criptografia, recurso a ser criptografado (o rescurso precisa ser um array de caracteres, por isso o uso do toCharArray()))
        var hashedUserPassword = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(hashedUserPassword);


        var userCreated = this.userRepository.save(userModel);
        // Status Code para resposta em caso de sucesso (usuário criado)
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
