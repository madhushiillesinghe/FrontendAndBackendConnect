package lk.ijse.gdse.aad68.studentmanagement.controller;


import lk.ijse.gdse.aad68.studentmanagement.dto.StudentDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@WebServlet(urlPatterns = "/student", loadOnStartup = 2)
public class Student extends HttpServlet {
    Connection connection;

    public  static String SAVE_STUDENT="INSERT INTO student (id,name,email,city,level) VALUES (?, ?, ?, ?, ?)";
    public static String GET_STUDENT = "SELECT * FROM student WHERE id = ?";
    public static  String UPDATE_STUDENT="UPDATE student SET name= ?,email=?,city=?,level=? WHERE id=?";
    public static String DELETE_STUDENT = "DELETE FROM student WHERE id = ?";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try(var writer=resp.getWriter()){
            StudentDTO studentDTO=new StudentDTO();
            Jsonb jsonb=JsonbBuilder.create();
            var studentId=req.getParameter("studentId");
            var ps=connection.prepareStatement(GET_STUDENT);
            ps.setString(1,studentId);
            var rst=ps.executeQuery();
            while (rst.next()){
                studentDTO.setID(rst.getString("id"));
                studentDTO.setName(rst.getString("name"));
                studentDTO.setEmail(rst.getString("email"));
                studentDTO.setCity(rst.getString("city"));
                studentDTO.setLevel(rst.getString("level"));
            }

            resp.setContentType("application/json");
            jsonb.toJson(studentDTO,writer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //save student
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
//        BufferedReader reader = req.getReader();
//        StringBuilder sb = new StringBuilder();
//        reader.lines().forEach(line -> sb.append(line).append("\n"));
//        System.out.println(sb);



//        var reader = Json.createReader(req.getReader());
////        JsonObject jsonObject = reader.readObject();
////        String name = jsonObject.getString("name");
////        System.out.println(name);
////
////        //send data to client
////        var writer = resp.getWriter();
////        writer.write(name);
//
//        //optional - JSON array processing
//        JsonArray jsonArray = reader.readArray();
//        for (int i = 0; i < jsonArray.size(); i++){
//            var jsonObject1 = jsonArray.getJsonObject(i);
//            System.out.println(jsonObject1.getString("name"));
//            System.out.println(jsonObject1.getString("email"));
//        }

        try (var writer = resp.getWriter()){
            //object binding of the JSON
            Jsonb jsonb = JsonbBuilder.create();
            StudentDTO studentDTO = jsonb.fromJson(req.getReader(), StudentDTO.class);
            studentDTO.setID(lk.ijse.gdse.aad68.studentmanagement.Util.util.idGenerate());

            //save data in DB
            var ps = connection.prepareStatement(SAVE_STUDENT);
            ps.setString(1,studentDTO.getID());
            ps.setString(2,studentDTO.getName());
            ps.setString(3,studentDTO.getEmail());
            ps.setString(4,studentDTO.getCity());
            ps.setString(5,studentDTO.getLevel());

            if(ps.executeUpdate() != 0 ){
                resp.setStatus(HttpServletResponse.SC_CREATED);
                writer.write("save student successfully");
            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to Save student");
            }

            //create response
            resp.setContentType("application/json");
            jsonb.toJson(studentDTO,writer);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo: Delete Student
        try(var writer=resp.getWriter()){
            var studentId=req.getParameter("studentId");
            var ps=connection.prepareStatement(DELETE_STUDENT);
            ps.setString(1,studentId);

            if(ps.executeUpdate() != 0){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                writer.write("STUDENT DELETE SUCCESS");
            }else{
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("STUDENT DELETE FAIL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo: Update Student
        try(var write=resp.getWriter()){
            var studentId=req.getParameter("studentId");
            Jsonb jsonb=JsonbBuilder.create();
            StudentDTO studentDTO=jsonb.fromJson(req.getReader(), StudentDTO.class);
            var ps=connection.prepareStatement(UPDATE_STUDENT);

            ps.setString(1,studentDTO.getName());
            ps.setString(2,studentDTO.getEmail());
            ps.setString(3,studentDTO.getCity());
            ps.setString(4,studentDTO.getLevel());
            ps.setString(5,studentId);

            if(ps.executeUpdate()!= 0){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                write.write("Student already updated");
            }else{
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                write.write("Fail to update");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void init() throws ServletException {
         var dbClass = getServletContext().getInitParameter("db-class");
         var dbUrl = getServletContext().getInitParameter("dburl");
         var dbUsername = getServletContext().getInitParameter("db-username");
         var dbPassword = getServletContext().getInitParameter("db-password");
        try {
            Class.forName(dbClass);
            this.connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}