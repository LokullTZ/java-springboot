package br.com.leodev.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Getters and Setters criados automaticamente, seguindo a convesão, através da lob Lombok.
@Entity(name = "tb_tasks") // ORM adicionado pelo Spring JPA (jakarta persistence). Mapeará a classe como uma entidade do banco, transformando-a em uma tabela e seus atributos em campos.
public class TaskModel {
    @Id // Anotation do Jakarta Persistence (Spring JPA) que permite definir o atributo vinculado como chave primária da tabela.
    @GeneratedValue(generator = "UUID") // Anotaion do Jakarta que informa ao Spring que ele deverá gerenciar a geração do conteúdo do atributo (como se fosse um auto_increment, por exemplo). A propriedade generator recebe o valor da estrutura em que se deseja que os valores sejam gerados, nesse caso UUID.
    private UUID id;
    private String description;
    @Column(length = 50) // Propriedade length para a anotation @Column determina a quantidade máxima de caracteres permitidos para os dados dessa coluna no bando de dados.
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    
    private UUID idUser;

    @CreationTimestamp // Essa anotation do Hibernate, um dos principais ORM utilziados para Java, permite o registro do momento de criação do recurso em nossa base de dados, de forma automática.
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception {
        if(title.length() > 50){
            throw new Exception("O campo titulo nao pode conter mais de cinquenta caracteres!");
        }
        this.title = title;
    }
}
