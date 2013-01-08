package info.bliki.api;

/**
 * Manages category data from the <a href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
 */
public class Category {
	String ns;

	String title;

	public Category() {
		ns = "";
		title = "";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Category) {
			return title.equals(((Category) obj).title) && ns.equals(((Category) obj).ns);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return title.hashCode();
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
