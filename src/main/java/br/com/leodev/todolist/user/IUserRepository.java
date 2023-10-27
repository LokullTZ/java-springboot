package br.com.leodev.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID>{
    // Spring JPA permite a criação de métodos personalizados para manipulações de dados no banco. O método abaixo, por exemplo, será interpretado pelo Spring JPA como um SELECT na informação determinada.
    // UserModel é o tipo de dado de retorno, findByusername é o nome do método, (String userName) é o tipo e nome do atributo que deverá ser buscado.
    UserModel findByUsername(String userName);
}
