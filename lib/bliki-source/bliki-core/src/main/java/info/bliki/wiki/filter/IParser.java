package info.bliki.wiki.filter;

import info.bliki.wiki.model.IWikiModel;


public interface IParser
{

	public void setModel(IWikiModel wikiModel);
	
	/** 
	 * Display <i>no table of contents</i>?
	 * 
	 * @return
	 */
	public boolean isNoToC();

	/**
	 * Set the <i>no table of contents</i> render mode
	 * 
	 * @param noToC
	 */
	public void setNoToC(boolean noToC);
	
	/**
	 * The text is rendered as a template
	 * @return
	 */
	public boolean isTemplate();
	
}