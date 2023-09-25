package com.example.demo.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PageAbleDto;
import com.example.demo.dto.TodoDto;
import com.example.demo.entity.Todo;
import com.example.demo.service.TodoService;
import com.example.demo.service.UserService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class TodoController {
	@Autowired
	UserService userService;
	@Autowired
	TodoService todoService;

	@GetMapping("/todo/list/{username}")
	public List<Todo> user(@PathVariable String username, Model model) {
		List<Todo> todoUserList = todoService.getTodoByUsername(username);
		return todoUserList;
	}
	// ページネーション　開発中
	@GetMapping("/todo/listpage/{username}")
	public List<Todo> pageUser(@RequestBody PageAbleDto pageAbleDto) {
		Pageable pageable = Pageable.ofSize(pageAbleDto.getSize()).withPage(pageAbleDto.getPage());
		Page<Todo> todoUserPageList = todoService.getPageTodoByUsername(pageAbleDto.getUsername(),pageable);
		List<Todo> todoUserList = todoUserPageList.getContent();
		return todoUserList;
	}

	@PostMapping("/todo/insert/{username}")
	public Todo todoInsert(@RequestBody TodoDto todoDto) {
		return todoService.insertTodo(todoDto);
	}

	@PostMapping("todo/finish/{id}")
	public void finishTodo(@PathVariable String id) {
		todoService.finishTodoById(id);
	}
	
	@PostMapping("todo/edit/{id}")
	public Todo editTodo(@PathVariable String id,@RequestBody TodoDto todoDto) {
		Todo todo = todoService.editTodoById(id,todoDto);
		return todo;
	}

	@PostMapping("todo/delete/{id}")
	public void deleteTodo(@PathVariable String id) {
		todoService.deleteTodoById(id);
	}

	@PostMapping(value = "todo/download/{username}")
	public void downloadTodo(@PathVariable String username, HttpServletResponse response) throws IOException {
		try (ServletOutputStream os = response.getOutputStream();) {
			LocalDateTime date1 = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String fdate1 = dtformat.format(date1);
			FileOutputStream todoXlsx = new FileOutputStream(
					"//Users//wadahajimeko//Desktop//Output//todo_" + fdate1 + ".xlsx");
			todoService.directory(username, todoXlsx);
		}
	}
}
