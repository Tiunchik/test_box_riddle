/**
 * Package server.box for
 *
 * @author Maksim Tiunchik
 */
package box.sever;

import box.local.DBStore;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Class QuestionServlet -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 31.03.2020
 */
@WebServlet(urlPatterns = "/request")
public class QuestionServlet extends HttpServlet {

    private static final DBStore BASE = DBStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/request.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (BufferedReader read = req.getReader(); PrintWriter out = resp.getWriter()) {
            StringBuilder fullLine = new StringBuilder();
            String oneLine;
            while ((oneLine = read.readLine()) != null) {
                fullLine.append(oneLine);
            }
            JSONObject json = (JSONObject) new JSONParser().parse(fullLine.toString());
            String id = (String) json.get("id");
            String color = (String) json.get("color");
            if (id != null && color != null) {
                List<Integer> answer = BASE.search(Integer.parseInt(id), color);
                JSONArray array = new JSONArray();
                answer.forEach(array::add);
                out.write(array.toJSONString());
                out.flush();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}