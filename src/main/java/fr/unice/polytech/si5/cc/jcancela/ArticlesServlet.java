package fr.unice.polytech.si5.cc.jcancela;

import com.google.gson.Gson;
import fr.unice.polytech.si5.cc.jcancela.model.Article;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class ArticlesServlet
 *
 * @author Joël CANCELA VAZ
 */
@WebServlet
public class ArticlesServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		List<Article> articles = new ArrayList<>();
		articles.add(new Article("Poire",2.0,1));
		articles.add(new Article("Pomme",2.7,7));
		resp.getWriter().println(new Gson().toJson(articles));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = req.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		resp.setContentType("application/json");
		resp.getWriter().println(data);
	}
}
