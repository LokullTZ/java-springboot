package br.com.leodev.todolist.task;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


// <TaskModel = Entidade, UUID = Atributo Chave-Primária>
// extends = herança, herdando características da classe JpaRepository
public interface ITaskRepository extends JpaRepository<TaskModel, UUID>{
    List<TaskModel> findByIdUser(UUID idUser);
    TaskModel findByid(UUID id);
}
