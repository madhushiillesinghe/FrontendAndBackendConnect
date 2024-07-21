package lk.ijse.gdse.aad68.studentmanagement.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.aad68.studentmanagement.dto.StudentDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/demo")
public class demo extends HttpServlet {
    @Override
    public void init() throws ServletException {
        var initParameter=getServletContext().getInitParameter("myparam");
        System.out.println(initParameter);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
         List <StudentDTO> studentDTOList = jsonb.fromJson(req.getReader(), new ArrayList<StudentDTO>() {

        }.getClass().getGenericSuperclass());
          studentDTOList.forEach(System.out::println);
    }
}
