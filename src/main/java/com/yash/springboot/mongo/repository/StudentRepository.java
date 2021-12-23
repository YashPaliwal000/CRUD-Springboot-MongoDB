package com.yash.springboot.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.yash.springboot.mongo.model.Student;

@Repository
public interface StudentRepository extends MongoRepository<Student,Long> {

}
