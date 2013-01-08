package info.bliki.api.creator;

public class ImageData {
	String fImageName;

	String fImageUrl;

	String fImageFilename;

	public ImageData(String name) {
		this(name, "", "");
	}

	public ImageData(String name, String url) {
		this(name, url, "");
	}

	public ImageData(String name, String url, String filename) {
		fImageName = name;
		fImageUrl = url;
		fImageFilename = filename;
	}

	public String getName() {
		return fImageName;
	}

	public void setName(String imageName) {
		this.fImageName = imageName;
	}

	public String getUrl() {
		return fImageUrl;
	}

	public void setUrl(String imageUrl) {
		this.fImageUrl = imageUrl;
	}

	public String getFilename() {
		return fImageFilename;
	}

	public void setFilename(String imageFilename) {
		this.fImageFilename = imageFilename;
	}
}
