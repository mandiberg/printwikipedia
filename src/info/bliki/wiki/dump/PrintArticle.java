package info.bliki.wiki.dump;

import java.io.IOException;

import info.bliki.api.Connector;
import info.bliki.wiki.model.WikiModel;

/**
 * Example filter which prints the rendered HTML articles to System.out
 * 
 * The number of processed articles is limited by a maximum counter
 * 
 */
public class PrintArticle implements IArticleFilter {
	private WikiModel wikiModel;

	private int counter;

	private int max_counter;

	/**
	 * Example filter which prints the rendered HTML articles to System.out
	 * 
	 * The number of processed articles is limited by a maximum counter
	 * <code>max_count</code>
	 * 
	 */
	public PrintArticle(int max_count) {
		wikiModel = new WikiModel("${image}", "${title}");
		counter = 0;
		max_counter = max_count;
	}

	public boolean process(WikiArticle article) {
		counter++;
		if (counter >= max_counter) {
			return false;
		}
		String htmlText = "";
		htmlText = wikiModel.render(article.getText());
		if (htmlText == null) {
			System.out.println("An IOException occured!");
		} else {
			System.out.println(htmlText);
		}
		return true;
	}

	public static void main(String args[]) throws Exception {
		// OutputStream fout = new FileOutputStream("data.txt");
		// OutputStream bout = new BufferedOutputStream(fout);
		// Writer out = new OutputStreamWriter(bout, Connector.UTF8_CHARSET);

		// test for Wikipedia input
		IArticleFilter filter = new PrintArticle(999999);
		WikiXMLParser parser = new WikiXMLParser("C:/temp/v1.xml", filter);
		parser.parse();
	}
}
