package com.github.kazuki43zoo.sample.domain.repository;

import com.github.kazuki43zoo.sample.domain.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.List;

@Repository
@Primary
public class TodoApiRepository implements TodoRepository {

    @Value("${api.baseUrl:http://localhost:9081/api}/todos")
    String todosResourceUrl;

    @Value("${api.baseUrl:http://localhost:9081/api}/todos/{todoId}")
    String todoResourceUrl;

    @Autowired
    RestOperations restOperations;

    @PostAuthorize("permitAll()")
    @Override
    public Todo findOne(String todoId) {
        return restOperations.getForObject(todoResourceUrl, Todo.class, todoId);
    }

    @Override
    public List<Todo> findAll(String usename) {
        ResponseEntity<List<Todo>> responseEntity =
                restOperations.exchange(RequestEntity.get(URI.create(todosResourceUrl)).build(), new ParameterizedTypeReference<List<Todo>>() {
                });
        return responseEntity.getBody();
    }

    @Override
    public void create(Todo todo) {
        restOperations.postForLocation(todosResourceUrl, todo);
    }

    @Override
    public boolean update(Todo todo) {
        restOperations.put(todoResourceUrl, todo, todo.getTodoId());
        return true;
    }

    @Override
    public void delete(Todo todo) {
        restOperations.delete(todoResourceUrl, todo.getTodoId());
    }

}