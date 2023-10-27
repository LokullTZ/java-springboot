package br.com.leodev.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.leodev.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Servlet é base para a maioria dos frameworks web existentes para Java.
@Component // Classe mais genérica de gerenciamento utilizada pelo Spring. Essa anotation é necessária porque é com ela que definimos que o Spring DEVE passar pelo filtro antes de encaminhar a requisição para o restante da rota.
public class UserAuth extends OncePerRequestFilter{

    @Autowired 
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // request.getServletPath(): Permite capturar as informações da rota utilizada na requisição.
        var checkRoute = request.getServletPath();

        if(checkRoute.startsWith("/tasks/")){
            // getHeader() pega as informações de autorização que geralmente são enviadas através do cabeçalho da requisição.
            var authorization = request.getHeader("Authorization");
            
            // Métodos substring() e trim() utilizados para tratamento da String recebida. substring remove a quantidade de caracteres informada e o trim remove espaços em branco.
            var userAuthEncoded = authorization.substring("Basic".length()).trim();
    
            // Base64, do pacote util do java, é uma classe que nos permite tratar informações encriptadas. No caso do código de authorization recebido através do header da requisição, os dados de autorização são encriptados em código de base-64.
            // O retorno do getDecoder.decode() é um array de bytes no Java. Por esse motivo que as informações foram atribuídas a um array de byte.
            byte[] userAuthDecoded = Base64.getDecoder().decode(userAuthEncoded);
    
            // A utilização da classe String para instanciação de um objeto tipo String com base nas informações armazenadas no userAuthDecoded é necessária porque as informações retornadas estão no formato byte, e ainda não representam o valor desejado!
            // Com essa conversão, finalmente teremos a informação de password do usuário.
            var userAuthDecodedToString = new String(userAuthDecoded);
    
            // DADOS DO USUÁRIO
            // split() busca pelo argumento informando em uma String, removendo toda e qualquer ocorrência do elemento na String. Retorna as palavras restantes, separadamente, em um array.
            String[] userCredentials = userAuthDecodedToString.split(":");
            String username = userCredentials[0];
            String password = userCredentials[1];
    
            // VALIDANDO USUÁRIO
            var user = this.userRepository.findByUsername(username);
            if(user == null){
                response.sendError(401);
            }else{
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVerify.verified){
                    request.setAttribute("idUser", user.getId()); // Recuperando informações da requisição e definindo que a informação de ID do usuário será enviada adiante, no caso para a camada Controller.
                    filterChain.doFilter(request, response);
                }else{
                    response.sendError(401);
                }
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }
    
}
