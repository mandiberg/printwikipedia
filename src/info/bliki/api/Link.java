package info.bliki.api;

/**
 * Manages page link data from the <a href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
 */
public class Link {
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Link) {
			return title.equals(((Link) obj).title) && ns.equals(((Link) obj).ns);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return title.hashCode();
	}

	String ns;

	String title;

	public Link() {
		ns = "";
		title = "";
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Ns: " + ns + "; Title: " + title;
	}
}
