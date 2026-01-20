package com.example.Webapp.Utils;

import com.example.Webapp.Model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtils {

    public static void write(Response rm, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json;charset=utf-8");

        PrintWriter out = resp.getWriter();
        out.write(new ObjectMapper().writeValueAsString(rm));
        out.flush();
        out.close();

    }
}
