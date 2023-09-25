package com.example.demo.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TodoDto;
import com.example.demo.entity.Todo;
import com.example.demo.repository.TodoRepository;

@Service
public class TodoService {
	@Autowired
	TodoRepository todoRepository;

	public List<Todo> getTodoByUsername(String username) {
		return todoRepository.findAllByUsername(username);
	}
	
	public Page<Todo> getPageTodoByUsername(String username,Pageable pageable) {
		return todoRepository.findAllByUsername(username,pageable);
	}

	public List<Todo> getTodoListByStateAndUsername(String username, String state) {
		return todoRepository.findAllByStateAndUsername(username, state);
	}

	public Todo getTodoById(String id) {
		return todoRepository.findById(Integer.parseInt(id));
	}

	public void deleteTodoById(String id) {
		Todo todo = todoRepository.findById(Integer.parseInt(id));
		todoRepository.delete(todo);
	}

	public void finishTodoById(String id) {
		Todo todo = todoRepository.findById(Integer.parseInt(id));
		LocalDateTime now = LocalDateTime.now();
		todo.setUpdatetime(now);
		todo.setState("Finish");
		todoRepository.save(todo);
	}

	public Todo editTodoById(String id, TodoDto todoDto) {
		Todo todo = todoRepository.findById(Integer.parseInt(id));
		todo.setTitle(todoDto.getTitle());
		todo.setContent(todoDto.getContent());
		todo.setUsername(todoDto.getUsername());
		todo.setCategory(todoDto.getCategory());
		todo.setDeadline(todoDto.getDeadline());
		LocalDateTime now = LocalDateTime.now();
		todo.setUpdatetime(now);
		todoRepository.save(todo);
		return todo;
	}

	public Todo insertTodo(TodoDto todoDto) {
		Todo todo = new Todo();
		todo.setTitle(todoDto.getTitle());
		todo.setContent(todoDto.getContent());
		todo.setCategory(todoDto.getCategory());
		todo.setDeadline(todoDto.getDeadline());
		todo.setUsername(todoDto.getUsername());
		LocalDateTime now = LocalDateTime.now();
		todo.setCreatetime(now);
		todo.setUpdatetime(now);
		todo.setState("Ready");
		todoRepository.save(todo);
		return todo;
	}

	public void downloadTodoExcel(String username, OutputStream outputStream) {
		final int todoColumns = 6;
		List<Todo> todos = getTodoByUsername(username);
		Workbook wb = new XSSFWorkbook();
		Sheet sh = wb.createSheet();
		Font font = wb.createFont();
		font.setFontName("ＭＳ Ｐゴシック");
		try {
			for (int i = 0; i < todos.size(); i++) {
				Row row = sh.createRow(i);
				if (i > 0) {
					for (int j = 0; j < todoColumns; j++) {
						Cell cell = row.createCell(j);
						switch (j) {
						case 0:
							cell.setCellValue(todos.get(i).getTitle());
							break;
						case 1:
							cell.setCellValue(todos.get(i).getContent());
							break;
						case 2:
							cell.setCellValue(todos.get(i).getCreatetime());
							break;
						case 3:
							cell.setCellValue(todos.get(i).getUpdatetime());
							break;
						case 4:
							cell.setCellValue(todos.get(i).getUsername());
							break;
						case 5:
							cell.setCellValue(todos.get(i).getState());
							break;
						}
					}

				} else {
					for (int j = 0; j < todoColumns; j++) {
						Cell cell = row.createCell(j);
						switch (j) {
						case 0:
							cell.setCellValue("タイトル");
							break;
						case 1:
							cell.setCellValue("コンテンツ");
							break;
						case 2:
							cell.setCellValue("作成時間");
							break;
						case 3:
							cell.setCellValue("更新時間");
							break;
						case 4:
							cell.setCellValue("作成ユーザー");
							break;
						case 5:
							cell.setCellValue("状態");
							break;
						}
					}
				}

			}
			wb.write(outputStream);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} finally {
			try {
				wb.close();
			} catch (Exception ex) {
			}
		}

	}

	public void directory(String username, OutputStream outputStream) {
		Path p = Paths.get("//Users//wadahajimeko//Desktop//Output");
		try {
			if (Files.exists(p)) {
				System.out.println("ファイルまたはディレクトリは存在します");
			} else {
				Files.createDirectory(p);
			}

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		downloadTodoExcel(username, outputStream);
	}
}
