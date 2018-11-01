package fr.unice.polytech.si5.cc.jcancela;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.cloud.datastore.*;
import fr.unice.polytech.si5.cc.jcancela.model.Article;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("serial")
@WebServlet
public class DatastoreServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
			ServletException {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("article").build();
		QueryResults<Entity> results = datastore.run(query);
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		while (results.hasNext()) {
			Entity entity = results.next();
			out.format("Article: %s Prix: %s Qte: %s \n", entity.getString("nom"), entity.getDouble("prix"),
					entity.getLong("qte"));//Int => Long
		}
	}

	//Ex to test in Postman: {"nom":"Pomme","prix":7.8,"qte":77}
	//Ex to test in Postman: {"nom":"Poire","prix":7.7,"qte":77}
	//Delete entity with ?delete= <name>
	//Delete: http://localhost:8080/datastore?delete=Pomme
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		if(req.getParameterMap().size()>0 && req.getParameterMap().containsKey("delete")){
			StructuredQuery.PropertyFilter nameFilter = StructuredQuery.PropertyFilter.eq("nom",req.getParameter(
					"delete"));
			Query<Entity> query = Query.newEntityQueryBuilder().setKind("article").setFilter(nameFilter).build();
			QueryResults<Entity> results = datastore.run(query);
			while (results.hasNext()) {
				Entity entity = results.next();
				datastore.delete(entity.getKey());
			}
		}else{
			KeyFactory keyFactory = datastore.newKeyFactory().setKind("article");
			BufferedReader reader = req.getReader();
			Article articleInput = new Gson().fromJson(reader, Article.class);
			IncompleteKey key = keyFactory.setKind("article").newKey();
			FullEntity<IncompleteKey> aNewArticle = FullEntity.newBuilder(key)
					.set("nom", articleInput.getNom()).set("prix", articleInput.getPrix()).set("qte", articleInput.getQte()).build();
			datastore.add(aNewArticle);
		}
	}
}