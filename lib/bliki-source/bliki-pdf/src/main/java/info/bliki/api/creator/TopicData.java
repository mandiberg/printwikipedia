package info.bliki.api.creator;

public class TopicData {
	String fTopicName;

	String fTopicContent;

	public TopicData(String name) {
		this(name, "");
	}

	public TopicData(String name, String content) {
		fTopicName = name;
		fTopicContent = content;
	}

	public String getName() {
		return fTopicName;
	}

	public void setName(String topicName) {
		this.fTopicName = topicName;
	}

	public String getContent() {
		return fTopicContent;
	}

	public void setContent(String topicContent) {
		this.fTopicContent = topicContent;
	}
}
