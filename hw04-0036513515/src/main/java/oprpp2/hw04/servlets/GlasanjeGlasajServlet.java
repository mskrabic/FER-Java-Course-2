package oprpp2.hw04.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet za glasanje.
 * 
 * @author mskrabic
 *
 */
public class GlasanjeGlasajServlet extends HttpServlet {
	
	/**
	 * Serijski broj.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path path = Path.of(fileName);
		String id = req.getParameter("id");
		
		if (!path.toFile().exists())
			Files.createFile(path);
		
		List<String> lines;
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			System.out.println("Error while reading from file.");
			return;
		}
		boolean firstVote = true;
		List<String> newLines = new ArrayList<>();
		for (String line : lines) {
			if (!line.startsWith(id)) {
				newLines.add(line);
				continue;
			}
			String[] parts = line.split("\t");
			int votes = Integer.parseInt(parts[1]) + 1;
			line = parts[0] + "\t" + votes;
			firstVote = false;
			newLines.add(line);
		}
		if (firstVote) {
			String line = id + "\t" + "1";
			newLines.add(line);
		}
		
		try {
			Files.write(Path.of(fileName), newLines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
		} catch (IOException e) {
			System.out.println("Error while writing to file.");
			return;
		}
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
}
