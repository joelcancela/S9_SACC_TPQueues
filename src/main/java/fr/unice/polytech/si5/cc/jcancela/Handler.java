package fr.unice.polytech.si5.cc.jcancela;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

//Source https://cloud.google.com/appengine/docs/standard/java/taskqueue/push/example?authuser=1&hl=zh-tw
public class Handler extends HttpServlet {
	private static final Logger log = Logger.getLogger(Handler.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = req.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		resp.setContentType("application/json");
		log.info("Treated new request, payload:"+data);
		resp.getWriter().println(data);
	}
}
