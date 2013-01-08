package info.bliki.api.query;

/**
 * Module Sitematrix <b> action=sitematrix (sm) </b>.<br/> Get Wikimedia sites
 * list.
 * 
 * Example: <a
 * href="http://en.wikipedia.org/w/api.php?action=sitematrix">api.php
 * ?action=sitematrix</a>
 * 
 */
public class Sitematrix extends RequestBuilder {
	public Sitematrix() {
		super();
		action("sitematrix");
	}
	
	public static Sitematrix create() {
		return new Sitematrix();
	}
}
