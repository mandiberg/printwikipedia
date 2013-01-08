package info.bliki.wiki.model;

import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.tags.ATag;
import info.bliki.wiki.tags.BrTag;
import info.bliki.wiki.tags.DdTag;
import info.bliki.wiki.tags.DlTag;
import info.bliki.wiki.tags.DtTag;
import info.bliki.wiki.tags.HTMLBlockTag;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.HrTag;
import info.bliki.wiki.tags.MathTag;
import info.bliki.wiki.tags.NowikiTag;
import info.bliki.wiki.tags.PTag;
import info.bliki.wiki.tags.PreTag;
import info.bliki.wiki.tags.RefTag;
import info.bliki.wiki.tags.ReferencesTag;
import info.bliki.wiki.tags.SourceTag;
import info.bliki.wiki.tags.code.ABAPCodeFilter;
import info.bliki.wiki.tags.code.CSharpCodeFilter;
import info.bliki.wiki.tags.code.GroovyCodeFilter;
import info.bliki.wiki.tags.code.JavaCodeFilter;
import info.bliki.wiki.tags.code.JavaScriptCodeFilter;
import info.bliki.wiki.tags.code.PHPCodeFilter;
import info.bliki.wiki.tags.code.SourceCodeFormatter;
import info.bliki.wiki.tags.code.XMLCodeFilter;
import info.bliki.wiki.template.Anchorencode;
import info.bliki.wiki.template.Expr;
import info.bliki.wiki.template.Fullurl;
import info.bliki.wiki.template.ITemplateFunction;
import info.bliki.wiki.template.If;
import info.bliki.wiki.template.Ifeq;
import info.bliki.wiki.template.Ifexist;
import info.bliki.wiki.template.Ifexpr;
import info.bliki.wiki.template.LC;
import info.bliki.wiki.template.LCFirst;
import info.bliki.wiki.template.Localurl;
import info.bliki.wiki.template.NS;
import info.bliki.wiki.template.Padleft;
import info.bliki.wiki.template.Padright;
import info.bliki.wiki.template.Switch;
import info.bliki.wiki.template.Tag;
import info.bliki.wiki.template.Time;
import info.bliki.wiki.template.UC;
import info.bliki.wiki.template.UCFirst;
import info.bliki.wiki.template.URLEncode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Common Configuration settings
 * 
 */
public class Configuration implements IConfiguration {
	final public static String BLOCK_TAGS = "|address|blockquote|center|del|dir|div|dl|fieldset|form|h1|h2|h3|h4|h5|h6|hr|ins|isindex|menu|noframes|noscript|ol|p|pre|table|ul|";

	final public static String INLINE_TAGS = "|a|abbr|acronym|applet|snippet|b|basefont|bdo|big|br|button|cite|code|del|dfn|em|font|i|img|ins|input|iframe|kbd|label|map|object|q|samp|script|select|small|span|strong|sub|sup|textarea|tt|var|";

	final public static String SPECIAL_BLOCK_TAGS = "|applet|snippet|blockquote|body|button|center|dd|del|div|fieldset|form|iframe|ins|li|map|noframes|noscript|object|td|th|";

	final static HTMLTag HTML_A_OPEN = new ATag();

	final public static HTMLTag HTML_EM_OPEN = new HTMLTag("em");

