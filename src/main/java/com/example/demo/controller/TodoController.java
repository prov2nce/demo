package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	@GetMapping("/test")
	public ResponseEntity<?> testTodo(){
		String str = service.testService();	//�׽�Ʈ ���� ���
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		
		return ResponseEntity.ok().body(response);
		
	}
	
	//����
	@GetMapping("/testId")
	public ResponseEntity<?> testTodoId(){
		String str = service.testIdService();	//�׽�Ʈ ���� ���
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		
		return ResponseEntity.ok().body(response);
		
	}
	
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testResponseEntity(){
		List<String> list = new ArrayList<>();
		list.add("ResponseEntity Return !");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping
	public ResponseEntity<?> createTodo(
			@AuthenticationPrincipal String userId, 
			@RequestBody TodoDTO dto) {
		
		try {
			// 1. TodoEntity�� ��ȯ
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// 2. id�� null�� �ʱ�ȭ�Ѵ�. ���� ��ÿ��� id�� ����� �ϱ� �����̴�.
			entity.setId(null);
			
			// 3. �ӽ� ����� ���̵� ����. 
			entity.setUserId(userId);
			
			// 4. ���񽺸� �̿��� Todo��ƼƼ�� ����
			List<TodoEntity> entities = service.create(entity);
			
			// 5. �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// 6. ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ�Ѵ�.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// 7. ResponseDTO ����
			return ResponseEntity.ok().body(response);
			
		} catch (Exception e) {
			// 8. Ȥ�� ���ܰ� �ִ� ��� dto ��� error�� �޼����� �־� ����
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(
			@AuthenticationPrincipal String userId){		
		//1. ���� �޼����� retrieve() �޼��带 �̿��� Todo����Ʈ�� ������
		List<TodoEntity> entities = service.retrieve(userId);
		
		//2. �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO����Ʈ�� ��ȯ
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//3. ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		//4. ResponseDTO ����
		return ResponseEntity.ok().body(response);
		
	}
	
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,
			@RequestBody TodoDTO dto){
		//1. dto�� entity�� ��ȯ
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		//2. id�� temporaryUserId�� �ʱ�ȭ
		entity.setUserId(userId);
		
		//3. ���񽺸� �̿��� entity�� ������Ʈ
		List<TodoEntity> entities = service.update(entity);
		
		//4. �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//5. ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		//6. ResponseDTO ����
		return ResponseEntity.ok().body(response);
		
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(
			@AuthenticationPrincipal String userId,
			@RequestBody TodoDTO dto){
		try {
			//1. TodoEntity�� ��ȯ
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			//2. �ӽ� ����� ���̵� ����
			entity.setUserId(userId);
			
			//3. ���񽺸� �̿��� entity ����
			List<TodoEntity> entities = service.delete(entity);
			
			//4. �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//5. ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO �ʱ�ȭ
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			//6. ResponseDTO�� ����
			return ResponseEntity.ok().body(response);
			
		} catch (Exception e) {
			//7. Ȥ�� ���ܰ� �ִ� ��� dto ��� error�� �޼����� �־� ����
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
