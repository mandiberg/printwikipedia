package info.bliki.wiki.template;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * A template parser function for <code>{{formatnum: ... }}</code> <i>lower
 * case</i> syntax. See <a
 * href="http://en.wikipedia.org/wiki/Help:Variable#Formatting">Wikipedia -
 * Help:Variable#Formatting</a>
 *
 */
public class Formatnum extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Formatnum();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (list.size() > 0) {
            String result = isSubst ? list.get(0) : parseTrim(list.get(0), model);
            if (result.length() > 0) {
                try {
                    NumberFormat nf = NumberFormat.getNumberInstance(model.getLocale());
                    if (list.size() > 1 && list.get(1).equalsIgnoreCase("r")) {
                        Number num = nf.parse(result);
                        if (num instanceof Double) {
                            result = Expr.getWikiNumberFormat(num.doubleValue(), model);
                        } else {
                            result = num.toString();
                        }
                    } else {
                        Double dbl = new Double(result);
                        // decimal number that will be rounded down by NumberFormat#format()?
                        if (result.endsWith(".")) {
                            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(model.getLocale());
                            result = nf.format(dbl) + df.getDecimalFormatSymbols().getDecimalSeparator();
                        } else if (dbl == dbl.intValue()) {
                            int idx = result.indexOf('.');
                            if (idx != -1) {
                                nf.setMinimumFractionDigits(result.length() - 1 - idx);
                            }
                            result = nf.format(dbl);
                        } else {
                            result = nf.format(dbl);
                        }
                    }
                } catch (Exception ex) {
                    if (Configuration.DEBUG) {
                        System.out.println("formatnum error: " + list.toString());
                    }
                    if (Configuration.STACKTRACE) {
                        ex.printStackTrace();
                    }
                }
            }
            return result;
        }
        return null;
    }
}
