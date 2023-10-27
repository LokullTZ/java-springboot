package br.com.leodev.todolist.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Data
@Entity(name = "tb_users") // Anotation para definir que a classe será utilizada como tabela na base de dados. O argumento name permite definir um nome diferente do nome original da classe para criação da tabela.
public class UserModel {
    @Id // Anotation que permite definir a chave-primaria da tabela.
    @GeneratedValue(generator = "UUID") // Anotation que informa ao Spring que a geração de valor deve ser automática e gerenciada por ele. O argumento define de que forma será gerado, no caso uma estrutura UUID.
    private UUID id;
    @Column(name = "nickname", unique = true) // Anotation que permite definir um nome específico para a coluna no banco de dados. Caso não seja utilizado, o nome da coluna será exatamente o nome do atributo.
    private String username;
    private String name;
    private String password;

    @CreationTimestamp // Anotation que possui as configurações de data e hora para registro do momento de criação de um recurso em nosso bando de dados.
    private LocalDateTime createdAt; // Classe java LocalDateTime, do pacote time, servindo como tipo de dado para o atributo createdAt.
}
