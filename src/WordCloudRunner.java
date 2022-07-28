//made by roger
//class for running the code

public class WordCloudRunner {
	
	//enters in a url and uh dabs on it very hard until a picture comes out
	//might take like 20 minutes and 20% of cpu due to constipation
	public static void main(String[] args) throws Exception {
		WordCloud cloud = new WordCloud("https://www.cfisd.net/en");
		WordCloudDisplay display = new WordCloudDisplay(cloud, 800, 600, 250, "CFISD's Main Website");
		display.show();
	}
	
}
