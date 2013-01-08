package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.BaseToken;

import java.util.List;


public interface IHTMLToWiki {

	/**
	 * 
	 * @param nodes
	 * @param resultBuffer
	 *          the converted Wiki string
	 */
	public abstract void nodesToText(List nodes, StringBuilder resultBuffer);

	/**
	 * Convert the given HTML <code>node</code> into a <code>wikiText</code>
	 * 
	 * @param node
	 * @param wikiText
	 */
	public abstract void nodeToWiki(BaseToken node, StringBuilder wikiText);

	/**
	 * Convert the given HTML <code>node</code> into  <code>plainText</code>
	 * @param node
	 * @param plainText
	 */
	public abstract void nodesToPlainText(BaseToken node, StringBuilder plainText);

}