package finix;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.stream.Collectors;

@WebServlet("/")
public class KeyValueService extends HttpServlet {

    private Connection createConnection() {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://ec2-54-215-162-35.us-west-1.compute.amazonaws.com:5432/finix",
                            "other_user", "guest");
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return c;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String id = getIdFromRequest(req);
        try (Connection c = createConnection()) {
            Statement stmt = c.createStatement();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM values where id ='");
            query.append(id);
            query.append("';");
            writer.println(query.toString());
            ResultSet rs = stmt.executeQuery(query.toString());
            if (rs.next()) {
                String value = rs.getString("value");
                writer.println("ID = " + id + " value = " + value);
            } else {
                writer.println("This record doesn't exist");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }

    private String getIdFromRequest(HttpServletRequest req) {
        int lastSlashIndex = req.getRequestURL().reverse().indexOf("/");
        return req.getRequestURL().substring(req.getRequestURL().length() - lastSlashIndex);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String id = getIdFromRequest(req);
        String value = req.getReader().lines().collect(Collectors.joining());
        Statement stmt = null;
        try (Connection c = createConnection(); PrintWriter writer = resp.getWriter()) {
            stmt = c.createStatement();
            stmt.executeUpdate("INSERT INTO values (ID,VALUE) VALUES ('" + id + "','" + value + "');");
            stmt.close();
            writer.println("Wrote id " + id + " value " + value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = getIdFromRequest(req);
        String value = req.getReader().lines().collect(Collectors.joining());
        try (Connection c = createConnection(); PrintWriter writer = resp.getWriter()) {
            writer.println("<html>Put!</html>");
            Statement stmt = c.createStatement();
            String sql = "UPDATE values set value = '" + value + "' where ID='" + id + "';";
            stmt.executeUpdate(sql);
            stmt.close();
            writer.println("<html>Put Success!</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String id = getIdFromRequest(req);
        try (Connection c = createConnection();PrintWriter writer = resp.getWriter()) {
            writer.println("<html>Delete!</html>");
            Statement stmt = c.createStatement();
            String sql = "DELETE from values where ID = '" + id + "';";
            stmt.executeUpdate(sql);
            writer.println("<html>Delete Successful!</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
