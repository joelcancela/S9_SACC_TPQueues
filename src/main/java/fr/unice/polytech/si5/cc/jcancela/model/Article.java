package fr.unice.polytech.si5.cc.jcancela.model;

/**
 * Class Article
 *
 * @author Joël CANCELA VAZ
 */
public class Article {

	String nom;
	double prix;
	int qte;
	Magasin magasin;

	public Article() {
	}

	public Article(String nom, double prix, int qte) {
		this.nom = nom;
		this.prix = prix;
		this.qte = qte;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getQte() {
		return qte;
	}

	public void setQte(int qte) {
		this.qte = qte;
	}

	public Magasin getMagasin() {
		return magasin;
	}

	public void setMagasin(Magasin magasin) {
		this.magasin = magasin;
	}
}
