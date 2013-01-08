package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.Reference;

import java.io.IOException;
import java.util.List;

/**
 * Wiki tag for references &lt;references /&gt;
 * 
 * See <a href="http://en.wikipedia.org/wiki/Wikipedia:Footnotes">Footnotes</a>
 */
public class ReferencesTag extends HTMLTag {
	public ReferencesTag() {
		super("references");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException {
		List<Reference> list = model.getReferences();
		if (list != null) {
			Reference ref;
			int counter;
			writer.append("<ol class=\"references\">\n");
			for (int i = 0; i < list.size(); i++) {
				ref = (Reference) list.get(i);
				counter = ref.getCounter();
				writer.append("<li id=\"_note-");
				if (counter == 0) {
					String i1 = Integer.toString(i + 1);
					writer.append(i1);
					writer.append("\"><b><a href=\"#_ref-");
					writer.append(i1);
					// upwards arrow
					writer.append("\" title=\"\">&#8593;</a></b> ");//&uarr;
				} else {
					String note;
					String nameAttribute = ref.getAttribute();
					char ch;
					writer.append(nameAttribute);
					writer.append("\">&#8593; "); //&uarr;
					for (int j = 0; j <= counter; j++) {
						if (j >= Reference.CHARACTER_REFS.length()) {
							ch = 'Z';
						} else {
							ch = Reference.CHARACTER_REFS.charAt(j);
						}
						note = nameAttribute + '_' + ch;
						writer.append("<a href=\"#_ref-");
						writer.append(note);
						writer.append("\" title=\"\"><sup><i><b>" + ch + "</b></i></sup></a> ");
					}
				}
				writer.append(ref.getRefString());
				writer.append("</li>");
			}
			writer.append("</ol>");
		}
	}
 
	@Override
	public boolean isReduceTokenStack() {
		return false;
	}
	
	@Override
	public String getParents() {
		return Configuration.SPECIAL_BLOCK_TAGS;
	}
}