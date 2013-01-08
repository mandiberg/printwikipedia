package info.bliki.wiki.model;



/**
 * Wiki model implementation which allows some simple additional BB code syntax inside the wiki syntax
 * 
 * See <a href="http://en.wikipedia.org/wiki/BBCode">BBCode</a>
 * 
 */
public class BBCodeModel extends WikiModel
{
	public BBCodeModel(String imageBaseURL, String linkBaseURL)
	{
		super(imageBaseURL, linkBaseURL);
	}

	@Override
	public boolean parseBBCodes()
	{
		return true;
	}
}