	final public static HTMLTag HTML_H1_OPEN = new HTMLBlockTag("h1", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_H2_OPEN = new HTMLBlockTag("h2", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_H3_OPEN = new HTMLBlockTag("h3", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_H4_OPEN = new HTMLBlockTag("h4", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_H5_OPEN = new HTMLBlockTag("h5", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_H6_OPEN = new HTMLBlockTag("h6", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_ITALIC_OPEN = new HTMLTag("i");

	final public static HTMLTag HTML_BOLD_OPEN = new HTMLTag("b");

	final public static HTMLTag HTML_PARAGRAPH_OPEN = new PTag();

	final public static HTMLTag HTML_BLOCKQUOTE_OPEN = new HTMLBlockTag("blockquote", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_STRIKE_OPEN = new HTMLTag("strike");

	final public static HTMLTag HTML_STRONG_OPEN = new HTMLTag("strong");

	final public static HTMLTag HTML_UNDERLINE_OPEN = new HTMLTag("u");

	final public static HTMLTag HTML_SUB_OPEN = new HTMLTag("sub");

	final public static HTMLTag HTML_SUP_OPEN = new HTMLTag("sup");

	final public static HTMLTag HTML_CENTER_OPEN = new HTMLTag("center");

	final public static HTMLTag HTML_TT_OPEN = new HTMLTag("tt");

	final public static HTMLTag HTML_TABLE_OPEN = new HTMLBlockTag("table", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_CAPTION_OPEN = new HTMLBlockTag("caption", "|table|");

	final public static HTMLTag HTML_TH_OPEN = new HTMLBlockTag("th", "|tr|");

	final public static HTMLTag HTML_TR_OPEN = new HTMLBlockTag("tr", "|table|tbody|tfoot|thead|");

	final public static HTMLTag HTML_TD_OPEN = new HTMLBlockTag("td", "|tr|");

	final public static HTMLTag HTML_UL_OPEN = new HTMLBlockTag("ul", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_OL_OPEN = new HTMLBlockTag("ol", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_LI_OPEN = new HTMLBlockTag("li", "|dir|menu|ol|ul|");

	final public static HTMLTag HTML_FONT_OPEN = new HTMLTag("font");

	final public static HTMLTag HTML_CITE_OPEN = new HTMLTag("cite");

	final public static HTMLTag HTML_DIV_OPEN = new HTMLBlockTag("div", SPECIAL_BLOCK_TAGS);

	final public static HTMLTag HTML_DD_OPEN = new DdTag();

	final public static HTMLTag HTML_DL_OPEN = new DlTag();

	final public static HTMLTag HTML_DT_OPEN = new DtTag();

	final public static HTMLTag HTML_SPAN_OPEN = new HTMLTag("span");

	final public static HTMLTag HTML_VAR_OPEN = new HTMLTag("var");

	final public static HTMLTag HTML_CODE_OPEN = new HTMLTag("code");

	// strike-through
	final public static HTMLTag HTML_S_OPEN = new HTMLTag("s");

	// small
	final public static HTMLTag HTML_SMALL_OPEN = new HTMLTag("small");

	final public static HTMLTag HTML_BIG_OPEN = new HTMLTag("big");

	final public static HTMLTag HTML_U_OPEN = new HTMLTag("u");

	final public static HTMLTag HTML_DEL_OPEN = new HTMLTag("del");

	final public static HTMLTag HTML_PRE_OPEN = new PreTag();

	/**
	 * Interwiki links pointing to other wikis in the web
	 */
	private static final String[] INTERWIKI_STRINGS = { "be-x-old", "http://be-x-old.wikipedia.org/wiki/${title}", "tavi",
			"http://tavi.sourceforge.net/${title}", "xh", "http://xh.wikipedia.org/wiki/${title}", "lasvegaswiki",
			"http://wiki.gmnow.com/index.php/${title}", "pmeg", "http://www.bertilow.com/pmeg/${title}.php", "warpedview",
			"http://www.warpedview.com/index.php/${title}", "slashdot", "http://slashdot.org/article.pl?sid=${title}", "wikimedia",
			"http://wikimediafoundation.org/wiki/${title}", "wikia", "http://www.wikia.com/wiki/index.php/${title}", "wo",
			"http://wo.wikipedia.org/wiki/${title}", "jefo", "http://www.esperanto-jeunes.org/vikio/index.php?${title}", "openfacts",
			"http://openfacts.berlios.de/index.phtml?title=${title}", "lqwiki", "http://wiki.linuxquestions.org/wiki/${title}", "wa",
			"http://wa.wikipedia.org/wiki/${title}", "ciscavate", "http://ciscavate.org/index.php/${title}", "demokraatia",
			"http://wiki.demokraatia.ee/", "efnetpythonwiki", "http://purl.net/wiki/python/${title}", "mediazilla",
			"http://bugzilla.wikipedia.org/${title}", "wikiquote", "http://en.wikiquote.org/wiki/${title}", "jbo",
			"http://jbo.wikipedia.org/wiki/${title}", "vo", "http://vo.wikipedia.org/wiki/${title}", "vi",
			"http://vi.wikipedia.org/wiki/${title}", "gamewiki", "http://gamewiki.org/wiki/index.php/${title}", "hewikisource",
			"http://he.wikisource.org/wiki/${title}", "ve", "http://ve.wikipedia.org/wiki/${title}", "google",
			"http://www.google.com/search?q=${title}", "uz", "http://uz.wikipedia.org/wiki/${title}", "drumcorpswiki",
			"http://www.drumcorpswiki.com/index.php/${title}", "nah", "http://nah.wikipedia.org/wiki/${title}", "ur",
			"http://ur.wikipedia.org/wiki/${title}", "jiniwiki", "http://www.cdegroot.com/cgi-bin/jini?${title}", "uk",
			"http://uk.wikipedia.org/wiki/${title}", "ug", "http://ug.wikipedia.org/wiki/${title}", "osi",
			"reference model=http://wiki.tigma.ee/", "mbtest", "http://www.usemod.com/cgi-bin/mbtest.pl?${title}", "disinfopedia",
			"http://www.disinfopedia.org/wiki.phtml?title=${title}", "ty", "http://ty.wikipedia.org/wiki/${title}", "squeak",
			"http://minnow.cc.gatech.edu/squeak/${title}", "tw", "http://tw.wikipedia.org/wiki/${title}", "tlh",
			"http://tlh.wikipedia.org/wiki/${title}", "tt", "http://tt.wikipedia.org/wiki/${title}", "ts",
			"http://ts.wikipedia.org/wiki/${title}", "tr", "http://tr.wikipedia.org/wiki/${title}", "scoutpedia",
			"http://www.scoutpedia.info/index.php/${title}", "minnan", "http://zh-min-nan.wikipedia.org/wiki/${title}", "to",
			"http://to.wikipedia.org/wiki/${title}", "tn", "http://tn.wikipedia.org/wiki/${title}", "wikinfo",
			"http://www.wikinfo.org/wiki.php?title=${title}", "s23wiki", "http://is-root.de/wiki/index.php/${title}", "tl",
			"http://tl.wikipedia.org/wiki/${title}", "aiwiki", "http://www.ifi.unizh.ch/ailab/aiwiki/aiw.cgi?${title}", "tk",
			"http://tk.wikipedia.org/wiki/${title}", "ti", "http://ti.wikipedia.org/wiki/${title}", "th",
			"http://th.wikipedia.org/wiki/${title}", "tg", "http://tg.wikipedia.org/wiki/${title}", "fr.fr",
			"http://fr.fr.wikinations.org/${title}", "te", "http://te.wikipedia.org/wiki/${title}", "csb",
			"http://csb.wikipedia.org/wiki/${title}", "theopedia", "http://www.theopedia.com/${title}", "ta",
			"http://ta.wikipedia.org/wiki/${title}", "acadwiki", "http://xarch.tu-graz.ac.at/autocad/wiki/${title}", "efnetceewiki",
			"http://purl.net/wiki/c/${title}", "phpwiki", "http://phpwiki.sourceforge.net/phpwiki/index.php?${title}", "tmwiki",
			"http://www.EasyTopicMaps.com/?page=${title}", "sw", "http://sw.wikipedia.org/wiki/${title}", "benefitswiki",
			"http://www.benefitslink.com/cgi-bin/wiki.cgi?${title}", "ecxei", "http://www.ikso.net/cgi-bin/wiki.pl?${title}", "sv",
			"http://sv.wikipedia.org/wiki/${title}", "uea", "http://www.tejo.org/uea/${title}", "su",
			"http://su.wikipedia.org/wiki/${title}", "st", "http://st.wikipedia.org/wiki/${title}", "ss",
			"http://ss.wikipedia.org/wiki/${title}", "sr", "http://sr.wikipedia.org/wiki/${title}", "sq",
			"http://sq.wikipedia.org/wiki/${title}", "so", "http://so.wikipedia.org/wiki/${title}", "sn",
			"http://sn.wikipedia.org/wiki/${title}", "sm", "http://sm.wikipedia.org/wiki/${title}", "sl",
			"http://sl.wikipedia.org/wiki/${title}", "sk", "http://sk.wikipedia.org/wiki/${title}", "cache",
			"http://www.google.com/search?q=cache:${title}", "svgwiki", "http://www.protocol7.com/svg-wiki/default.asp?${title}", "si",
			"http://si.wikipedia.org/wiki/${title}", "smikipedia", "http://www.smikipedia.org/${title}", "simple",
			"http://simple.wikipedia.org/wiki/${title}", "sh", "http://sh.wikipedia.org/wiki/${title}", "sg",
			"http://sg.wikipedia.org/wiki/${title}", "gentoo-wiki", "http://gentoo-wiki.com/${title}", "se",
			"http://se.wikipedia.org/wiki/${title}", "webseitzwiki", "http://webseitz.fluxent.com/wiki/${title}", "sd",
			"http://sd.wikipedia.org/wiki/${title}", "sc", "http://sc.wikipedia.org/wiki/${title}", "jamwiki",
			"http://jamwiki.org/wiki/en/${title}", "sa", "http://sa.wikipedia.org/wiki/${title}", "greencheese",
			"http://www.greencheese.org/${title}", "linuxwiki", "http://www.linuxwiki.de/${title}", "diveintoosx",
			"http://diveintoosx.org/${title}", "bridgeswiki", "http://c2.com/w2/bridges/${title}", "rw",
			"http://rw.wikipedia.org/wiki/${title}", "ru", "http://ru.wikipedia.org/wiki/${title}", "corpknowpedia",
			"http://corpknowpedia.org/wiki/index.php/${title}", "echei", "http://www.ikso.net/cgi-bin/wiki.pl?${title}", "ro",
			"http://ro.wikipedia.org/wiki/${title}", "rn", "http://rn.wikipedia.org/wiki/${title}", "rm",
			"http://rm.wikipedia.org/wiki/${title}", "wikispecies", "http://species.wikipedia.org/wiki/${title}", "webdevwikinl",
			"http://www.promo-it.nl/WebDevWiki/index.php?page=${title}", "sourceforge", "http://sourceforge.net/${title}", "pythonwiki",
			"http://www.pythonwiki.de/${title}", "roa-rup", "http://roa-rup.wikipedia.org/wiki/${title}", "tmnet",
			"http://www.technomanifestos.net/?${title}", "gmailwiki", "http://www.gmailwiki.com/index.php/${title}", "plog4u",
			"http://plog4u.org/index.php/${title}", "googlegroups", "http://groups.google.com/groups?q=${title}", "wikiworld",
			"http://WikiWorld.com/wiki/index.php/${title}", "qu", "http://qu.wikipedia.org/wiki/${title}", "consciousness",
			"http://teadvus.inspiral.org/", "eljwiki", "http://elj.sourceforge.net/phpwiki/index.php/${title}", "lojban",
			"http://www.lojban.org/tiki/tiki-index.php?page=${title}", "usej", "http://www.tejo.org/usej/${title}", "tokipona",
			"http://tokipona.wikipedia.org/wiki/${title}", "mathsongswiki", "http://SeedWiki.com/page.cfm?wikiid=237&doc=${title}",
			"got", "http://got.wikipedia.org/wiki/${title}", "shakti", "http://cgi.algonet.se/htbin/cgiwrap/pgd/ShaktiWiki/${title}",
			"memoryalpha", "http://www.memory-alpha.org/en/index.php/${title}", "cliki", "http://ww.telent.net/cliki/${title} ", "pt",
			"http://pt.wikipedia.org/wiki/${title}", "fr.ca", "http://fr.ca.wikinations.org/${title}", "ps",
			"http://ps.wikipedia.org/wiki/${title}", "fur", "http://fur.wikipedia.org/wiki/${title}", "wikicities",
			"http://www.wikicities.com/index.php/${title}", "pl", "http://pl.wikipedia.org/wiki/${title}", "pi",
			"http://pi.wikipedia.org/wiki/${title}", "wiktionary", "http://en.wiktionary.org/wiki/${title}", "turismo",
			"http://www.tejo.org/turismo/${title}", "pa", "http://pa.wikipedia.org/wiki/${title}", "terrorwiki",
			"http://www.liberalsagainstterrorism.com/wiki/index.php/${title}", "finalempire",
			"http://final-empire.sourceforge.net/cgi-bin/wiki.pl?${title}", "fr.be", "http://fr.wikinations.be/${title}", "os",
			"http://os.wikipedia.org/wiki/${title}", "or", "http://or.wikipedia.org/wiki/${title}", "netvillage",
			"http://www.netbros.com/?${title}", "seattlewireless", "http://seattlewireless.net/?${title}", "om",
			"http://om.wikipedia.org/wiki/${title}", "pangalacticorg", "http://www.pangalactic.org/Wiki/${title}", "seeds",
			"http://www.IslandSeeds.org/wiki/${title}", "oc", "http://oc.wikipedia.org/wiki/${title}", "raec",
			"http://www.raec.clacso.edu.ar:8080/raec/Members/raecpedia/${title}", "ny", "http://ny.wikipedia.org/wiki/${title}", "nv",
			"http://nv.wikipedia.org/wiki/${title}", "foldoc", "http://www.foldoc.org/foldoc/foldoc.cgi?${title}", "no",
			"http://no.wikipedia.org/wiki/${title}", "nn", "http://nn.wikipedia.org/wiki/${title}", "metawikipedia",
			"http://meta.wikimedia.org/wiki/${title}", "wikif1", "http://www.wikif1.org/${title}", "nl",
			"http://nl.wikipedia.org/wiki/${title}", "ypsieyeball", "http://sknkwrks.dyndns.org:1957/writewiki/wiki.pl?${title}", "ng",
			"http://ng.wikipedia.org/wiki/${title}", "purlnet", "http://purl.oclc.org/NET/${title}", "ne",
			"http://ne.wikipedia.org/wiki/${title}", "nb", "http://nb.wikipedia.org/wiki/${title}", "abbenormal",
			"http://www.ourpla.net/cgi-bin/pikie.cgi?${title}", "na", "http://na.wikipedia.org/wiki/${title}", "docbook",
			"http://docbook.org/wiki/moin.cgi/${title}", "fr.org", "http://fr.wikinations.org/${title}", "my",
			"http://my.wikipedia.org/wiki/${title}", "brasilwiki", "http://rio.ifi.unizh.ch/brasilienwiki/index.php/${title}", "mt",
			"http://mt.wikipedia.org/wiki/${title}", "ms", "http://ms.wikipedia.org/wiki/${title}", "mr",
			"http://mr.wikipedia.org/wiki/${title}", "advogato", "http://www.advogato.org/${title}", "senseislibrary",
			"http://senseis.xmp.net/?${title}", "mo", "http://mo.wikipedia.org/wiki/${title}", "mn",
			"http://mn.wikipedia.org/wiki/${title}", "lutherwiki", "http://www.lutheranarchives.com/mw/index.php/${title}", "ml",
			"http://ml.wikipedia.org/wiki/${title}", "mk", "http://mk.wikipedia.org/wiki/${title}", "mi",
			"http://mi.wikipedia.org/wiki/${title}", "jspwiki", "http://www.ecyrd.com/JSPWiki/Wiki.jsp?page=${title}", "mh",
			"http://mh.wikipedia.org/wiki/${title}", "mg", "http://mg.wikipedia.org/wiki/${title}", "metaweb",
			"http://www.metaweb.com/wiki/wiki.phtml?title=${title}", "kmwiki", "http://www.voght.com/cgi-bin/pywiki?${title}",
			"efnetxmlwiki", "http://purl.net/wiki/xml/${title}", "tejo", "http://www.tejo.org/vikio/${title}", "zwiki",
			"http://www.zwiki.org/${title}", "lv", "http://lv.wikipedia.org/wiki/${title}", "lt",
			"http://lt.wikipedia.org/wiki/${title}", "lo", "http://lo.wikipedia.org/wiki/${title}", "foxwiki",
			"http://fox.wikis.com/wc.dll?Wiki~${title}", "ln", "http://ln.wikipedia.org/wiki/${title}", "emacswiki",
			"http://www.emacswiki.org/cgi-bin/wiki.pl?${title}", "li", "http://li.wikipedia.org/wiki/${title}", "bemi",
			"http://bemi.free.fr/vikio/index.php?${title}", "lg", "http://lg.wikipedia.org/wiki/${title}", "wikibooks",
			"http://en.wikibooks.org/wiki/${title}", "lb", "http://lb.wikipedia.org/wiki/${title}", "la",
			"http://la.wikipedia.org/wiki/${title}", "creationmatters", "http://www.ourpla.net/cgi-bin/wiki.pl?${title}", "ky",
			"http://ky.wikipedia.org/wiki/${title}", "kw", "http://kw.wikipedia.org/wiki/${title}", "kv",
			"http://kv.wikipedia.org/wiki/${title}", "pikie", "http://pikie.darktech.org/cgi/pikie?${title}", "evowiki",
			"http://www.evowiki.org/index.php/${title}", "ku", "http://ku.wikipedia.org/wiki/${title}", "ks",
			"http://ks.wikipedia.org/wiki/${title}", "kr", "http://kr.wikipedia.org/wiki/${title}", "haribeau",
			"http://wiki.haribeau.de/cgi-bin/wiki.pl?${title}", "ko", "http://ko.wikipedia.org/wiki/${title}", "kn",
			"http://kn.wikipedia.org/wiki/${title}", "km", "http://km.wikipedia.org/wiki/${title}", "kl",
			"http://kl.wikipedia.org/wiki/${title}", "kk", "http://kk.wikipedia.org/wiki/${title}", "kj",
			"http://kj.wikipedia.org/wiki/${title}", "ki", "http://ki.wikipedia.org/wiki/${title}", "why",
			"http://clublet.com/c/c/why?${title}", "kg", "http://kg.wikipedia.org/wiki/${title}", "ka",
			"http://ka.wikipedia.org/wiki/${title}", "mus", "http://mus.wikipedia.org/wiki/${title}", "hrwiki",
			"http://www.hrwiki.org/index.php/${title}", "orgpatterns",
			"http://www.bell-labs.com/cgi-user/OrgPatterns/OrgPatterns?${title}", "jv", "http://jv.wikipedia.org/wiki/${title}",
			"gotamac", "http://www.got-a-mac.org/${title}", "dolphinwiki", "http://www.object-arts.com/wiki/html/Dolphin/${title}",
			"zh-cn", "http://zh.wikipedia.org/wiki/${title}", "visualworks", "http://wiki.cs.uiuc.edu/VisualWorks/${title}", "iawiki",
			"http://www.IAwiki.net/${title}", "freebsdman", "http://www.FreeBSD.org/cgi/man.cgi?apropos=1&query=${title}", "ja",
			"http://ja.wikipedia.org/wiki/${title}", "chy", "http://chy.wikipedia.org/wiki/${title}", "unreal",
			"http://wiki.beyondunreal.com/wiki/${title}", "iu", "http://iu.wikipedia.org/wiki/${title}", "it",
			"http://it.wikipedia.org/wiki/${title}", "is", "http://is.wikipedia.org/wiki/${title}", "chr",
			"http://chr.wikipedia.org/wiki/${title}", "usemod", "http://www.usemod.com/cgi-bin/wiki.pl?${title}", "cmwiki",
			"http://www.ourpla.net/cgi-bin/wiki.pl?${title}", "hammondwiki", "http://www.dairiki.org/HammondWiki/index.php3?${title}",
			"cho", "http://cho.wikipedia.org/wiki/${title}", "io", "http://io.wikipedia.org/wiki/${title}", "personaltelco",
			"http://www.personaltelco.net/index.cgi/${title}", "ik", "http://ik.wikipedia.org/wiki/${title}", "haw",
			"http://haw.wikipedia.org/wiki/${title}", "ii", "http://ii.wikipedia.org/wiki/${title}", "wikisource",
			"http://sources.wikipedia.org/wiki/${title}", "lugkr", "http://lug-kr.sourceforge.net/cgi-bin/lugwiki.pl?${title}", "ig",
			"http://ig.wikipedia.org/wiki/${title}", "zh-cfr", "http://zh-min-nan.wikipedia.org/wiki/${title}", "ie",
			"http://ie.wikipedia.org/wiki/${title}", "id", "http://id.wikipedia.org/wiki/${title}", "ia",
			"http://ia.wikipedia.org/wiki/${title}", "openwiki", "http://openwiki.com/?${title}", "hz",
			"http://hz.wikipedia.org/wiki/${title}", "hy", "http://hy.wikipedia.org/wiki/${title}", "strikiwiki",
			"http://ch.twi.tudelft.nl/~mostert/striki/teststriki.pl?${title}", "hu", "http://hu.wikipedia.org/wiki/${title}",
			"herzkinderwiki", "http://www.herzkinderinfo.de/Mediawiki/index.php/${title}", "ht", "http://ht.wikipedia.org/wiki/${title}",
			"hr", "http://hr.wikipedia.org/wiki/${title}", "webisodes", "http://www.webisodes.org/${title}", "globalvoices",
			"http://cyber.law.harvard.edu/dyn/globalvoices/wiki/${title}", "ho", "http://ho.wikipedia.org/wiki/${title}", "hi",
			"http://hi.wikipedia.org/wiki/${title}", "elibre", "http://enciclopedia.us.es/index.php/${title}", "alife",
			"http://news.alife.org/wiki/index.php?${title}", "he", "http://he.wikipedia.org/wiki/${title}", "ast",
			"http://ast.wikipedia.org/wiki/${title}", "ha", "http://ha.wikipedia.org/wiki/${title}", "revo",
			"http://purl.org/NET/voko/revo/art/${title}.html", "arxiv", "http://www.arxiv.org/abs/${title}", "sockwiki",
			"http://wiki.socklabs.com/${title}", "gv", "http://gv.wikipedia.org/wiki/${title}", "gu",
			"http://gu.wikipedia.org/wiki/${title}", "gn", "http://gn.wikipedia.org/wiki/${title}", "gl",
			"http://gl.wikipedia.org/wiki/${title}", "seapig", "http://www.seapig.org/${title}", "gd",
			"http://gd.wikipedia.org/wiki/${title}", "ga", "http://ga.wikipedia.org/wiki/${title}", "opera7wiki",
			"http://nontroppo.org/wiki/${title}", "oeis",
			"http://www.research.att.com/cgi-bin/access.cgi/as/njas/sequences/eisA.cgi?Anum=${title}", "moinmoin",
			"http://purl.net/wiki/moin/${title}", "fy", "http://fy.wikipedia.org/wiki/${title}", "gej",
			"http://www.esperanto.de/cgi-bin/aktivikio/wiki.pl?${title}", "fr", "http://fr.wikipedia.org/wiki/${title}", "arc",
			"http://arc.wikipedia.org/wiki/${title}", "fo", "http://fo.wikipedia.org/wiki/${title}", "fj",
			"http://fj.wikipedia.org/wiki/${title}", "wikinews", "http://en.wikinews.org/wiki/${title}", "fi",
			"http://fi.wikipedia.org/wiki/${title}", "ff", "http://ff.wikipedia.org/wiki/${title}", "annotationwiki",
			"http://www.seedwiki.com/page.cfm?wikiid=368&doc=${title}", "sep11", "http://sep11.wikipedia.org/wiki/${title}", "wlug",
			"http://www.wlug.org.nz/${title}", "fa", "http://fa.wikipedia.org/wiki/${title}", "eu",
			"http://eu.wikipedia.org/wiki/${title}", "tmbw", "http://www.tmbw.net/wiki/index.php/${title}", "et",
			"http://et.wikipedia.org/wiki/${title}", "scn", "http://scn.wikipedia.org/wiki/${title}", "es",
			"http://es.wikipedia.org/wiki/${title}", "muweb", "http://www.dunstable.com/scripts/MuWebWeb?${title}", "eo",
			"http://eo.wikipedia.org/wiki/${title}", "en", "http://en.wikipedia.org/wiki/${title}", "dejanews",
			"http://www.deja.com/=dnc/getdoc.xp?AN=${title}", "el", "http://el.wikipedia.org/wiki/${title}", "jargonfile",
			"http://sunir.org/apps/meta.pl?wiki=JargonFile&redirect=${title}", "eokulturcentro",
			"http://esperanto.toulouse.free.fr/wakka.php?wiki=${title}", "ee", "http://ee.wikipedia.org/wiki/${title}", "tum",
			"http://tum.wikipedia.org/wiki/${title}", "plog4u_de", "http://plog4u.de/index.php/${title}", "dz",
			"http://dz.wikipedia.org/wiki/${title}", "dv", "http://dv.wikipedia.org/wiki/${title}", "kerimwiki",
			"http://wiki.oxus.net/${title}", "dk", "http://da.wikipedia.org/wiki/${title}", "de",
			"http://de.wikipedia.org/wiki/${title}", "dwjwiki", "http://www.suberic.net/cgi-bin/dwj/wiki.cgi?${title}", "da",
			"http://da.wikipedia.org/wiki/${title}", "wlwiki", "http://winslowslair.supremepixels.net/wiki/index.php/${title}", "cy",
			"http://cy.wikipedia.org/wiki/${title}", "w", "http://en.wikipedia.org/wiki/${title}", "cv",
			"http://cv.wikipedia.org/wiki/${title}", "cs", "http://cs.wikipedia.org/wiki/${title}", "cr",
			"http://cr.wikipedia.org/wiki/${title}", "q", "http://en.wikiquote.org/wiki/${title}", "co",
			"http://co.wikipedia.org/wiki/${title}", "zh-min-nan", "http://zh-min-nan.wikipedia.org/wiki/${title}", "n",
			"http://en.wikinews.org/wiki/${title} ", "m", "http://meta.wikimedia.org/wiki/${title}", "annotation",
			"http://bayle.stanford.edu/crit/nph-med.cgi/${title}", "ch", "http://ch.wikipedia.org/wiki/${title}", "efnetcppwiki",
			"http://purl.net/wiki/cpp/${title}", "ce", "http://ce.wikipedia.org/wiki/${title}", "c2find",
			"http://c2.com/cgi/wiki?FindPage&value=${title} ", "b", "http://en.wikibooks.org/wiki/${title}", "ca",
			"http://ca.wikipedia.org/wiki/${title}", "dictionary",
			"http://www.dict.org/bin/Dict?Database=*&Form=Dict1&Strategy=*&Query=${title}", "ang",
			"http://ang.wikipedia.org/wiki/${title}", "zh-tw", "http://zh.wikipedia.org/wiki/${title}", "bs",
			"http://bs.wikipedia.org/wiki/${title}", "br", "http://br.wikipedia.org/wiki/${title}", "twiki",
			"http://twiki.org/cgi-bin/view/${title}", "bo", "http://bo.wikipedia.org/wiki/${title}", "wikt",
			"http://en.wiktionary.org/wiki/${title}", "bn", "http://bn.wikipedia.org/wiki/${title}", "bm",
			"http://bm.wikipedia.org/wiki/${title}", "bi", "http://bi.wikipedia.org/wiki/${title}", "bh",
			"http://bh.wikipedia.org/wiki/${title}", "bg", "http://bg.wikipedia.org/wiki/${title}", "knowhow",
			"http://www2.iro.umontreal.ca/~paquetse/cgi-bin/wiki.cgi?${title}", "be", "http://be.wikipedia.org/wiki/${title}", "wiki",
			"http://c2.com/cgi/wiki?${title}", "patwiki", "http://gauss.ffii.org/${title}", "ba",
			"http://ba.wikipedia.org/wiki/${title}", "rfc", "http://www.rfc-editor.org/rfc/rfc${title}.txt", "zu",
			"http://zu.wikipedia.org/wiki/${title}", "lanifexwiki", "http://opt.lanifex.com/cgi-bin/wiki.pl?${title}", "twistedwiki",
			"http://purl.net/wiki/twisted/${title}", "az", "http://az.wikipedia.org/wiki/${title}", "ay",
			"http://ay.wikipedia.org/wiki/${title}", "commons", "http://commons.wikimedia.org/wiki/${title}", "acronym",
			"http://www.acronymfinder.com/af-query.asp?String=exact&Acronym=${title}", "av", "http://av.wikipedia.org/wiki/${title}",
			"aspienetwiki", "http://aspie.mela.de/Wiki/index.php?title=${title}", "as", "http://as.wikipedia.org/wiki/${title}",
			"metawiki", "http://sunir.org/apps/meta.pl?${title}", "ar", "http://ar.wikipedia.org/wiki/${title}", "zh",
			"http://zh.wikipedia.org/wiki/${title}", "pywiki", "http://www.voght.com/cgi-bin/pywiki?${title}", "an",
			"http://an.wikipedia.org/wiki/${title}", "am", "http://am.wikipedia.org/wiki/${title}", "ak",
			"http://ak.wikipedia.org/wiki/${title}", "infosecpedia", "http://www.infosecpedia.org/pedia/index.php/${title}", "za",
			"http://za.wikipedia.org/wiki/${title}", "af", "http://af.wikipedia.org/wiki/${title}", "firstwiki",
			"http://firstwiki.org/index.php/${title}", "als", "http://als.wikipedia.org/wiki/${title}", "ab",
			"http://ab.wikipedia.org/wiki/${title}", "aa", "http://aa.wikipedia.org/wiki/${title}", "ursine",
			"http://ursine.ca/${title}", "meatball", "http://www.usemod.com/cgi-bin/mb.pl?${title}", "mozillawiki",
			"http://wiki.mozilla.org/index.php/${title}", "imdb", "http://us.imdb.com/Title?${title}", "pythoninfo",
			"http://www.python.org/cgi-bin/moinmoin/${title}", "yo", "http://yo.wikipedia.org/wiki/${title}", "seattlewiki",
			"http://seattlewiki.org/wiki/${title}", "yi", "http://yi.wikipedia.org/wiki/${title}", "vls",
			"http://vls.wikipedia.org/wiki/${title}", "meta", "http://meta.wikimedia.org/wiki/${title}", "susning",
			"http://www.susning.nu/${title}", "nds", "http://nds.wikipedia.org/wiki/${title}", "wikitravel",
			"http://wikitravel.org/en/${title}", "codersbase", "http://www.codersbase.com/${title}", "tpi",
			"http://tpi.wikipedia.org/wiki/${title}", "ppr", "http://c2.com/cgi/wiki?${title}" };

	/**
	 * Map from the interwiki shortcut to the real Interwiki-URL
	 */
	protected static final HashMap<String, String> INTERWIKI_MAP = new HashMap<String, String>();

	/**
	 * Map the HTML token string to the correspoding TagToken implementation
	 */
	protected static final HashMap<String, TagToken> TAG_TOKEN_MAP = new HashMap<String, TagToken>();

	/**
	 * Map the source code's language string to the code formatter implementation
	 */
	protected static final HashMap<String, SourceCodeFormatter> CODE_FORMATTER_MAP = new HashMap<String, SourceCodeFormatter>();;

	/**
	 * Map the template's function name to the TemplateFunction implementation
	 */
	protected static final HashMap<String, ITemplateFunction> TEMPLATE_FUNCTION_MAP = new HashMap<String, ITemplateFunction>();;

	/**
	 * Allowed URI schemes
	 */
	protected static final HashSet<String> URI_SCHEME_MAP = new HashSet<String>();

	public final static Pattern NOWIKI_OPEN_PATTERN = Pattern.compile("\\<nowiki\\>", Pattern.CASE_INSENSITIVE);

	public final static Pattern NOWIKI_CLOSE_PATTERN = Pattern.compile("\\<\\/nowiki\\>", Pattern.CASE_INSENSITIVE);

	static {
		URI_SCHEME_MAP.add("http");
		URI_SCHEME_MAP.add("https");
		URI_SCHEME_MAP.add("ftp");

		for (int i = 0; i < INTERWIKI_STRINGS.length; i += 2) {
			INTERWIKI_MAP.put(INTERWIKI_STRINGS[i], INTERWIKI_STRINGS[i + 1]);
		}
		TEMPLATE_FUNCTION_MAP.put("anchorencode", Anchorencode.CONST);
		TEMPLATE_FUNCTION_MAP.put("fullurl", Fullurl.CONST);
		TEMPLATE_FUNCTION_MAP.put("ns", NS.CONST);
		TEMPLATE_FUNCTION_MAP.put("urlencode", URLEncode.CONST);
		TEMPLATE_FUNCTION_MAP.put("lc", LC.CONST);
		TEMPLATE_FUNCTION_MAP.put("uc", UC.CONST);
		TEMPLATE_FUNCTION_MAP.put("localurl", Localurl.CONST);
		TEMPLATE_FUNCTION_MAP.put("lcfirst", LCFirst.CONST);
		TEMPLATE_FUNCTION_MAP.put("ucfirst", UCFirst.CONST);
		TEMPLATE_FUNCTION_MAP.put("padleft", Padleft.CONST);
		TEMPLATE_FUNCTION_MAP.put("padright", Padright.CONST);
		TEMPLATE_FUNCTION_MAP.put("#expr", Expr.CONST);
		TEMPLATE_FUNCTION_MAP.put("#if", If.CONST);
		TEMPLATE_FUNCTION_MAP.put("#ifeq", Ifeq.CONST);
		TEMPLATE_FUNCTION_MAP.put("#ifexist", Ifexist.CONST);
		TEMPLATE_FUNCTION_MAP.put("#ifexpr", Ifexpr.CONST);
		TEMPLATE_FUNCTION_MAP.put("#switch", Switch.CONST);
		TEMPLATE_FUNCTION_MAP.put("#tag", Tag.CONST);
		TEMPLATE_FUNCTION_MAP.put("#time", Time.CONST);

		CODE_FORMATTER_MAP.put("abap", new ABAPCodeFilter());
		CODE_FORMATTER_MAP.put("csharp", new CSharpCodeFilter());
		CODE_FORMATTER_MAP.put("groovy", new GroovyCodeFilter());
		CODE_FORMATTER_MAP.put("java", new JavaCodeFilter());
		CODE_FORMATTER_MAP.put("javascript", new JavaScriptCodeFilter());
		CODE_FORMATTER_MAP.put("php", new PHPCodeFilter());
		CODE_FORMATTER_MAP.put("html4strict", new XMLCodeFilter());
		CODE_FORMATTER_MAP.put("xml", new XMLCodeFilter());

		TAG_TOKEN_MAP.put("br", new BrTag());
		TAG_TOKEN_MAP.put("hr", new HrTag());

		TAG_TOKEN_MAP.put("nowiki", new NowikiTag());
		TAG_TOKEN_MAP.put("pre", HTML_PRE_OPEN);// new PreTag());
		TAG_TOKEN_MAP.put("math", new MathTag());
		// TAG_TOKEN_MAP.put("embed", new EmbedTag());
		TAG_TOKEN_MAP.put("ref", new RefTag());
		TAG_TOKEN_MAP.put("references", new ReferencesTag());

		TAG_TOKEN_MAP.put("source", new SourceTag());

		TAG_TOKEN_MAP.put("a", HTML_A_OPEN);
		TAG_TOKEN_MAP.put("h1", HTML_H1_OPEN);
		TAG_TOKEN_MAP.put("h2", HTML_H2_OPEN);
		TAG_TOKEN_MAP.put("h3", HTML_H3_OPEN);
		TAG_TOKEN_MAP.put("h4", HTML_H4_OPEN);
		TAG_TOKEN_MAP.put("h5", HTML_H5_OPEN);
		TAG_TOKEN_MAP.put("h6", HTML_H6_OPEN);

		TAG_TOKEN_MAP.put("em", HTML_EM_OPEN);
		TAG_TOKEN_MAP.put("i", HTML_ITALIC_OPEN);
		TAG_TOKEN_MAP.put("b", HTML_BOLD_OPEN);

		TAG_TOKEN_MAP.put("strong", HTML_STRONG_OPEN);
		TAG_TOKEN_MAP.put("u", HTML_UNDERLINE_OPEN);
		TAG_TOKEN_MAP.put("p", HTML_PARAGRAPH_OPEN);

		TAG_TOKEN_MAP.put("blockquote", HTML_BLOCKQUOTE_OPEN);

		TAG_TOKEN_MAP.put("var", HTML_VAR_OPEN);
		TAG_TOKEN_MAP.put("code", HTML_CODE_OPEN);
		TAG_TOKEN_MAP.put("s", HTML_S_OPEN);
		TAG_TOKEN_MAP.put("small", HTML_SMALL_OPEN);
		TAG_TOKEN_MAP.put("big", HTML_BIG_OPEN);
		TAG_TOKEN_MAP.put("del", HTML_DEL_OPEN);

		TAG_TOKEN_MAP.put("sub", HTML_SUB_OPEN);
		TAG_TOKEN_MAP.put("sup", HTML_SUP_OPEN);
		TAG_TOKEN_MAP.put("strike", HTML_STRIKE_OPEN);

		TAG_TOKEN_MAP.put("table", HTML_TABLE_OPEN);
		TAG_TOKEN_MAP.put("th", HTML_TH_OPEN);
		TAG_TOKEN_MAP.put("tr", HTML_TR_OPEN);
		TAG_TOKEN_MAP.put("td", HTML_TD_OPEN);
		TAG_TOKEN_MAP.put("caption", HTML_CAPTION_OPEN);

		TAG_TOKEN_MAP.put("ul", HTML_UL_OPEN);
		TAG_TOKEN_MAP.put("ol", HTML_OL_OPEN);
		TAG_TOKEN_MAP.put("li", HTML_LI_OPEN);

		TAG_TOKEN_MAP.put("font", HTML_FONT_OPEN);
		TAG_TOKEN_MAP.put("center", HTML_CENTER_OPEN);
		TAG_TOKEN_MAP.put("tt", HTML_TT_OPEN);
		TAG_TOKEN_MAP.put("div", HTML_DIV_OPEN);
		TAG_TOKEN_MAP.put("span", HTML_SPAN_OPEN);

		TAG_TOKEN_MAP.put("cite", HTML_CITE_OPEN);
	}

	public final static Configuration DEFAULT_CONFIGURATION = new Configuration();

	/**
	 * Limits the recursive call of the Wikipedia and Template parser to a depth
	 * of PARSER_RECURSION_LIMIT
	 */
	public final static int PARSER_RECURSION_LIMIT = 16;

	/**
	 * Limits the recursive call of the HTMLConverter renderer to a depth of
	 * RENDERER_RECURSION_LIMIT
	 */
	public final static int RENDERER_RECURSION_LIMIT = 256;

	public Configuration() {
	}

	public Map<String, String> getInterwikiMap() {
		return INTERWIKI_MAP;
	}

	public String addInterwikiLink(String key, String value) {
		return INTERWIKI_MAP.put(key, value);
	}

	/**
	 * Get the set of all allowed URI scheme shortcuts like http, https, ftp,...
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
	 * 
	 */
	public Set<String> getUriSchemeSet() {
		return URI_SCHEME_MAP;
	}

	/**
	 * Add an allowed URI scheme shortcut like http, https, ftp,...
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
	 * 
	 * @return <code>true</code> if the set did not already contain the specified
	 *         URI key.
	 */
	public boolean addUriScheme(String uriKey) {
		return URI_SCHEME_MAP.add(uriKey);
	}

	/**
	 * Check if the URI scheme is allowed.
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
	 * 
	 * @return <code>true</code> if the set contains the specified URI key.
	 */
	public boolean containsUriScheme(String uriKey) {
		return URI_SCHEME_MAP.contains(uriKey);
	}

	public Map<String, ITemplateFunction> getTemplateMap() {
		return TEMPLATE_FUNCTION_MAP;
	}

	public ITemplateFunction addTemplateFunction(String key, ITemplateFunction value) {
		return TEMPLATE_FUNCTION_MAP.put(key, value);
	}

	public Map<String, SourceCodeFormatter> getCodeFormatterMap() {
		return CODE_FORMATTER_MAP;
	}

	public SourceCodeFormatter addCodeFormatter(String key, SourceCodeFormatter value) {
		return CODE_FORMATTER_MAP.put(key, value);
	}

	public Map<String, TagToken> getTokenMap() {
		return TAG_TOKEN_MAP;
	}

	public TagToken addTokenTag(String key, TagToken value) {
		return TAG_TOKEN_MAP.put(key, value);
	}

}
