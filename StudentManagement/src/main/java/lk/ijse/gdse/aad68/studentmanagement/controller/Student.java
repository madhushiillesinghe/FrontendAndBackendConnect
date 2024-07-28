package lk.ijse.gdse.aad68.studentmanagement.controller;


import lk.ijse.gdse.aad68.studentmanagement.Util.util;
import lk.ijse.gdse.aad68.studentmanagement.bo.StudentBOIMPL;
import lk.ijse.gdse.aad68.studentmanagement.dao.StudentDaoIMPL;
import lk.ijse.gdse.aad68.studentmanagement.dto.StudentDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@WebServlet(urlPatterns = "/student", loadOnStartup = 2)
public class Student extends HttpServlet {
    static Logger logger= LoggerFactory.getLogger(Student.class);
    Connection connection;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(var writer = resp.getWriter()) {
            var studentBOIMPL = new StudentBOIMPL();            Jsonb jsonb = JsonbBuilder.create();
            //DB Process
            var studentId = req.getParameter("studentId");;
            resp.setContentType("application/json");
            jsonb.toJson(studentBOIMPL.getStudent(studentId,connection),writer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //save student
        if(req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        try (var writer = resp.getWriter()){
            Jsonb jsonb = JsonbBuilder.create();
            var studentBOIMPL = new StudentBOIMPL();            StudentDTO student = jsonb.fromJson(req.getReader(), StudentDTO.class);
            student.setID(util.idGenerate());
            //Save data in the DB
            writer.write(studentBOIMPL.saveStudent(student,connection));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo: Delete Student
        try (var writer = resp.getWriter()) {
            var studentId = req.getParameter("studentId");
            var studentBOIMPL = new StudentBOIMPL();
            if(studentBOIMPL.deleteStudent(studentId,connection)){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else {
                writer.write("Delete failed");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo: Update Student
        try (var writer = resp.getWriter()) {
            var studentBOIMPL = new StudentBOIMPL();            var studentId = req.getParameter("studentId");
            Jsonb jsonb = JsonbBuilder.create();
            StudentDTO student = jsonb.fromJson(req.getReader(), StudentDTO.class);
            if(studentBOIMPL.updateStudent(studentId,student,connection)){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else {
                writer.write("Update failed");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void init() throws ServletException {
        logger.info("Init Method invoked");

try{
    var ctx = new InitialContext(); // connection pool eken connection ekk arahnn ekt
    DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/studentManagement"); // cast krl ek datasource ekk hdgnnwa // lookup => apit eken puluwn eke nm dila mokkhri gnna apit ona de lokup krl gann ek cast krgnn jenaric nida
    this.connection = pool.getConnection(); // DataSource (connection pool ekkt)=> walin getconnection ekk
    logger.info("Connection Inizialize",this.connection);
        } catch ( SQLException | NamingException e) {
            e.printStackTrace();
        }

    }
}