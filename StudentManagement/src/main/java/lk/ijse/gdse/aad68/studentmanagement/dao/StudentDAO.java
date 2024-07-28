package lk.ijse.gdse.aad68.studentmanagement.dao;

import lk.ijse.gdse.aad68.studentmanagement.controller.Student;
import lk.ijse.gdse.aad68.studentmanagement.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface StudentDAO{
    String saveStudent(StudentDTO student, Connection connection) throws SQLException;
    boolean updateStudent(String id,StudentDTO student,Connection connection) throws SQLException;
    boolean deleteStudent(String id,Connection connection) throws SQLException;
    StudentDTO getStudent(String id,Connection connection) throws SQLException;
}
