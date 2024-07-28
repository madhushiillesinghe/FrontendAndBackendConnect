package lk.ijse.gdse.aad68.studentmanagement.bo;

import lk.ijse.gdse.aad68.studentmanagement.controller.Student;
import lk.ijse.gdse.aad68.studentmanagement.dao.StudentDaoIMPL;
import lk.ijse.gdse.aad68.studentmanagement.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class StudentBOIMPL implements StudentBO{
    @Override
    public String saveStudent(StudentDTO student, Connection connection) throws SQLException {
        var studentDaoIMPL = new StudentDaoIMPL();
        return studentDaoIMPL.saveStudent(student, connection);
    }

    @Override
    public boolean updateStudent(String id, StudentDTO student, Connection connection) throws SQLException {
        var studentDAOIMPL = new StudentDaoIMPL();
        return studentDAOIMPL.updateStudent(id, student, connection);
    }

    @Override
    public boolean deleteStudent(String id, Connection connection) throws SQLException {
        var studentDAOIMPL = new StudentDaoIMPL();
        return studentDAOIMPL.deleteStudent(id, connection);
    }

    @Override
    public StudentDTO getStudent(String id, Connection connection) throws SQLException {
        var studentDAOIMPL = new StudentDaoIMPL();
        return studentDAOIMPL.getStudent(id, connection);
    }
}
