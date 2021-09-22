package net.ausiasmarch.sermiller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Control extends HttpServlet {

    private void opDelay(Integer iLast) {
        try {
            Thread.sleep(iLast);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET,POST, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, x-requested-with, Content-Type");
        try ( PrintWriter out = response.getWriter()) {
            Gson oGson = new Gson();
            try {
                Integer op1 = Integer.parseInt(request.getParameter("op1"));
                Integer op2 = Integer.parseInt(request.getParameter("op2"));
                Integer max = 50;
                if (op1 == null || op1 > max || op1 < 1 || op2 == null || op2 > max || op2 < 1) {
                    out.print(oGson.toJson("Error en los operandos: deben ser enteros entre 1 y " + max + "."));
                } else {
                    String strMaxDelay = request.getParameter("maxdelay");
                    if (!"".equalsIgnoreCase(strMaxDelay) && strMaxDelay != null) {
                        Integer maxDelay = Integer.parseInt(strMaxDelay);
                        if (maxDelay >= 1 || maxDelay <= 10000) {
                            opDelay(new Random().nextInt(maxDelay) + 1);
                        }
                    }
                    out.print(oGson.toJson(new MultiplicationBean(op1, op2, op1 * op2)));
                }
            } catch (Exception ex) {
                out.print(oGson.toJson("Error: " + ex.toString()));
                out.print(ex);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
