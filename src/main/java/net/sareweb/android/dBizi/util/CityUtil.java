package net.sareweb.android.dBizi.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sareweb.android.dBizi.model.City;
import net.sareweb.android.dBizi.model.Station;
import net.sareweb.android.dBizi.util.DBiziConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CityUtil {

	public static City initCity(String idioma) {

List<Station> stations = new ArrayList<>();
		
		String datuak = estazioDatuakLortu();

		Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(datuak);
		List<String> estazioak = new ArrayList<>();
		int pos = -1;
		while (matcher.find(pos + 1)) {
			pos = matcher.start();
			estazioak.add(matcher.group(1));
			System.out.println("kasu, hau da estazioa:" + matcher.group(1));
		}
		
		for (int i = 1; i < estazioak.size(); i++) {
			String balioak[] = estazioak.get(i).split(",");
			String izena = balioak[2].replaceAll("\"", "").replace("\\u00f1",
					"ñ");
			// euskaratu
			if (idioma == "eu") {
				switch (izena) {
				case "Ayuntamiento":
					izena = "Udaletxea";
					break;
				case "Paseo Francia":
					izena = "Frantzia ibilbidea";
					break;
				case "Magisterio":
					izena = "Magisteritza";
					break;
				case "Av. Zarautz":
					izena = "Zarautz etorbidea";
					break;
				case "Plaza Cataluña":
					izena = "Katalunia plaza";
					break;
				case "Universidades":
					izena = "Unibertsitateak";
					break;
				default:
					break;
				}
			}
			// stazio berria sortu
			Station stazioa = new Station();
			stazioa.setId(i + 9);
			stazioa.setNombre(izena);
			stazioa.setLatitud(Double.parseDouble(balioak[0]));
			stazioa.setLongitud(Double.parseDouble(balioak[1]));
			stazioa.setPlazasTotales(Integer.toString(Integer
					.parseInt(balioak[3]) + Integer.parseInt(balioak[4])));
			stazioa.setBicisDisponibles(balioak[4]);
			stations.add(stazioa);//stazioa batu
		}
				return new City(DBiziConstants.BDIZI_CITY_NAME, stations);
	}

	private static String estazioDatuakLortu() {
		Document doc = null;
		try {
			doc = Jsoup.connect(DBiziConstants.SERVER).get();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Element scripta = doc.getElementsByTag("script").last()
				.previousElementSibling();
		String scriptdata = scripta.data();
		String scriptdatak[] = scriptdata.split("=");
		return scriptdatak[1];
	}

	private static String TAG = "CityUtils";

}
