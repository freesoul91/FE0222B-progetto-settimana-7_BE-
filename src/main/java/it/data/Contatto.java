package it.data;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Table(name="contatto")
@Entity
public class Contatto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String nome;
	private String cognome;
	private String email;
	private ArrayList<NumTelefono> numTelefoni;
	
	public Contatto() {}
	
		
	public Contatto(String nome, String cognome, String email) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
	}

	
	@Id
	@Column(name="id_contatto")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	@Column(name="nome")
	public String getNome() {
		return nome;
	}
	@Column(name="cognome")
	public String getCognome() {
		return cognome;
	}
	@Column(name="email")
	public String getEmail() {
		return email;
	}
	@OneToMany(mappedBy = "contatto", cascade = CascadeType.ALL)
	@JoinColumn(name="numero_telefono")
	public ArrayList<NumTelefono> getNumTelefoni() {
		if (numTelefoni == null)
			numTelefoni = new ArrayList<NumTelefono>();
		return numTelefoni;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setNumTelefoni(ArrayList<NumTelefono> numTelefoni) {
		this.numTelefoni = numTelefoni;
	}
	
	

}
