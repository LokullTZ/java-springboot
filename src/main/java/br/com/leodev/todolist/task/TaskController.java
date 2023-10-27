package br.com.leodev.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.leodev.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired // O próprio Spring gerenciará os ciclos de vida e instanciações necessárias para os objetos da classe dentro do projeto.
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity createTask(@RequestBody TaskModel taskModel, HttpServletRequest request, HttpServletResponse response){
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser); // (UUID) foi utilizado para realizar uma CONVERSÃO de tipo, de object para UUID.

        var currentDate = LocalDateTime.now(); // Capturando a data atual.
        if(currentDate.isAfter(taskModel.getStartAt())){ // Verificando se a data atual é superior a data informada como início da tarefa, utilizando o método isAfter()
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início não pode ser inferior a data de criação");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){ // Verificando se a data de início da tarefa é inferior a data de conclusão (Horas e minutos são, automaticamente, considerados. Exe.: Data de início e término representam o mesmo dia porém a hora de início é superior a hora de término.)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início não pode ser superior a data de término da tarefa!");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var userTasks = this.taskRepository.findByIdUser((UUID) idUser);
        return userTasks;
    }

    @PutMapping("/{id}") // Informando o nome do próprio argumento, entre chaves, para que o Spring reconheça como um atributo de Path Variable e possa realizar a substituição pelo valor de forma automática.
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, HttpServletResponse response, @PathVariable UUID id){ // @PathVariable permite capturar dados enviados na URL da requisição, como por exemplo o ID de uma task que será modificada.
        var idUser = request.getAttribute("idUser");
        
        var task = this.taskRepository.findById(id).orElse(null); // Método orElse() permite definir outra possibilidade de valor para a expressão. No caso, ou salva a task ou salva nulo.

        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A task informada não existe. Verifique a ID da task.");
        }

        if(idUser.equals(task.getIdUser())){
            Utils.copyNonNullProperties(taskModel, task);
            return ResponseEntity.status(HttpStatus.OK).body(this.taskRepository.save(task));
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você não tem permissão para alterar tasks que não sejam de sua propriedade.");
        }
        
    }
}
