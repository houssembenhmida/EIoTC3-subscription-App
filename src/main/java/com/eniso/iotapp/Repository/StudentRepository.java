package com.eniso.iotapp.Repository;

import com.eniso.iotapp.Entity.Student;
import org.springframework.data.repository.CrudRepository;


public interface StudentRepository extends CrudRepository<Student,Integer> {
}
