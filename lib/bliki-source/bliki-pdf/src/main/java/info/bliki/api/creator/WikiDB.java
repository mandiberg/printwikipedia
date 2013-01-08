package info.bliki.api.creator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * A simple Apache Derby Database to store the retrieved Wiki contents
 * 
 */
public class WikiDB {
	/**
	 * SQL statement to select the wiki content for a given topic name
	 */
	private final PreparedStatement fSelectContent;

	/**
	 * SQL statement to insert a new wiki entry (topic and content)
	 */
	private final PreparedStatement fInsertTopic;

	/**
	 * SQL statement to update the wiki content for a given topic name
	 */
	private final PreparedStatement fUpdateTopicContent;

	/**
	 * SQL statement to select the image data for a given image name
	 */
	private final PreparedStatement fSelectImage;

	/**
	 * SQL statement to insert a new image data entry
	 */
	private final PreparedStatement fInsertImage;

	/**
	 * SQL statement to update an existing image data entry (url and filename)
	 */
	private static PreparedStatement fUpdateImage;

	private Connection fConnection;

	/**
	 * The Wiki database constructor. Creates a new Derby Wiki database, if it
	 * doesn't already exists.
	 * 
	 * 
	 * @param directory
	 *          the main directory name where the database subdirectory should be
	 *          created
	 * @param databaseSubdirectoryName
	 *          the subdirectory name where the database files should be stored.
	 *          This directory should not exist if you would like to create a
	 *          completely new database.
	 * @throws Exception
	 */
	public WikiDB(String directory, String databaseSubdirectoryName) throws Exception {
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		Properties properties = new Properties();
		properties.put("user", "user1");
		properties.put("password", "user1");
		String jdbcUrl;
		if (directory.charAt(directory.length() - 1) == '/') {
			jdbcUrl = "jdbc:derby:" + directory + databaseSubdirectoryName + ";create=true;characterEncoding=utf-8";
		} else {
			jdbcUrl = "jdbc:derby:" + directory + "/" + databaseSubdirectoryName + ";create=true;characterEncoding=utf-8";
		}
		fConnection = DriverManager.getConnection(jdbcUrl, properties);
		createTableIfItDoesntExist();

		fSelectContent = fConnection.prepareStatement("SELECT version_content FROM topic WHERE topic_name = ?");
		fInsertTopic = fConnection.prepareStatement("INSERT INTO topic (topic_name, version_content) VALUES (?,?)");
		fUpdateTopicContent = fConnection.prepareStatement("UPDATE topic SET version_content = ?  WHERE topic_name = ?");

		fSelectImage = fConnection.prepareStatement("SELECT image_url, image_filename FROM image WHERE image_name = ?");
		fInsertImage = fConnection.prepareStatement("INSERT INTO image (image_name, image_url, image_filename) VALUES (?,?, ?)");
		fUpdateImage = fConnection.prepareStatement("UPDATE image SET image_url = ?,  image_filename = ? WHERE image_name = ?");
	}

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
		// showContentsOfTableTest();
		fSelectContent.close();
		fInsertTopic.close();
		fUpdateTopicContent.close();
		fSelectImage.close();
		fInsertImage.close();
		fUpdateImage.close();
		fConnection.close();
	}

	/**
	 * Select the topic data from the database
	 * 
	 * @param name
	 *          the name of the topic
	 * @return <code>null</code> if no data was found
	 * @throws Exception
	 */
	public TopicData selectTopic(String name) throws Exception {
		TopicData topicData = new TopicData(name);
		fSelectContent.setString(1, name);
		ResultSet resultSet = fSelectContent.executeQuery();
		try {
			while (resultSet.next()) {
				topicData.setContent(resultSet.getString(1));
				return topicData;
			}
		} finally {
			resultSet.close();
		}
		return null;
	}

	public void insertTopic(TopicData topic) throws Exception {
		fInsertTopic.setString(1, topic.getName());
		fInsertTopic.setString(2, topic.getContent());
		fInsertTopic.execute();
	}

	public void updateTopic(TopicData topic) throws Exception {
		fUpdateTopicContent.setString(1, topic.getContent());
		fUpdateTopicContent.setString(2, topic.getName());
		fUpdateTopicContent.execute();
	}

	/**
	 * Select the image data from the database
	 * 
	 * @param imageName
	 *          the name of the image
	 * @return <code>null</code> if no data was found
	 * @throws Exception
	 */
	public ImageData selectImage(String imageName) throws Exception {
		ImageData imageData = new ImageData(imageName);
		fSelectImage.setString(1, imageName);
		ResultSet resultSet = fSelectImage.executeQuery();
		try {
			while (resultSet.next()) {
				imageData.setUrl(resultSet.getString(1));
				imageData.setFilename(resultSet.getString(2));
				return imageData;
			}
		} finally {
			resultSet.close();
		}
		return null;
	}

	public void insertImage(ImageData imageData) throws Exception {
		fInsertImage.setString(1, imageData.getName());
		fInsertImage.setString(2, imageData.getUrl());
		fInsertImage.setString(3, imageData.getFilename());
		fInsertImage.execute();
	}

	public void updateImage(ImageData imageData) throws Exception {
		fUpdateTopicContent.setString(1, imageData.getUrl());
		fUpdateTopicContent.setString(2, imageData.getFilename());
		fUpdateTopicContent.setString(3, imageData.getName());
		fUpdateTopicContent.execute();
	}

	private void createTableIfItDoesntExist() throws Exception {

		ResultSet resultSet = fConnection.getMetaData().getTables("%", "%", "%", new String[] { "TABLE" });
		// int columnCnt = resultSet.getMetaData().getColumnCount();
		boolean shouldCreateTableTopic = true;
		boolean shouldCreateTableImage = true;
		String tableName = null;
		while (resultSet.next() && shouldCreateTableTopic) {
			tableName = resultSet.getString("TABLE_NAME");
			if (tableName.equalsIgnoreCase("topic")) {
				shouldCreateTableTopic = false;
			} else if (tableName.equalsIgnoreCase("image")) {
				shouldCreateTableImage = false;
			}
		}
		resultSet.close();
		if (shouldCreateTableTopic) {
			System.out.println("Creating Table topic...");
			Statement statement = fConnection.createStatement();
			statement.execute("CREATE TABLE topic "
					+ "(topic_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ " topic_name VARCHAR(256) NOT NULL, " + " version_content CLOB, " + " CONSTRAINT PK_Topic PRIMARY KEY (topic_id) "
					+ " ) ");
			statement.execute("CREATE INDEX indx_topic ON topic(topic_name)");
			statement.close();
		}
		if (shouldCreateTableImage) {
			System.out.println("Creating Table image...");
			Statement statement = fConnection.createStatement();
			statement.execute("CREATE TABLE image "
					+ "(image_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ " image_name VARCHAR(256) NOT NULL, " + " image_url VARCHAR(1024) NOT NULL, "
					+ " image_filename VARCHAR(1024) NOT NULL, " + " CONSTRAINT PK_Image PRIMARY KEY (image_id) " + " ) ");
			statement.execute("CREATE INDEX indx_image ON image(image_name)");
			statement.close();
		}
	}

}