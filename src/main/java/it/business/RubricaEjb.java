package it.business;

import java.util.List;

import it.data.Contatto;
import it.data.NumTelefono;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Stateless
@LocalBean
public class RubricaEjb {

	@PersistenceContext(unitName="corsoEpicodePersistence")
	EntityManager em;
   
    public RubricaEjb() {
       
    }

    public List<Object[]> getAllContattiENumeri() {
    	Query q1 = em.createQuery("SELECT c.id, c.nome, c.cognome, c.email, n.numeroTelefono FROM Contatto c JOIN NumTelefono n ON "
    			+ "c.contatto = n.contatto "
    			+ "WHERE NOT n.numeroTelefono = ''");
    	List<Object[]> contatti = q1.getResultList();
    	return contatti;
    }
    
    public List<Object[]> getContattoByCognome(String cognomePartial) {   	
    	Query sql = em.createNativeQuery("SELECT contatto.id_contatto, contatto.nome, contatto.cognome, \r\n"
    			+ "contatto.email, numero.numero_telefono from contatto JOIN \r\n"
    			+ "numero on contatto.id_contatto=numero.id_contatto WHERE cognome ILIKE '%" + cognomePartial + "%'");   	
    	List<Object[]> contatto = sql.getResultList();
    	return contatto;
    }
    
    public List<Object[]> getContattoByNumero(String numeroPartial) {
    	Query sql = em.createNativeQuery("SELECT contatto.id_contatto, contatto.nome, contatto.cognome, \r\n"
    			+ "contatto.email, numero.numero_telefono from contatto JOIN \r\n"
    			+ "numero on contatto.id_contatto=numero.id_contatto WHERE numero_telefono ILIKE '%" + numeroPartial + "%'");   	
    	List<Object[]> contatto = sql.getResultList();
    	return contatto;
    }
    
    public Contatto insertContatto(Contatto c) {
    	em.persist(c);
    	return c;
    }
    
    public Contatto insertContatto(String nome, String cognome, String email) {
    	Contatto c = new Contatto(nome, cognome, email);
    	return insertContatto(c);
    }
    
    public Contatto getContattoById(int id) {
    	return em.find(Contatto.class, id);
    }
    
    public Contatto deleteContattoById(Contatto c) {
    	em.remove(getContattoById(c.getId()));
    	return c;
    }
    
    public void updateNumeroContatto(int idContatto, String numero1, String numero2, String nome, String cognome, String email) {
    	Contatto c = em.find(Contatto.class, idContatto);
    	c.setCognome(cognome); c.setNome(nome); c.setEmail(email);
    	List<NumTelefono> numeri = c.getNumTelefoni();
    	boolean primoTrovato = false;
    	boolean secondoTrovato = false;
    	
    	for ( NumTelefono n : numeri ) {
    		if (n.getNumeroTelefono().equals(numero1) ) {
    			primoTrovato = true;
    		}
    		if (n.getNumeroTelefono().equals(numero2)) {
    			secondoTrovato = true;
    		}
    	
    	}
    	if (!primoTrovato) {
    		NumTelefono n1 = new NumTelefono();
    		n1.setNumeroTelefono(numero1);
    		c.getNumTelefoni().add(n1);
    		n1.setContatto(c);
    	}
    	else if (!secondoTrovato) {
    		NumTelefono n2 = new NumTelefono();
    		n2.setNumeroTelefono(numero2);
    		c.getNumTelefoni().add(n2);
    		n2.setContatto(c);
    	}
    
    }
    
    public void updateNumeroContattoUnNum(int idContatto, String numero1,  String nome, String cognome, String email) {
    	Contatto c = em.find(Contatto.class, idContatto);
    	c.setNome(nome); c.setCognome(cognome); c.setEmail(email);
    	List<NumTelefono> numeri = c.getNumTelefoni();
    	boolean primoTrovato = false;
    	for ( NumTelefono n : numeri ) {
    		if (n.getNumeroTelefono().equals(numero1) ) {
    			primoTrovato = true;
    		}
    	
    	}
    	if (!primoTrovato) {
    		NumTelefono n1 = new NumTelefono();
    		n1.setNumeroTelefono(numero1);
    		c.getNumTelefoni().add(n1);
    		n1.setContatto(c);
    	}
    
    }
    
    public boolean lunghezzaNumero(String numero) {
    	if (numero.length() == 10) {
    		return true;
    	}
    		return false;
    }

    public boolean inserimentoNumeroObbligatorio(String numero1, String numero2) {
    	if( !numero1.isBlank() || !numero2.isBlank()) {
    		return true;
    	}
    	return false;
    }
    
    public boolean numeriUguali(String numero1, String numero2) {
    	if( !numero1.equals(numero2) ) {
    		return true;
    	}
    	return false;
    }
    
    public boolean numeroGiaEsistente(String numero) {
    	Query q = em.createNativeQuery("SELECT numero_telefono FROM numero JOIN contatto ON numero.id_contatto = contatto.id_contatto");
    	Object numeroC = (String)q.getSingleResult();	
    	if ( !numero.equals(numeroC) ) {
    		return true;
    	}
    	else return false;
    	
    }
    // Aggiorna contatto "semplificato"
    public void aggiornaContattoEsistente( int idContatto, String numero1, String numero2, String nome, String cognome, String email ) {
    	Contatto c = em.find(Contatto.class, idContatto);
    	if (c == null) {
    		c = new Contatto();
    		c.setId(idContatto);
    	}
    	c.setNome(nome); c.setCognome(cognome); c.setEmail(email);
    	if ( !numero1.isBlank() ) {
    		NumTelefono n1 = new NumTelefono();
    		n1.setContatto(c);
    		n1.setNumeroTelefono(numero1);
    		c.getNumTelefoni().add(n1);
    	}
    	if ( !numero2.isBlank() ) {
    		NumTelefono n2 = new NumTelefono();
    		n2.setContatto(c);
    		n2.setNumeroTelefono(numero2);
    		c.getNumTelefoni().add(n2);
    	}
    	em.merge(c);
    }
    
    public void deleteContattoById(int idContatto) {
    	em.remove(em.find(Contatto.class, idContatto));
    }
    
    public boolean soloNumeri(String numero, int lunghezzaNumero) {  //n = lunghezza stringa
    	lunghezzaNumero = 10;
    	for (int i = 0; i < lunghezzaNumero; i++) {
  
            if (!Character.isDigit(numero.charAt(i))) {
            	return false;
            }
           
        }
        return true;
    }

}
