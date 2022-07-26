package it.presentation;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import it.business.RubricaEjb;

@WebServlet("/mostratutti")
public class MostraTuttiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	@EJB
	RubricaEjb servRubrica;
	
   
    public MostraTuttiServlet() {
       
    }

	// CHE CLASSE QUESTA CLASSE!!! Servlet che utilizza il patter "FACCOTORY" model per decidere quale istanza utilizzare!
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/mostratutti.jsp";
		
		
		List<Object[]> lista = servRubrica.getAllContattiENumeri();
		
		
		request.setAttribute("lista", lista);
		
		request.getServletContext().getRequestDispatcher(url).forward(request, response);
		
	}

}
