package oprpp2.hw04.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import oprpp2.hw04.servlets.GlasanjeRezultatiServlet.Result;
import oprpp2.hw04.servlets.GlasanjeServlet.Band;

/**
 * Pomoćni razred sa statičkim metodama za čitanje podataka o glasanju iz datoteka.
 * 
 * @author mskrabic
 *
 */
public class Utils {
	
	/**
	 * Čita sve linije iz datoteke i vraća ih u listi.
	 * 
	 * @param fileName	ime datoteke.
	 * 
	 * @return	lista svih linija u datoteci.
	 * 
	 * @throws IOException	u slučaju pogreške.
	 */
	public static List<String> readFrom(String fileName) throws IOException {
		Path path = Path.of(fileName);
		if (!path.toFile().exists())
			try {
				Files.createFile(path);
			} catch (Exception e) {
				System.out.println("Unable to create the results file.");
				return null;
			}
		
		return Files.readAllLines(path);
	}
	
	/**
	 * Čita sve linije iz datoteke i parsira ih u listu {@link Band} objekata.
	 * 
	 * @param fileName	ime datoteke.
	 * 
	 * @return	lista bendova.
	 * 
	 * @throws IOException u slučaju pogreške.
	 */
	public static List<Band> readBands(String fileName) throws IOException {
		List<String> lines = Utils.readFrom(fileName);
		List<Band> bands = new ArrayList<>();
		for (String line : lines) {
			String[] parts = line.split("\t");
			if (parts.length != 3) continue;
			
			bands.add(new Band(parts[0], parts[1], parts[2]));
		}
		
		return bands;
	}

	/**
	 * Čita sve linije iz datoteke i parsira ih u listu {@link Result} objekata.
	 * 
	 * @param fileName	ime datoteke.
	 * @param bands	lista bendova.
	 * 
	 * @return	lista rezultata.
	 * 
	 * @throws IOException u slučaju pogreške.
	 */
	public static List<Result> getResults(String fileName, List<Band> bands) throws IOException {
		List<String> lines = Utils.readFrom(fileName);	
		List<Result> results = new ArrayList<>();
		for (String line : lines) {
			String[] parts = line.split("\t");
			if (parts.length != 2) continue;
			for (Band band : bands) {
				if (band.getId().equals(parts[0])) {
					results.add(new Result(band, parts[1]));					
				}
			}
		}
		return results;
	}

}
