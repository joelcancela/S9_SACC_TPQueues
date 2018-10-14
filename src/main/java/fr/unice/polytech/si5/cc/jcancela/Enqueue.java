package fr.unice.polytech.si5.cc.jcancela;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.Gson;
import fr.unice.polytech.si5.cc.jcancela.model.Article;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Enqueue
 *
 * @author Joël CANCELA VAZ
 */
@WebServlet
public class Enqueue extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Article> articles = new ArrayList<>();
		articles.add(new Article("Poire", 2.0, 1));
		articles.add(new Article("Pomme", 2.0, 1));
		articles.add(new Article("A", 2.0, 1));
		articles.add(new Article("B", 2.0, 1));
		articles.add(new Article("C", 2.0, 1));

		Queue queue = QueueFactory.getQueue("article-queue");
		for (Article article : articles) {
			Gson gson = new Gson();
			String articleToJson = gson.toJson(article);
			queue.add(TaskOptions.Builder.withUrl("/worker").method(TaskOptions.Method.POST).payload(articleToJson));
		}
		response.sendRedirect("/");
	}
}
