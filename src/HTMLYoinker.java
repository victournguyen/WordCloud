import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
//made by roger
//scraper for html

public class HTMLYoinker {
	
	//fills and returns a Map with the word and the value of the word
	public static Map<String, Integer> getWeights(String url) throws MalformedURLException, IOException {

		//takes in a url and removes html formatting
		URL url_s = new URL(url);
		URLConnection site = url_s.openConnection();
		site.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) "
                + "AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		site.connect();
		Scanner scraper = new Scanner(site.getInputStream());
		String content = scraper.useDelimiter("\\Z").next();
		content = content.trim().replaceAll(">", ">\n").replaceAll("<", "\n<").replaceAll("\n+", "\n");

		//places the words into the map as well as a number of points depending on where the word is
		Scanner weight_scraper = new Scanner(content);
		Map<String, Integer> weights = new HashMap<String, Integer>();
		boolean h1 = false, h2 = false, h3 = false, h4 = false, h5 = false, h6 = false, ahref = false, p = false, title = false, ul = false, li = false;
		while (weight_scraper.hasNext()) {
			String line = weight_scraper.nextLine().trim();
			if (line.length() == 0)
				continue;
			
			switch (line) {
				case "<p>": p = true; break;
				case "<title>": title = true; break;
				case "<h1>": h1 = true; break;
				case "<h2>": h2 = true; break;
				case "<h3>": h3 = true; break;
				case "<h4>": h4 = true; break;
				case "<h5>": h5 = true; break;
				case "<h6>": h6 = true; break;
				case "<ul>": ul = true; break;
				case "<li>": li = true; break;
				case "</p>": p = false; break;
				case "</title>": title = false; break;
				case "</h1>": h1 = false; break;
				case "</h2>": h2 = false; break;
				case "</h3>": h3 = false; break;
				case "</h4>": h4 = false; break;
				case "</h5>": h5 = false; break;
				case "</h6>": h6 = false; break;
				case "</ul>": ul = false; break;
				case "</li>": li = false; break;
				case "</a>": ahref = false; break;
			}
			if (line.matches("<a.*href ?= ?\".*\">"))
				ahref = true;
			if (!(line.matches("<.*>")) && (h1|h2|h3|h4|h5|h6|ahref|p|title|(ul&li))) {
				String[] words = line.split("[ \\-_]");
				for (String word : words) {
					String word_lower = word.toLowerCase();
					if (word_lower.length() == 0 || word_lower.matches(".*[^a-z].*"))
						continue;
					if (!weights.containsKey(word_lower)) {
						weights.put(word_lower, 0);
					}

					//calculates the amount of points for each word
					int add = 0;
					if (h1) add += 10;
					if (h2) add += 8;
					if (h3) add += 6;
					if (h4) add += 4;
					if (h5) add += 2;
					if (h6) add++;
					if (p) add++;
					if (title) add += 10;
					if (ahref) add += 5;
					if (ul&li) add++;
					weights.put(word_lower, weights.get(word_lower) + add);
				}
			}
		}
		scraper.close();
		weight_scraper.close();
		return weights;
	}
	
}
