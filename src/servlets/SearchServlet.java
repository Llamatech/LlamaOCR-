package servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.llama.tech.utils.list.Lista;

import backend.Archivo;
import backend.Carpeta;
import backend.WordProcessor;

@WebServlet(name = "SearchServlet", urlPatterns = {"/buscar/*"})
public class SearchServlet extends HttpServlet{
	
	private WordProcessor proc;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException 
	{
		proc = WordProcessor.getInstance();
		String busq=request.getParameter("p");
		Lista<Archivo> lista ;
		
		if(busq.equals("palabra"))
		{ 
			lista = proc.darArchivosPorPalabra(busq);
		}
		else{
			lista = proc.darArchivosPorPrefijo(busq);
		}
		

		PrintWriter pw = response.getWriter();
		String contextPath = getServletContext().getRealPath(File.separator);
		response.setCharacterEncoding("UTF-8");
		File file = new File(contextPath+"resultadosBusqueda.html");
		
		String clave = request.getParameter("fname");
		
		FileInputStream fis = new FileInputStream(file);

		StringBuilder sb = new StringBuilder();
		String relleno = clave;

		try(BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF8")))
		{
			for(String linea = br.readLine();linea!=null;linea=br.readLine())
			{
				if(linea.contains("%s"))
				{
					pw.println("<h4>Búsqueda:"+ relleno+"</h4>");
				}
				else if(linea.contains("%p"))
				{
					for(Archivo a:lista)
					{
						pw.println("<li><a href=\"verArchivo?q=\""+ a.getNombre() +"\">"+a.getNombre()+"</a></li>/n");
					}
				}
				else
				{
					pw.println(linea);
				}
			}
		}


		
		
	}
//	  <li><a value="holi">First Nested Item</a></li>
//      <li><a onclick="myFunction()">Second Nested Item</a></li>
//      <li><a onclick="myFunction()">Third Nested Item</a></li>
//      <li><a onclick="myFunction()">Fourth Nested Item</a></li>

}
