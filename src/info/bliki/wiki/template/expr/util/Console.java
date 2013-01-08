package info.bliki.wiki.template.expr.util;

import info.bliki.wiki.template.expr.SyntaxError;
import info.bliki.wiki.template.expr.eval.DoubleEvaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * A java console program to run the evaluator interactively. Utility for
 * testing the <code>{{ #expr: ... }}</code> and
 * <code>{{ #ifexpr: ... }}</code> expression evaluator.
 * 
 */
public class Console {

	private File fFile;

	public static void main(final String args[]) {
		printUsage();
		Console console;
		try {
			console = new Console();
		} catch (final SyntaxError e1) {
			e1.printStackTrace();
			return;
		}
		String expr = null;
		console.setArgs(args);
		final File file = console.getFile();
		if (file != null) {
			try {
				final BufferedReader f = new BufferedReader(new FileReader(file));
				final StringBuffer buff = new StringBuffer(1024);
				String line;
				while ((line = f.readLine()) != null) {
					buff.append(line);
					buff.append("\n");
				}
				f.close();
				System.out.println(console.interpreter(buff.toString()));
			} catch (final IOException ioe) {
				final String msg = "Cannot read from the specified file. " + "Make sure the path exists and you have read permission.";
				System.out.println(msg);
				return;
			}
		}

		while (true) {
			try {
				expr = console.readString(System.out, ">>> ");
				if (expr != null) {
					if ((expr.length() >= 4) && expr.toLowerCase().substring(0, 4).equals("exit")) {
						break;
					}
					System.out.println(console.interpreter(expr));
				}
			} catch (final Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Prints the usage of how to use this class to System.out
	 */
	private static void printUsage() {
		final String lineSeparator = System.getProperty("line.separator");
		final StringBuffer msg = new StringBuffer();
		msg.append(Console.class.getCanonicalName() + " [options]" + lineSeparator);
		msg.append(lineSeparator);
		msg.append("Options: " + lineSeparator);
		msg.append("  -h or -help                  print this message" + lineSeparator);
		msg.append("  -f or -file <filename>       use given file as input" + lineSeparator);
		msg.append("To stop the program type: " + lineSeparator);
		msg.append("exit<RETURN-KEY>" + lineSeparator);
		msg.append("****+****+****+****+****+****+****+****+****+****+****+****+");

		System.out.println(msg.toString());
	}

	public Console() throws SyntaxError {
		super();
	}

	/**
	 * Sets the arguments for the <code>main</code> method
	 * 
	 * @param args
	 *          the aruments of the program
	 */
	private void setArgs(final String args[]) {
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i];
			if (arg.equals("-help") || arg.equals("-h")) {
				printUsage();
				return;
			} else if (arg.equals("-file") || arg.equals("-f")) {
				try {
					fFile = new File(args[i + 1]);
					i++;
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					final String msg = "You must specify a file when " + "using the -file argument";
					System.out.println(msg);
					return;
				}
			} else if (arg.startsWith("-")) {
				// we don't have any more args to recognize!
				final String msg = "Unknown arg: " + arg;
				System.out.println(msg);
				printUsage();
				return;
			}

		}

	}

	/**
	 * Evaluates the given string-expression and returns the result in
	 * <code>OutputForm</code>
	 * 
	 * @param strEval
	 * 
	 */
	public String interpreter(final String strEval) {
		try {
			DoubleEvaluator engine = new DoubleEvaluator();
			double d = engine.evaluate(strEval);
			return Double.toString(d);
		} catch (SyntaxError e) {
			System.err.println();
			System.err.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * prints a prompt on the console but doesn't print a newline
	 * 
	 * @param out
	 * @param prompt
	 *          the prompt string to display
	 * 
	 */
	public void printPrompt(final PrintStream out, final String prompt) {
		out.print(prompt);
		out.flush();
	}

	/**
	 * read a string from the console. The string is terminated by a newline
	 * 
	 * @param out
	 *          Description of Parameter
	 * @return the input string (without the newline)
	 */

	public String readString(final PrintStream out) {
		final StringBuffer input = new StringBuffer();
		final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;

		try {
			while (!done) {
				final String s = in.readLine();
				if (s != null) {
					if ((s.length() > 0) && (s.charAt(s.length() - 1) != '\\')) {
						input.append(s);
						done = true;
					} else {
						if (s.length() > 1) {
							input.append(s.substring(0, s.length() - 1));
						} else {
							input.append(" ");
						}
					}
				}
			}
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
		return input.toString();
	}

	/**
	 * read a string from the console. The string is terminated by a newline
	 * 
	 * @param prompt
	 *          the prompt string to display
	 * @param out
	 *          Description of Parameter
	 * @return the input string (without the newline)
	 */

	public String readString(final PrintStream out, final String prompt) {
		printPrompt(out, prompt);
		return readString(out);
	}

	/**
	 * @param file
	 */
	public void setFile(final File file) {
		fFile = file;
	}

	/**
	 * @return the file with which the program was started or <code>null</code>
	 */
	public File getFile() {
		return fFile;
	}

}