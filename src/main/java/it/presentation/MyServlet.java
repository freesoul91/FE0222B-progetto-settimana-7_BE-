package it.presentation;

import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import it.business.RubricaEjb;
import it.data.Contatto;
import it.data.NumTelefono;

@WebServlet("/miaservlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	RubricaEjb servRubrica;

	public MyServlet() {

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String operazione = request.getParameter("operazione");
		switch(operazione) {
		
		case "insert": 								
			insertContatto(request, response);
			break;
			
		case "ricercacognome" :			//IL MOSTRA TUTTI I CONTATTI SI TROVA SULLA SERVLET: "MostraTuttiServlet"					
			ricercaContattoByCognome(request, response);
			break;
			
		case "update" :
			updateContattoById(request, response);
			break;
			
		case "ricercanumero" :
			ricercaContattoByNumero(request, response);
			break;
		
		case "deletecontatto" :
			removeContattoById(request, response);
			break;
		
		case "insertduenumeri" :
			inserisciContatto(request, response);
		break;
		
		/*case "aggiornacontatto" : METODO UPDATE DI PROVA (No controlli)
			aggiornaContatto(request, response);
		break;*/
		
		}
	
	}
	
	private void removeContattoById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		if (!request.getParameter("id").isBlank()) {
			Integer idDaEliminare = Integer.parseInt(request.getParameter("id"));
			servRubrica.deleteContattoById(idDaEliminare);
			out.println("<h2>Hai eliminato con successo il contatto ed i relativi numeri dal DB</h2>");	
			out.println("<a href='index.html'>Torna all'index!</a>");
		}
		else {
			out.println("<h2>NON HAI INSERITO L'ID CONTATTO DA ELIMINARE! riprova!</h2>");
			out.println("<a href='index.html'>Torna all'index!</a>");
		}
	
	}
	//ALTRO METODO UPDATE DI PROVA (Senza Controlli!!)
	/*private void aggiornaContatto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Integer idCont = Integer.parseInt(request.getParameter("id"));
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String numero1 = request.getParameter("numero1");
		String numero2 = request.getParameter("numero2");
		servRubrica.aggiornaContattoEsistente(idCont, numero1, numero2, nome, cognome, email);
		out.println("<h1>Contatto aggiornato con successo</h1>");
		out.println("<a href='index.html'>Torna all'index!</a>");
		
	}*/

	
	private void insertContatto(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		Contatto c = new Contatto();

		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");

		c.setNome(nome);
		c.setCognome(cognome);
		c.setEmail(email);

		NumTelefono numero1 = new NumTelefono();
		NumTelefono numero2 = new NumTelefono();

		ArrayList<NumTelefono> numeriTelefono = new ArrayList<NumTelefono>();

		if (!request.getParameter("numero1").isBlank() && servRubrica.lunghezzaNumero(request.getParameter("numero1"))
			&& servRubrica.numeriUguali(request.getParameter("numero1"), request.getParameter("numero2"))
			  && servRubrica.soloNumeri(request.getParameter("numero1"), 10) ) {
			
			numero1.setNumeroTelefono(request.getParameter("numero1"));
			numero1.setContatto(c);
			numeriTelefono.add(numero1);
			
			out.println("<h1>Contatto inserito con successo</h1>");
			out.println("<a href='index.html'>Torna all'index!</a>");

		}

		else if (!request.getParameter("numero2").isBlank() && servRubrica.lunghezzaNumero(request.getParameter("numero2"))
			&& servRubrica.numeriUguali(request.getParameter("numero1"), request.getParameter("numero2")) 
			  && servRubrica.soloNumeri(request.getParameter("numero2"), 10) ) {
			System.out.println();
			numero2.setNumeroTelefono(request.getParameter("numero2"));
			numero2.setContatto(c);
			numeriTelefono.add(numero2);
			
			out.println("<h1>Contatto inserito con successo</h1>");
			out.println("<a href='index.html'>Torna all'index!</a>");
		}

		else if (request.getParameter("numero1").isBlank() && request.getParameter("numero2").isBlank()) {
			HttpSession session = request.getSession();
			session.setAttribute("messaggio", "Attenzione! Inserire almeno un numero di telefono!");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/insertcontatto.jsp");
			dispatcher.forward(request, response);

		}
		
		else if (!servRubrica.numeriUguali(request.getParameter("numero1"), request.getParameter("numero2"))) {
			HttpSession session = request.getSession();
			session.setAttribute("messaggio", "Attenzione! Hai inserito 2 numeri uguali oppure il numero inserito è già associato ad un contatto!");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/insertcontatto.jsp");
			dispatcher.forward(request, response);
			
		}
		c.setNumTelefoni(numeriTelefono);
		servRubrica.insertContatto(c);
	}
	
	private void inserisciContatto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String numero1 = request.getParameter("numero1");
		String numero2 = request.getParameter("numero2");
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		
		if (numero1.isBlank() || 
				numero2.isBlank() || 
				!servRubrica.lunghezzaNumero(numero1) || 
				!servRubrica.lunghezzaNumero(numero2) || 
				!servRubrica.soloNumeri(numero1, 10) ||
				!servRubrica.soloNumeri(numero2, 10)) {
			System.out.println("n1 is blank: " + numero1.isBlank() + " /n2isblank: " + numero2.isBlank() + 
					" /lungNum1: " + servRubrica.lunghezzaNumero(numero1) + " /lungNum2: " + servRubrica.lunghezzaNumero(numero2) + " /"
							+ "solonum1: "+ servRubrica.soloNumeri(numero1, 10) + " /soloNum2: " + servRubrica.soloNumeri(numero2, 10) );
			out.println("<h2>Errore inserimento contatto con due numeri!</h2>");
			out.println("<a href='index.html'>Torna all'index!</a>");
			return;
			
		}
		Contatto c = new Contatto();

		c.setNome(nome);
		c.setCognome(cognome);
		c.setEmail(email);

		NumTelefono num1 = new NumTelefono();
		NumTelefono num2 = new NumTelefono();

		ArrayList<NumTelefono> numeriTelefono = new ArrayList<NumTelefono>();
		num1.setNumeroTelefono(numero1);
		num2.setNumeroTelefono(numero2);
		num1.setContatto(c);
		num2.setContatto(c);
		
		numeriTelefono.add(num1);
		numeriTelefono.add(num2);
		c.setNumTelefoni(numeriTelefono);
		servRubrica.insertContatto(c);
		
		out.println("<h1>Contatto inserito con successo</h1>");
		out.println("<a href='index.html'>Torna all'index!</a>");
	}
	
	private void ricercaContattoByCognome(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String url = "/contattocognome.jsp";
		PrintWriter out = response.getWriter();
		if (!request.getParameter("cognome").isBlank()) {
			String cognome = request.getParameter("cognome");
			List<Object[]> contatto = servRubrica.getContattoByCognome(cognome);
			request.setAttribute("contatto", contatto);
			request.getServletContext().getRequestDispatcher(url).forward(request, response);
		}
		else {
			out.println("<h2>NON HAI INSERITO IL COGNOME! riprova!</h2>");
			out.println("<a href='index.html'>Torna all'index!</a>");
		}
			
	}
	
	private void ricercaContattoByNumero(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String url = "/contattonumero.jsp";
		PrintWriter out = response.getWriter();
		if (!request.getParameter("numero").isBlank()) {
			String numero = request.getParameter("numero");
			List<Object[]> contatto = servRubrica.getContattoByNumero(numero);
			request.setAttribute("contatto", contatto);
			request.getServletContext().getRequestDispatcher(url).forward(request, response);
		}
		else {
			out.println("<h2>NON HAI INSERITO UN NUMERO DI TELEFONO! riprova!</h2>");
			out.println("<a href='index.html'>Torna all'index!</a>");
		}
			
	}
	
	private void updateContattoById(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		if ( !request.getParameter("numero1").isBlank() && servRubrica.lunghezzaNumero(request.getParameter("numero1") ) && servRubrica.soloNumeri(request.getParameter("numero1"), 10)) {
			servRubrica.updateNumeroContattoUnNum(
					Integer.parseInt(request.getParameter("id")), 
					request.getParameter("numero1"), 
					request.getParameter("nome"),
					request.getParameter("cognome"), 
					request.getParameter("email"));
			out.println("<h3>Hai aggiornato con successo il contatto, vedi lista completa per vederlo</h>");
			out.println("<br><br><a href='index.html'>Torna all'index!</a>");
		}
		
		else if (!request.getParameter("numero1").isBlank() && !request.getParameter("numero2").isBlank() && servRubrica.lunghezzaNumero(request.getParameter("numero1"))
				 && servRubrica.lunghezzaNumero(request.getParameter("numero2")) && servRubrica.soloNumeri(request.getParameter("numero1"), 10) && servRubrica.soloNumeri(request.getParameter("numero2"), 10)) {
			servRubrica.updateNumeroContatto(
					Integer.parseInt(request.getParameter("id")), 
					request.getParameter("numero1"), 
					request.getParameter("numero2"), 
					request.getParameter("nome"),
					request.getParameter("cognome"), 
					request.getParameter("email"));
				out.println("<h3>Hai aggiornato con successo il contatto, vedi lista completa per vederlo</h>");
				out.println("<br><br><a href='index.html'>Torna all'index!</a>");
			
		}
	
		else {
			out.println("<h2>NON HAI INSERITO DEI DATI! OPPURE I NUMERI INSERITI NON RISPETTANO I CONTROLLI (solo numeri, lunghezza max = 10) riprova!</h2>");
			out.println("<a href='index.html'>Torna all'index!</a>");
		}
	
	}

}
