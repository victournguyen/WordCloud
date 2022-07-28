import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//made by victor
//stores info about the word cloud

public class WordCloud {
	
	public Map<String, Integer> weights;
	public int max_weight;
	public List<Entry<String, Integer>> entries;
	public String url;
	
	//collects information from the URL
	public WordCloud(String url) throws IOException {
		this.url = url;
		weights = HTMLYoinker.getWeights(url);
		max_weight = Collections.max(weights.values());
		entries = new ArrayList<Entry<String, Integer>>(weights.entrySet());
		Comparator<Entry<String, Integer>> entrycomp = (arg0, arg1) -> {
			return arg1.getValue() - arg0.getValue();
		};
		Collections.sort(entries, entrycomp);
	}
	
}
