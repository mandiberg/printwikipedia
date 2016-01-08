package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;


public class ATag extends HTMLTag {
    public ATag() {
        super("a");
    }

    @Override
    public boolean isReduceTokenStack() {
        return false;
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        if (converter.renderLinks()) {
            super.renderHTML(converter, buf, model);
        } else {
            List<Object> children = getChildren();
            if (children.size() != 0) {
                converter.nodesToText(children, buf, model);
            }
        }
    }

    @Override
    public boolean addAttribute(String attName, String attValue, boolean checkXSS) {
        super.addAttribute(attName, attValue, checkXSS);
        if (attName != null && attValue != null && attName.equalsIgnoreCase("href")) {
            String valueLowerCased = attValue.trim().toLowerCase();
            if (valueLowerCased.startsWith("http:") || valueLowerCased.startsWith("https:") || valueLowerCased.startsWith("ftp:")
                    || valueLowerCased.startsWith("ftps:") || valueLowerCased.startsWith("mailto:")) {
                addAttribute("rel", "nofollow", true);
                return true;
            }
        }
        return false;
    }

}
