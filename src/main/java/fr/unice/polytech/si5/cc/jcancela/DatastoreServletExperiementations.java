package fr.unice.polytech.si5.cc.jcancela;

import com.google.cloud.datastore.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class DatastoreServletExperiementations
 *
 * @author Joël CANCELA VAZ
 */
@WebServlet
public class DatastoreServletExperiementations extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

		Query<Entity> query =
				Query.newEntityQueryBuilder().setKind("magasin").build();
		QueryResults<Entity> results = datastore.run(query);
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		while (results.hasNext()) {
			Entity entity = results.next();
			out.println(entity.getString("name"));
			out.format("Magasin: %s \n", entity.getString("name"));
		}
		out.println("DEBUG\n");

		Transaction txn = datastore.newTransaction();
		results = datastore.run(query);
		try {
			while (results.hasNext()) {
				Entity entity = results.next();
				Entity updated =
						Entity.newBuilder(entity).set("name", "Cherpa").build();
				txn.put(updated);
			}
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		out.println("DEBUG2\n");


		results = datastore.run(query);
		while (results.hasNext()) {
			Entity entity = results.next();
			out.format("Magasin: %s \n", entity.getString("name"));
		}

		out.println("DEBUG3\n");
		Key magasinKey = datastore.newKeyFactory().setKind("magasin").newKey("Carrefour");

		query =
				Query.newEntityQueryBuilder().setKind("articleNew").setFilter(StructuredQuery.PropertyFilter.hasAncestor(magasinKey)).build();
		results = datastore.run(query);
		resp.setContentType("text/plain");
		out = resp.getWriter();
		while (results.hasNext()) {
			Entity entity = results.next();
			out.format("Article: %s Prix: %s Qte: %s Magasin: %s \n", entity.getString("nom"), entity.getDouble("prix"),
					entity.getLong("qte"), entity.getEntity("magasin"));//Int => Long
		}
	}
}
