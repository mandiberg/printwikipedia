/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wikitopdf.html;

import info.bliki.wiki.model.WikiModel;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiPdfModel extends WikiModel {

    /**
     *
     */
    public WikiPdfModel() {
        super("", "http://en.wikipedia.org/wiki/");
    }

    @Override
    public void addCategory(String categoryName, String sortKey) {
        super.addCategory(categoryName.toUpperCase(), sortKey);
    }
}
