package it.data;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name="numero")
@Entity
public class NumTelefono implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String numeroTelefono;
	
	private Contatto contatto;
	
	public NumTelefono() {}
	
	

	@Id
	@Column(name="numero_telefono")
	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	@ManyToOne
	@JoinColumn(name="id_contatto")
	public Contatto getContatto() {
		return contatto;
	}

	public void setContatto(Contatto contatto) {
		this.contatto = contatto;
	} 
	
	
}
