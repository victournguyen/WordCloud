//made by roger
//class for running the code

public class WordCloudRunner {
	
	public static void main(String[] args) throws Exception {
		WordCloud cloud = new WordCloud("https://www.cfisd.net/en");
		WordCloudDisplay display = new WordCloudDisplay(cloud, 800, 600, 250, "CFISD's Main Website");
		display.show();
	}
	
}
