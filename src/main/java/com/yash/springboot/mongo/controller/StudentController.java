package com.yash.springboot.mongo.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yash.springboot.mongo.exception.ResourceNotFoundException;
import  com.yash.springboot.mongo.model.Student;
import  com.yash.springboot.mongo.repository.StudentRepository;
import  com.yash.springboot.mongo.service.SequenceGeneratorService;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class StudentController {
	@Autowired
	private StudentRepository StudentRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@GetMapping("/students")
	public List<Student> getAllStudents() {
		return StudentRepository.findAll();
	}

	@GetMapping("/student/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") Long studentId)
			throws ResourceNotFoundException {
		Student student = StudentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found for this id :: " + studentId));
		return ResponseEntity.ok().body(student);
	}

	@PostMapping("/students")
	public Student createStudent(@Valid @RequestBody Student student) {
		student.setId(sequenceGeneratorService.generateSequence(Student.SEQUENCE_NAME));
		return StudentRepository.save(student);
	}

	@PutMapping("/student/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") Long studentId,
			@Valid @RequestBody Student studentDetails) throws ResourceNotFoundException {
		Student student = StudentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found for this id :: " + studentId));

		student.setCourse(studentDetails.getCourse());
		student.setLastName(studentDetails.getLastName());
		student.setFirstName(studentDetails.getFirstName());
		final Student updatedStudent = StudentRepository.save(student);
		return ResponseEntity.ok(updatedStudent);
	}

	@DeleteMapping("/student/{id}")
	public Map<String, Boolean> deleteStudent(@PathVariable(value = "id") Long studentId)
			throws ResourceNotFoundException {
		Student student = StudentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found for this id :: " + studentId));

		StudentRepository.delete(student);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}