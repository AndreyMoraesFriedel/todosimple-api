package com.andreymoraes.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andreymoraes.todosimple.models.Task;
import com.andreymoraes.todosimple.models.User;
import com.andreymoraes.todosimple.repositories.TaskRepository;

@Service
public class TaskServices {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserServices userServices;

    @Transactional
    public Task findById(Long id){
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException(
            "Tarefa Não Encontrada! Id" + id + ", Tipo: " + Task.class.getName()));
    }

    @Transactional
    public Task create(Task obj){
        User user = this.userServices.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj){
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    @Transactional
    public void delete(Long id){
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possivel excluir pois há entidades relacionadas!");
        }
    }

}
