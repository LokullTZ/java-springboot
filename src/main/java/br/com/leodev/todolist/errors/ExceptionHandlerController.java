package br.com.leodev.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    
    @ExceptionHandler(HttpMessageNotReadableException.class) // @ExceptionHandler é a anotation que determina que o método tem por responsabilidade o tratamento da exceção que estiver determinada, nesse caso a HttpNotReadableException.class passada como argumento para a anotation.
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage()); // O método getMostSpecificCause() faz com que o método não espere mais receber um objeto JSON eliminando a referência textual no retorno para o usuário. Sendo assim, somente a mensagem definida no throw da exception será exibida.
    }

}
