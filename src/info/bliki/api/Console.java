package info.bliki.api;

import info.bliki.wiki.model.WikiModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Console {
    /**
     * Constant for an empty char array
     */
    public static final char[] NO_CHAR = new char[0];

    private static final int DEFAULT_READING_SIZE = 8192;

    public static void main(String[] args) {
        try {
            String image = "http://en.wikipedia.org/wiki/${image}";
            String link = "http://en.wikipedia.org/wiki/${title}";
            String encoding = UTF_8.name();
            String top = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n";
            String header = "<head>\n</head>\n<body>\n";
            String bottom = "\n</body></html>";
            if (args.length >= 1) {
                Properties prop = new Properties();
                prop.load(new FileInputStream(args[0]));
                String temp;
                temp = prop.getProperty("image");
                if (temp != null) {
                    image = temp;
                }
                temp = prop.getProperty("link");
                if (temp != null) {
                    link = temp;
                }
                temp = prop.getProperty("encoding");
                if (temp != null) {
                    encoding = temp;
                }
                temp = prop.getProperty("top");
                if (temp != null) {
                    top = temp;
                }
                temp = prop.getProperty("header");
                if (temp != null) {
                    header = temp;
                }
                temp = prop.getProperty("bottom");
                if (temp != null) {
                    bottom = temp;
                }
            }
            if (args.length >= 3) {
                FileInputStream inStream = new FileInputStream(args[1]);
                FileOutputStream outStream = new FileOutputStream(args[2]);
                char[] wikiChars = getInputStreamAsCharArray(inStream, -1, encoding);
                String wikiText = new String(wikiChars);
                WikiModel wikiModel = new WikiModel(image, link);
                String htmlStr = wikiModel.render(wikiText, false);
                StringBuffer buff = new StringBuffer();
                buff.append(top);
                buff.append(header);
                buff.append(htmlStr);
                buff.append(bottom);
                byte[] buffer = buff.toString().getBytes();
                outStream.write(buffer);
                inStream.close();
                outStream.close();
            } else {
                // user input through console
                final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

                while (true) {
                    String s = in.readLine();
                    if (s == null || s.equalsIgnoreCase("exit")) {
                        break;
                    }
                    WikiModel wikiModel = new WikiModel(image, link);
                    try {
                        wikiModel.setUp();
                        String htmlStr = wikiModel.render(s, false);
                        System.out.print(htmlStr);
                    } finally {
                        wikiModel.tearDown();
                    }
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the given input stream's contents as a character array. If a length
     * is specified (i.e. if length != -1), only length chars are returned.
     * Otherwise all chars in the stream are returned. Note this doesn't close the
     * stream.
     *
     * @throws IOException
     *           if a problem occurred reading the stream.
     */
    public static char[] getInputStreamAsCharArray(InputStream stream, int length, String encoding) throws IOException {
        InputStreamReader reader = null;
        reader = encoding == null ? new InputStreamReader(stream) : new InputStreamReader(stream, encoding);
        char[] contents;
        if (length == -1) {
            contents = NO_CHAR;
            int contentsLength = 0;
            int amountRead = -1;
            do {
                int amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE);

                // resize contents if needed
                if (contentsLength + amountRequested > contents.length) {
                    System.arraycopy(contents, 0, contents = new char[contentsLength + amountRequested], 0, contentsLength);
                }

                // read as many chars as possible
                amountRead = reader.read(contents, contentsLength, amountRequested);

                if (amountRead > 0) {
                    // remember length of contents
                    contentsLength += amountRead;
                }
            } while (amountRead != -1);

            // resize contents if necessary
            if (contentsLength < contents.length) {
                System.arraycopy(contents, 0, contents = new char[contentsLength], 0, contentsLength);
            }
        } else {
            contents = new char[length];
            int len = 0;
            int readSize = 0;
            while ((readSize != -1) && (len != length)) {
                // We record first the read size. In this case len is the actual read
                // size.
                len += readSize;
                readSize = reader.read(contents, len, length - len);
            }
            // Now we need to resize in case the default encoding used more than
            // one byte for each
            // character
            if (len != length)
                System.arraycopy(contents, 0, (contents = new char[len]), 0, len);
        }

        return contents;
    }
}
