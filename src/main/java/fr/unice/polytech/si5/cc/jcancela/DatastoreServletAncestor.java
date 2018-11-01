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
import java.util.Map;

/**
 * Class DatastoreServletAncestor
 *
 * @author Joël CANCELA VAZ
 * <p>
 * 1.
 */
@WebServlet
public class DatastoreServletAncestor extends HttpServlet {

	//?magasin=Carrefour&prix=10
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
			ServletException {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Map parameters = req.getParameterMap();
		if (parameters.size() == 2 && parameters.containsKey("magasin") && parameters.containsKey("prix")) {
			System.out.println(((String[]) parameters.get("magasin"))[0]);
			Key magasinKey = datastore.newKeyFactory().setKind("magasin").newKey(((String[]) parameters.get("magasin"))[0]);
			System.out.println("DEBUG: " + magasinKey);
			Query<Entity> query =
					Query.newEntityQueryBuilder().setKind("articleNew").setFilter(StructuredQuery.PropertyFilter.hasAncestor(magasinKey)).build();
			QueryResults<Entity> results = datastore.run(query);
			resp.setContentType("text/plain");
			PrintWriter out = resp.getWriter();
			while (results.hasNext()) {
				Entity entity = results.next();
				out.format("Article: %s Prix: %s Qte: %s Magasin: %s \n", entity.getString("nom"), entity.getDouble("prix"),
						entity.getLong("qte"), entity.getEntity("magasin"));//Int => Long
			}
		} else {
			Query<Entity> query = Query.newEntityQueryBuilder().setKind("articleNew").build();
			QueryResults<Entity> results = datastore.run(query);
			resp.setContentType("text/plain");
			PrintWriter out = resp.getWriter();
			while (results.hasNext()) {
				Entity entity = results.next();
				out.format("Article: %s Prix: %s Qte: %s Magasin: %s \n", entity.getString("nom"), entity.getDouble("prix"),
						entity.getLong("qte"), entity.getEntity("magasin"));//Int => Long
			}
		}


		/*
		Query<Entity> queryMagasin = Query.newEntityQueryBuilder().setKind("magasin").build();
		results = datastore.run(queryMagasin);
		resp.setContentType("text/plain");
		while (results.hasNext()) {
			Entity entity = results.next();
			out.format("Magasin %s \n", entity.getString("name"));
		}
		*/
	}

	//{"nom":"Poire","prix":7.7,"qte":77,"magasin":{"name":"Carrefour"}}
	//{"nom":"POMME","prix":7.7,"qte":77,"magasin":{"name":"Casino"}}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		BufferedReader reader = req.getReader();
		Article articleInput = new Gson().fromJson(reader, Article.class);

		Key magasinKey = datastore.newKeyFactory().setKind("magasin").newKey(articleInput.getMagasin().getName());
		System.out.println("DEBUG2: " + magasinKey);
		Entity magasin = datastore.get(magasinKey);
		if (magasin == null) {
			FullEntity<Key> aMagasin = FullEntity.newBuilder(magasinKey)
					.set("name", articleInput.getMagasin().getName()).build();
			datastore.add(aMagasin);
		}
		magasin = datastore.get(magasinKey);
		KeyFactory keyFactory = datastore.newKeyFactory()
				.addAncestors(PathElement.of("magasin", articleInput.getMagasin().getName()))
				.setKind("articleNew");
		IncompleteKey articleKey = keyFactory.newKey();

		FullEntity<IncompleteKey> aNewArticle = FullEntity.newBuilder(articleKey)
				.set("nom", articleInput.getNom()).set("prix", articleInput.getPrix()).set("qte",
						articleInput.getQte()).set("magasin", magasin).build();
		//Une limitation de la notion d'ancêtre est qu'elle est permanente. Proposez un moyen de faire passer un article d'un magasin à un autre.
		//Set entity ???
		datastore.add(aNewArticle);
	}

	//?magasin=Casino
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Map parameters = req.getParameterMap();
		Key magasinKey = datastore.newKeyFactory().setKind("magasin").newKey(((String[]) parameters.get("magasin"))[0]);
		Query<Entity> query =
				Query.newEntityQueryBuilder().setKind("articleNew").setFilter(StructuredQuery.PropertyFilter.hasAncestor(magasinKey)).build();
		QueryResults<Entity> results = datastore.run(query);
		Transaction txn = datastore.newTransaction();
		try {
			while (results.hasNext()) {
				Entity entity = results.next();
				Entity updated =
						Entity.newBuilder(entity).set("prix", entity.getDouble("prix") * 1.1).build();
				txn.put(updated);
			}
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

	}
}
