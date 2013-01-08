package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;

import java.util.Locale;
import java.util.Map;

/**
 * Wiki model implementation which allows some special JUnit tests with
 * namespaces and predefined templates
 * 
 */
public class WikiTestModel extends WikiModel {
	public final static String BIRTH_DATE_AND_AGE = "<includeonly>{{#if:{{{df|}}}|{{#expr:{{{3|{{{day}}}}}}}} {{MONTHNAME|{{{2|{{{month}}}}}}}}|{{MONTHNAME|{{{2|{{{month}}}}}}}} {{#expr:{{{3|{{{day}}}}}}}},}} {{{1|{{{year}}}}}}<span style=\"display:none\"> (<span class=\"bday\">{{{1|{{{year}}}}}}-{{padleft:{{{2|{{{month}}}}}}|2|0}}-{{padleft:{{{3|{{{day}}}}}}|2|0}}</span>)</span><span class=\"noprint\"> (age&nbsp;{{age | {{{1|{{{year}}}}}} | {{{2|{{{month}}}}}} | {{{3|{{{day}}}}}} }})</span></includeonly><noinclude>\n"
			+ "{{pp-template|small=yes}}\n" + "{{documentation}}\n" + "</noinclude>";
	public final static String MONTHNAME = "<includeonly>{{#if:{{{1|}}}|{{#switch:{{MONTHNUMBER|{{{1}}}}}|1=January|2=February|3=March|4=April|5=May|6=June|7=July|8=August|9=September|10=October|11=November|12=December|Incorrect required parameter 1=''month''!}}|Missing required parameter 1=''month''!}}</includeonly><noinclude>\n"
			+ "\n"
			+ "{{pp-template|small=yes}}\n"
			+ "{{Documentation}}\n"
			+ "<!-- Add categories and interwikis to the /doc subpage, not here! -->\n" + "</noinclude>";
	public final static String AGE = "<includeonly>{{#expr:({{{4|{{CURRENTYEAR}}}}})-({{{1}}})-(({{{5|{{CURRENTMONTH}}}}})<({{{2}}})or({{{5|{{CURRENTMONTH}}}}})=({{{2}}})and({{{6|{{CURRENTDAY}}}}})<({{{3}}}))}}</includeonly><noinclude>\n"
			+ "{{pp-template|small=yes}}\n" + "{{template doc}}\n" + "</noinclude>";
	public final static String MONTHNUMBER = "<includeonly>{{#if:{{{1|}}}\n" + " |{{#switch:{{lc:{{{1}}}}}\n" + "  |january|jan=1\n"
			+ "  |february|feb=2\n" + "  |march|mar=3\n" + "  |apr|april=4\n" + "  |may=5\n" + "  |june|jun=6\n" + "  |july|jul=7\n"
			+ "  |august|aug=8\n" + "  |september|sep=9\n" + "  |october|oct=10\n" + "  |november|nov=11\n" + "  |december|dec=12\n"
			+ "  |{{#ifexpr:{{{1}}}<0\n" + "   |{{#ifexpr:(({{{1}}})round 0)!=({{{1}}})\n"
			+ "    |{{#expr:12-(((0.5-({{{1}}}))round 0)mod 12)}}\n" + "    |{{#expr:12-(((11.5-({{{1}}}))round 0)mod 12)}}\n"
			+ "   }}\n" + "  |{{#expr:(((10.5+{{{1}}})round 0)mod 12)+1}}\n" + "  }}\n" + " }}\n"
			+ " |Missing required parameter 1=''month''!\n" + "}}</includeonly><noinclude>\n" + "{{pp-template|small=yes}}\n"
			+ "{{Documentation}}\n" + "<!-- Add categories and interwikis to the /doc subpage, not here! -->\n" + "</noinclude>";

	public final static String BORN_DATA = "{{#if:{{{birthname|}}}|{{{birthname|}}}<br />}}{{#if:{{{birth_date|{{{birthdate|}}}}}}|{{{birth_date|{{{birthdate}}}}}}<br />}}{{{location|{{{birth_place|{{{birthplace|}}}}}}}}}";

	public final static String TL = "{{[[Template:{{{1}}}|{{{1}}}]]}}<noinclude>\n" + "{{pp-template|small=yes}}\n"
			+ "{{documentation}}\n" + "</noinclude>";
	public final static String PRON_ENG = "#REDIRECT [[Template:Pron-en]]";
	public final static String PRON_EN = "<onlyinclude>pronounced <span title=\"Pronunciation in the International Phonetic Alphabet (IPA)\" class=\"IPA\">[[WP:IPA for English|/{{{1}}}/]]</span></onlyinclude>\n"
			+ "\n"
			+ "==Example==\n"
			+ "\n"
			+ "This code:\n"
			+ "\n"
			+ ": <code><nowiki>A battleship, {{pron-en|ˈbætəlʃɪp}}, is ...</nowiki></code>\n"
			+ "\n"
			+ "will display:\n"
			+ "\n"
			+ ": A battleship, {{pron-en|ˈbætəlʃɪp}}, is ...\n"
			+ "\n"
			+ "==Usage==\n"
			+ "{{usage of IPA templates}}\n"
			+ "\n"
			+ "<!-- PLEASE ADD CATEGORIES BELOW THIS LINE, THANKS. -->\n"
			+ "\n"
			+ "[[Category:IPA templates|{{PAGENAME}}]]\n"
			+ "\n"
			+ "<!-- PLEASE ADD INTERWIKIS BELOW THIS LINE, THANKS. -->\n"
			+ "<noinclude>\n"
			+ "[[ar:PronEng]]\n"
			+ "[[tl:Template:PronEng]]\n" + "[[simple:Template:IPA-en]]\n" + "</noinclude>";

	public final static String HTML_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n"
			+ "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
			+ "<head>\n" + "        <title>test</title>\n\n    </head>\n" + "    <body>";

	public final static String XHTML_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n"
			+ "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n";

	public final static String XHTML_END = "</body>\n" + "</html>";

	public final static String PIPE_SYMBOL = "|<noinclude>{{template doc}}</noinclude>";
	public final static String DOUBLE_PARAMETER = "{{{1}}}{{{1}}}";
	public final static String REFLIST_TEXT = "<div class=\"references-small\" {{#if: {{{colwidth|}}}| style=\"-moz-column-width:{{{colwidth}}}; -webkit-column-width:{{{colwidth}}}; column-width:{{{colwidth}}};\" | {{#if: {{{1|}}}| style=\"-moz-column-count:{{{1}}}; -webkit-column-count:{{{1}}}; column-count:{{{1}}} }};\" |}}>\n"
			+ "<references /></div><noinclude>{{pp-template|small=yes}}{{template doc}}</noinclude>\n";

	public final static String CAT = "[[:Category:{{{1}}}]]<noinclude>\n"
			+ "{{Interwikitmp-grp ineligible|CAT|Commons|Wiktionary}}\n" + "Creates a link to the specified category.\n" + "\n"
			+ "\'\'\'Example\'\'\':\n" + "<pre>\n" + "{{Cat|stubs}}\n" + "</pre>\n" + "{{Cat|stubs}}\n" + "\n" + "==See also==\n"
			+ "* {{Tl|Cl}}\n" + "* {{Tl|Ccl}}\n" + "* {{Tl|Lcs}}\n" + "\n" + "[[sl:predloga:kat]]\n" + "</noinclude>\n" + "";

	public final static String CITE_WEB_TEXT = "[{{{url}}} {{{title}}}]";

	public final static String NESTED_TEMPLATE_TEST = "test a {{nested}} template";

	public final static String NESTED = "a nested template text";

	public final static String IFEQ_TEST = "{{#ifeq: {{{1}}}|{{{2}}} | {{{1}}} equals {{{2}}} | {{{1}}} is not equal {{{2}}}}}";

	public final static String ENDLESS_RECURSION_TEST = " {{recursion}} ";
	public final static String TNAVBAR_TEXT = "<includeonly>{{#if:{{{nodiv|}}}|<!--then:\n"
			+ "-->&nbsp;<span class=\"noprint plainlinksneverexpand\" style=\"white-space:nowrap; font-weight:normal; font-size:xx-small; {{{fontstyle|}}}; {{#if:{{{fontcolor|}}}|color:{{{fontcolor}}};}} {{{style|}}}\">|<!--else:\n"
			+ "--><div class=\"noprint plainlinksneverexpand\" style=\"background-color:transparent; padding:0; white-space:nowrap; font-weight:normal; font-size:xx-small; {{{fontstyle|}}}; {{#if:{{{fontcolor|}}}|color:{{{fontcolor}}};}} {{{style|}}}\"><!--\n"
			+ "-->}}<!--\n"
			+ "\n"
			+ "-->{{#ifeq:{{{mini|}}}{{{miniv|}}}{{{plain|}}}{{{viewplain|}}}|<!--equals:-->1|<!--then:\n"
			+ "-->|<!--else:\n"
			+ "-->This box:&nbsp;<!--\n"
			+ "-->}}<!--\n"
			+ "\n"
			+ "-->{{#ifeq:{{{miniv|}}}{{{viewplain|}}}|<!--equals:-->1|<!--then:\n"
			+ "-->[[Template:{{{1}}}|<span title=\"View this template\" style=\"{{{fontstyle|}}};{{#if:{{{fontcolor|}}}|color:{{{fontcolor}}};}}\">{{#if:{{{viewplain|}}}|view|v}}</span>]]|<!--else:\n"
			+ "-->[[Template:{{{1}}}|<span title=\"View this template\" style=\"{{{fontstyle|}}};{{#if:{{{fontcolor|}}}|color:{{{fontcolor}}};}}\">{{#if:{{{mini|}}}|v|view}}</span>]]&nbsp;<span style=\"font-size:80%;\">•</span>&nbsp;[[Template talk:{{{1}}}|<span style=\"color:#002bb8;{{{fontstyle|}}};{{#if:{{{fontcolor|}}}|color:{{{fontcolor}}};}}\" title=\"Discussion about this template\">{{#if:{{{mini|}}}|d|talk}}</span>]]&nbsp;<span style=\"font-size:80%;\">•</span>&nbsp;[{{fullurl:{{ns:10}}:{{{1}}}|action=edit}} <span style=\"color:#002bb8;{{{fontstyle|}}};{{#if:{{{fontcolor|}}}|color:{{{fontcolor}}};}}\" title=\"You can edit this template. Please use the preview button before saving.\">{{#if:{{{mini|}}}|e|edit}}</span>]<!--\n"
			+ "-->}}<!--\n" + "\n" + "-->{{#if:{{{nodiv|}}}|<!--then:\n" + "--></span>&nbsp;|<!--else:\n" + "--></div><!--\n"
			+ "-->}}</includeonly><noinclude>\n" + "\n" + "{{pp-template|small=yes}}\n"
			+ "<hr/><center>\'\'\'{{purge}}\'\'\' the Wikipedia cache of this template.<hr/></center><br/>\n" + "{{documentation}} \n"
			+ "<!--Note: Metadata (interwiki links, etc) for this template should be put on [[Template:Tnavbar/doc]]-->\n"
			+ "</noinclude>";
	public final static String NAVBOX_TEXT = "<!--\n"
			+ "\n"
			+ "Please do not edit without discussion first as this is a VERY complex template.\n"
			+ "\n"
			+ "-->{{#switch:{{{border|{{{1|}}}}}}|subgroup|child=</div>|none=|#default=<table class=\"navbox\" cellspacing=\"0\" <!--\n"
			+ " -->style=\"{{{bodystyle|}}};{{{style|}}}\"><tr><td style=\"padding:2px;\">}}<!--\n"
			+ "\n"
			+ "--><table cellspacing=\"0\" class=\"nowraplinks {{#if:{{{title|}}}|{{#switch:{{{state|}}}|plain|off=|<!--\n"
			+ " -->#default=collapsible {{#if:{{{state|}}}|{{{state|}}}|autocollapse}}}}}} {{#switch:{{{border|{{{1|}}}}}}|<!--\n"
			+ " -->subgroup|child|none=navbox-subgroup\" style=\"width:100%;{{{bodystyle|}}};{{{style|}}}|<!--\n"
			+ " -->#default=\" style=\"width:100%;background:transparent;color:inherit}};{{{innerstyle|}}};\"><!--\n"
			+ "\n"
			+ "\n"
			+ "\n"
			+ "---Title and Navbar---\n"
			+ "-->{{#if:{{{title|}}}|<tr>{{#if:{{{titlegroup|}}}|<!--\n"
			+ " --><td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{titlegroupstyle|}}}\">{{{titlegroup|}}}</td><!--\n"
			+ " --><th style=\"border-left:2px solid #fdfdfd;width:100%;|<th style=\"}}{{{basestyle|}}};{{{titlestyle|}}}\" <!--\n"
			+ " -->colspan={{#expr:2{{#if:{{{imageleft|}}}|+1}}{{#if:{{{image|}}}|+1}}{{#if:{{{titlegroup|}}}|-1}}}} <!--\n"
			+ " -->class=\"navbox-title\"><!--\n"
			+ "\n"
			+ "-->{{#if:{{#switch:{{{navbar|}}}|plain|off=1}}<!--\n"
			+ " -->{{#if:{{{name|}}}||{{#switch:{{{border|{{{1|}}}}}}|subgroup|child|none=1}}}}|<!--\n"
			+ " -->{{#ifeq:{{{navbar|}}}|off|{{#ifeq:{{{state|}}}|plain|<div style=\"float:right;width:6em;\">&nbsp;</div>}}|<!--\n"
			+ " -->{{#ifeq:{{{state|}}}|plain||<div style=\"float:left; width:6em;text-align:left;\">&nbsp;</div>}}}}|<!--\n"
			+ " --><div style=\"float:left; width:6em;text-align:left;\"><!--\n"
			+ " -->{{Tnavbar|{{{name}}}|fontstyle={{{basestyle|}}};{{{titlestyle|}}};border:none;|mini=1}}<!--\n"
			+ " --></div>{{#ifeq:{{{state|}}}|plain|<div style=\"float:right;width:6em;\">&nbsp;</div>}}}}<!--\n"
			+ "\n"
			+ " --><span style=\"font-size:{{#switch:{{{border|{{{1|}}}}}}|subgroup|child|none=100|#default=110}}%;\"><!--\n"
			+ " -->{{{title}}}</span></th></tr>}}<!--\n"
			+ "\n"
			+ "\n"
			+ "\n"
			+ "---Above---\n"
			+ "-->{{#if:{{{above|}}}|<!--\n"
			+ " -->{{#if:{{{title|}}}|<tr style=\"height:2px;\"><td></td></tr>}}<!--\n"
			+ " --><tr><td class=\"navbox-abovebelow\" style=\"{{{basestyle|}}};{{{abovestyle|}}}\" <!--\n"
			+ " -->colspan=\"{{#expr:2{{#if:{{{imageleft|}}}|+1}}{{#if:{{{image|}}}|+1}}}}\">{{{above}}}</td></tr>}}<!--\n"
			+ "\n"
			+ "\n"
			+ "\n"
			+ "---Body---\n"
			+ "\n"
			+ "---First group/list and images---\n"
			+ "-->{{#if:{{{list1|}}}|{{#if:{{{title|}}}{{{above|}}}|<tr style=\"height:2px;\"><td></td></tr>}}<tr><!--\n"
			+ "\n"
			+ "-->{{#if:{{{imageleft|}}}|<!--\n"
			+ " --><td style=\"width:0%;padding:0px 2px 0px 0px;{{{imageleftstyle|}}}\" <!--\n"
			+ " -->rowspan={{#expr:1{{#if:{{{list2|}}}|+2}}{{#if:{{{list3|}}}|+2}}{{#if:{{{list4|}}}|+2}}<!--\n"
			+ " -->{{#if:{{{list5|}}}|+2}}{{#if:{{{list6|}}}|+2}}{{#if:{{{list7|}}}|+2}}{{#if:{{{list8|}}}|+2}}<!--\n"
			+ " -->{{#if:{{{list9|}}}|+2}}{{#if:{{{list10|}}}|+2}}{{#if:{{{list11|}}}|+2}}{{#if:{{{list12|}}}|+2}}<!--\n"
			+ " -->{{#if:{{{list13|}}}|+2}}{{#if:{{{list14|}}}|+2}}{{#if:{{{list15|}}}|+2}}{{#if:{{{list16|}}}|+2}}<!--\n"
			+ " -->{{#if:{{{list17|}}}|+2}}{{#if:{{{list18|}}}|+2}}{{#if:{{{list19|}}}|+2}}{{#if:{{{list20|}}}|+2}}}}><!--\n"
			+ " -->{{{imageleft|}}}</td>}}<!--\n"
			+ "\n"
			+ " -->{{#if:{{{group1|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group1style|}}}\"><!--\n"
			+ " -->{{{group1}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list1style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{list1padding|{{{listpadding|0em 0.25em}}}}}}\">}}{{{list1|}}}{{#if:{{{list1|}}}|</div></td><!--\n"
			+ "\n"
			+ "-->{{#if:{{{image|}}}|<!--\n"
			+ " --><td style=\"width:0%;padding:0px 0px 0px 2px;{{{imagestyle|}}}\" <!--\n"
			+ " -->rowspan={{#expr:1{{#if:{{{list2|}}}|+2}}{{#if:{{{list3|}}}|+2}}{{#if:{{{list4|}}}|+2}}<!--\n"
			+ " -->{{#if:{{{list5|}}}|+2}}{{#if:{{{list6|}}}|+2}}{{#if:{{{list7|}}}|+2}}{{#if:{{{list8|}}}|+2}}<!--\n"
			+ " -->{{#if:{{{list9|}}}|+2}}{{#if:{{{list10|}}}|+2}}{{#if:{{{list11|}}}|+2}}{{#if:{{{list12|}}}|+2}}<!--\n"
			+ " -->{{#if:{{{list13|}}}|+2}}{{#if:{{{list14|}}}|+2}}{{#if:{{{list15|}}}|+2}}{{#if:{{{list16|}}}|+2}}<!--\n"
			+ " -->{{#if:{{{list17|}}}|+2}}{{#if:{{{list18|}}}|+2}}{{#if:{{{list19|}}}|+2}}{{#if:{{{list20|}}}|+2}}}}><!--\n"
			+ " -->{{{image|}}}</td>}}<!--\n"
			+ "\n"
			+ "--></tr>}}<!--\n"
			+ "\n"
			+ "\n"
			+ "\n"
			+ "---Remaining groups/lists---\n"
			+ "\n"
			+ "-->{{#if:{{{list2|}}}|<!--\n"
			+ " -->{{#if:{{{title|}}}{{{above|}}}{{{list1|}}}|<tr style=\"height:2px\"><td></td></tr>}}<tr><!--\n"
			+ " -->{{#if:{{{group2|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group2style|}}}\"><!--\n"
			+ " -->{{{group2}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list2style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">}}{{{list2|}}}{{#if:{{{list2|}}}|</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list3|}}}|<!--\n"
			+ " -->{{#if:{{{title|}}}{{{above|}}}{{{list1|}}}{{{list2|}}}|<tr style=\"height:2px\"><td></td></tr>}}<tr><!--\n"
			+ " -->{{#if:{{{group3|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group3style|}}}\"><!--\n"
			+ " -->{{{group3}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list3style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">}}{{{list3|}}}{{#if:{{{list3|}}}|</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list4|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group4|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group4style|}}}\"><!--\n"
			+ " -->{{{group4}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list4style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">}}{{{list4|}}}{{#if:{{{list4|}}}|</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list5|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group5|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group5style|}}}\"><!--\n"
			+ " -->{{{group5}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list5style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">}}{{{list5|}}}{{#if:{{{list5|}}}|</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list6|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group6|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group6style|}}}\"><!--\n"
			+ " -->{{{group6}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list6style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list6|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list7|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group7|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group7style|}}}\"><!--\n"
			+ " -->{{{group7}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list7style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list7|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list8|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group8|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group8style|}}}\"><!--\n"
			+ " -->{{{group8}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list8style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list8|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list9|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group9|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group9style|}}}\"><!--\n"
			+ " -->{{{group9}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list9style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list9|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list10|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group10|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group10style|}}}\"><!--\n"
			+ " -->{{{group10}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list10style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list10|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list11|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group11|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group11style|}}}\"><!--\n"
			+ " -->{{{group11}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list11style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list11|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list12|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group12|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group12style|}}}\"><!--\n"
			+ " -->{{{group12}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list12style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list12|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list13|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group13|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group13style|}}}\"><!--\n"
			+ " -->{{{group13}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list13style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list13|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list14|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group14|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group14style|}}}\"><!--\n"
			+ " -->{{{group14}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list14style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list14|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list15|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group15|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group15style|}}}\"><!--\n"
			+ " -->{{{group15}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list15style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list15|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list16|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group16|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group16style|}}}\"><!--\n"
			+ " -->{{{group16}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list16style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list16|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list17|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group17|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group17style|}}}\"><!--\n"
			+ " -->{{{group17}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list17style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list17|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list18|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group18|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group18style|}}}\"><!--\n"
			+ " -->{{{group18}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list18style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list18|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list19|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group19|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group19style|}}}\"><!--\n"
			+ " -->{{{group19}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{oddstyle|}}};{{{list19style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|even|{{{evenodd|odd}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list19|}}}</div></td></tr>}}<!--\n"
			+ "\n"
			+ "-->{{#if:{{{list20|}}}|<!--\n"
			+ " --><tr style=\"height:2px\"><td></td></tr><tr><!--\n"
			+ " -->{{#if:{{{group20|}}}|<td class=\"navbox-group\" style=\"{{{basestyle|}}};{{{groupstyle|}}};{{{group20style|}}}\"><!--\n"
			+ " -->{{{group20}}}</td><td style=\"text-align:left;border-left:2px solid #fdfdfd;|<td colspan=2 style=\"}}<!--\n"
			+ " -->width:100%;padding:0px;{{{liststyle|}}};{{{evenstyle|}}};{{{list20style|}}}\" <!--\n"
			+ " -->class=\"navbox-list navbox-{{#ifeq:{{{evenodd|}}}|swap|odd|{{{evenodd|even}}}}}\"><!--\n"
			+ " --><div style=\"padding:{{{listpadding|0em 0.25em}}}\">{{{list20|}}}</div></td></tr>}}<!--\n" + "\n" + "\n"
			+ "---Below---\n" + "-->{{#if:{{{below|}}}|<!--\n"
			+ " -->{{#if:{{{title|}}}{{{above|}}}{{{list1|}}}{{{list2|}}}{{{list3|}}}|<tr style=\"height:2px;\"><td></td></tr>}}<!--\n"
			+ " --><tr><td class=\"navbox-abovebelow\" style=\"{{{basestyle|}}};{{{belowstyle|}}}\" <!--\n"
			+ " -->colspan=\"{{#expr:2{{#if:{{{imageleft|}}}|+1}}{{#if:{{{image|}}}|+1}}}}\">{{{below}}}</td></tr>}}<!--\n" + "\n" + "\n"
			+ "--></table>{{#switch:{{{border|{{{1|}}}}}}|subgroup|child=<div>|none=|#default=</td></tr></table>}}<!--\n" + "\n"
			+ "--><noinclude>\n" + "\n" + "{{pp-template|small=yes}}\n" + "\n" + "{{documentation}}\n"
			+ "<!-- Add categories and interwikis to the /doc subpage, not here! -->\n" + "</noinclude>";

	public final static String INFOBOX_SOFTWARE_TEXT = "<includeonly>{| class=\"infobox\" cellspacing=\"5\" style=\"width: 21em; font-size: 90%; text-align: left;\"\n"
			+ "! colspan=\"2\" style=\"text-align: center; font-size: 130%;\" | {{{title|{{{name|{{PAGENAME}}}}}}}}\n"
			+ "|-\n"
			+ "{{#if:{{{logo|}}}|\n"
			+ "{{!}} colspan=\"2\" style=\"text-align: center;\" {{!}} {{{logo|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{collapsible|}}}|\n"
			+ "{{!}} colspan=\"2\" {{!}}\n"
			+ "{{hidden|Screenshot|{{{screenshot}}}{{#if:{{{caption|}}}|<br />{{{caption|}}}}}|bg1=#ccccff|ta2=center}}\n"
			+ "{{!}}-\n"
			+ "|\n"
			+ "{{#if:{{{screenshot|}}}|\n"
			+ "{{!}} colspan=\"2\" style=\"text-align: center;\" {{!}} {{{screenshot|}}}{{#if:{{{caption|}}}|<br />{{{caption|}}}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "}}\n"
			+ "{{#if:{{{author|}}}|\n"
			+ "! [[Software design|Design by]]\n"
			+ "{{!}} {{{author|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{developer|}}}|\n"
			+ "! [[Software developer|Developed by]]\n"
			+ "{{!}} {{{developer|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{released|}}}|\n"
			+ "! Initial release\n"
			+ "{{!}} {{{released|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{frequently updated|{{{frequently_updated|}}}}}}|<!-- then -->\n"
			+ "{{#ifexist:Template:Latest stable release/{{{name|{{PAGENAME}}}}}|\n"
			+ "! [[Software release|Stable release]]\n"
			+ "{{!}} {{Latest stable release/{{{name|{{PAGENAME}}}}}}} <sub class=\"plainlinks\">[[{{SERVER}}{{localurl:Template:Latest_stable_release/{{{name|{{PAGENAME}}}}}|action=edit&preload=Template:LSR/syntax}} +/−]]</sub>\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#ifexist:Template:Latest preview release/{{{name|{{PAGENAME}}}}}|\n"
			+ "! [[Software release|Preview release]]\n"
			+ "{{!}} {{Latest preview release/{{{name|{{PAGENAME}}}}}}} <sub class=\"plainlinks\">[[{{SERVER}}{{localurl:Template:Latest_preview_release/{{{name|{{PAGENAME}}}}}|action=edit&preload=Template:LPR/syntax}} +/−]]</sub>\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "|<!-- else -->\n"
			+ "{{#if:{{{latest release version|{{{latest_release_version|}}}}}}|\n"
			+ "! [[Software release life cycle|Latest release]]\n"
			+ "{{!}} {{{latest release version|{{{latest_release_version|}}}}}} {{#if:{{{latest release date|{{{latest_release_date|}}}}}}| / {{{latest release date|{{{latest_release_date|}}}}}}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{latest preview version|{{{latest_preview_version|}}}}}}|\n"
			+ "! [[Software release life cycle|Preview release]]\n"
			+ "{{!}} {{{latest preview version|{{{latest_preview_version|}}}}}} {{#if:{{{latest preview date|{{{latest_preview_date|}}}}}}| / {{{latest preview date|{{{latest_preview_date|}}}}}}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "}}\n"
			+ "{{#if:{{{programming language|{{{programming_language|}}}}}}|\n"
			+ "! [[Programming language|Written in]]\n"
			+ "{{!}} {{{programming language|{{{programming_language|}}}}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{operating system|{{{operating_system|}}}}}}|\n"
			+ "! [[Operating system|OS]]\n"
			+ "{{!}} {{{operating system|{{{operating_system}}}}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{platform|}}}|\n"
			+ "! [[Platform (computing)|Platform]]\n"
			+ "{{!}} {{{platform|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{size|}}}|\n"
			+ "! [[File size|Size]]\n"
			+ "{{!}} {{{size|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{language|}}}|\n"
			+ "! [[Language|Available in]]\n"
			+ "{{!}} {{{language|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{status|}}}|\n"
			+ "! Development status\n"
			+ "{{!}} {{{status|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{genre|}}}|\n"
			+ "! [[List of software categories|Genre]]\n"
			+ "{{!}} {{{genre|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{license|}}}|\n"
			+ "! [[Software license|License]]\n"
			+ "{{!}} {{{license|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{licence|}}}|\n"
			+ "! [[Software license|Licence]]\n"
			+ "{{!}} {{{licence|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{website|}}}|\n"
			+ "! [[Website]]\n"
			+ "{{!}} {{{website|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{standard|}}}|\n"
			+ "! [[Standard]](s)\n"
			+ "{{!}} {{{standard|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "{{#if:{{{AsOf|}}}|\n"
			+ "! As of\n"
			+ "{{!}} {{{AsOf|}}}\n"
			+ "{{!}}-\n"
			+ "}}\n"
			+ "|}</includeonly><noinclude>\n"
			+ "{{pp-template|small=yes}}\n"
			+ "{{documentation}}\n"
			+ "<!-- Add cats and interwikis to the /doc subpage, not here! -->\n" + "</noinclude>";

	final static String FURTHER = "<includeonly>:<span class=\"boilerplate further\"\n"
			+ ">''{{{altphrase|Further information}}}: {{#if:{{{1|}}} |<!--then:-->{{{1}}} |<!--\n"
			+ "else:-->'''Error: [[Template:Further|Template must be given at least one article name]]''' \n"
			+ "}}{{#if:{{{2|}}}|{{#if:{{{3|}}}|, |&#32;and }}  {{{2}}}\n" + "}}{{#if:{{{3|}}}|{{#if:{{{4|}}}|, |, and }} {{{3}}}\n"
			+ "}}{{#if:{{{4|}}}|{{#if:{{{5|}}}|, |, and }} {{{4}}}\n" + "}}{{#if:{{{5|}}}|, and {{{5}}}\n"
			+ "}}{{#if:{{{6|}}}| — '''<br/>Error: [[Template:Futher|Too many links specified (maximum is 5)]]'''\n"
			+ "}}''</span></includeonly><!-- includeonly block is needed, as otherwise the bare template gives error message \n"
			+ "\"Error: Template must be given at least one article name\"\n" + " ---><noinclude>\n" + "{{template doc}}</noinclude>";

	public final static String ANARCHISM_SIDEBAR = "{{Ideology\n"
			+ "|ideology = Anarchism \n"
			+ "|image    = [[File:Anarchy-symbol.svg|125px|\"Circle-A\" anarchy symbol]]\n"
			+ "\n"
			+ "|list1name  = Schools\n"
			+ "|list1title = [[Anarchist schools of thought|Schools]]\n"
			+ "|list1 = [[Agorism]]{{·}} [[Buddhist anarchism|Buddhist]]{{·}} [[Anarcho-capitalism|Capitalist]]<br/> [[Christian anarchism|Christian]]{{·}} [[Collectivist anarchism|Collectivist]]{{·}} [[Anarchist communism|Communist]]<br/> [[Crypto-anarchism]]{{·}} [[Anarcha-feminism|Feminist]]<br/> [[Free-market anarchism|Free market]]{{·}} [[Green anarchism|Green]]{{·}} [[Individualist anarchism|Individualist]]<br/> [[Infoanarchism]]{{·}} [[Insurrectionary anarchism|Insurrectionary]]<br/> [[Left anarchism|Leftist]]{{·}} [[Mutualism (economic theory)|Mutualist]]{{·}} [[Anarcho-pacifism|Pacifist]]<br/> [[Panarchism|Pananarchist]]{{·}} [[Philosophical anarchism|Philosophical]]<br/> [[Platformism|Platformist]]{{·}} [[Post-anarchism|Post-anarchist]]<br/> [[Post-colonial anarchism|Post-colonial]] {{·}}[[Post-left anarchy|Post-left]]<br/> [[Anarcho-primitivism|Primitivist]]{{·}} [[Social anarchism|Social]]{{·}} [[Anarcho-syndicalism|Syndicalist]]<br/>  [[Veganarchism|Vegan]]{{·}}[[Anarchism without adjectives|Without adjectives]]{{·}} [[Zenarchy|Zen]]\n"
			+ "\n"
			+ "|list2name  = Theory/Practice\n"
			+ "|list2title = Theory{{·}}Practice\n"
			+ "|list2 = [[Anarchy]]{{·}} [[Black bloc]]<br/> [[Class struggle]] {{·}} [[Commune (socialism)|Communes]]<br/> [[Consensus democracy]]<br/> [[Decentralization]]{{·}} [[Deep ecology]]<br/> [[Direct action]]{{·}} [[Direct democracy]]<br/> [[Dual power]]{{·}} [[Especifismo]]<br/> [[Horizontalidad]]{{·}} [[Illegalism]]<br/> [[Individual reclamation]]{{·}} [[Anarchist law|Law]]<br/> [[Participatory politics]]<br/> [[Permanent Autonomous Zone]]<br/> [[Prefigurative politics]]<br/> [[Private defense agency]]<br/> [[Propaganda of the deed]]<br/> [[Refusal of work]]{{·}} [[Rewilding (anarchism)|Rewilding]]<br/> [[Social ecology]]<br/> [[Spontaneous order]]\n"
			+ "\n"
			+ "|list3name  = Issues\n"
			+ "|list3title = [[Issues in anarchism|Issues]]\n"
			+ "|list3 = [[Anarchism and anarcho-capitalism|Anarcho-capitalism]]{{·}} [[Anarchism and animal rights|Animal rights]]<br/> [[Anarchism and capitalism|Capitalism]]{{·}} [[Criticisms of anarchism|Criticisms]]{{·}} [[Anarchism and Islam|Islam]]<br/> [[Lifestyle anarchism|Lifestylism]]{{·}} [[Anarchism and Marxism|Marxism]]{{·}} [[Anarchism and nationalism|Nationalism]]<br/> [[Anarchism and Orthodox Judaism|Orthodox Judaism]]{{·}} [[Anarchism and religion|Religion]]<br/> [[Anarchism and violence|Violence]]\n"
			+ "\n"
			+ "|list4name  = History\n"
			+ "|list4title = [[History of anarchism|History]]\n"
			+ "|list4 =  [[WTO Ministerial Conference of 1999 protest activity|1999 WTO Conference protest]]<br/>[[Amakasu Incident]]<br/> [[Anarchist Catalonia]]<br/> [[Anarchist Exclusion Act]]<br/> [[Anarchy in Somalia]]<br/> [[Australian Anarchist Centenary Celebrations|Australian Anarchist Centenary]]<br/> [[Barcelona May Days]]<br/> [[Carnival Against Capitalism]]<br/> [[Escuela Moderna]]{{·}} [[Hague Congress (1872)|Hague Congress]]<br/> [[Haymarket affair]]<br/> [[High Treason Incident]]<br/> [[International Anarchist Congress of Amsterdam|Congress of Amsterdam]]<br/> [[Kate Sharpley Library]]<br/> [[Kronstadt rebellion]]<br/> [[Labadie Collection]]{{·}} [[LIP (clockwork company)|LIP]]<br/> ''[[Manifesto of the Sixteen]]''<br/> [[May 1968]] {{·}} [[May Day]]<br/> [[Paris Commune]]<br/> [[Provo (movement)|Provo]]{{·}} [[Red inverted triangle]]<br/> {{longlink|[[Revolutionary Insurrectionary Army of Ukraine]]}} [[Spanish Revolution]]<br/> [[Left-wing uprisings against the Bolsheviks|Third Russian Revolution]]<br/> [[Tragic Week]]{{·}} [[Trial of the thirty]]\n"
			+ "\n"
			+ "|list5name  = Culture\n"
			+ "|list5title = Culture\n"
			+ "|list5 = [[Anarcho-punk]]{{·}} [[Anarchism and the arts|Arts]]<br/> [[Black anarchism]]{{·}} [[Culture jamming]]<br/> [[DIY culture]]{{·}} [[Freeganism]]<br/> [[Independent Media Center]]<br/> [[Infoshop]]{{·}} ''[[The Internationale]]''<br/> [[Jewish anarchism]]{{·}} [[Land and liberty (slogan)|Land and liberty]]<br/>  [[Lifestyle anarchism|Lifestylism]]{{·}} [[Popular education]]<br/>[[Property is theft!]]<br/> [[Radical cheerleading]]<br/> [[Radical environmentalism]]<br/> [[Squatting]]{{·}}[[Anarchist symbolism|Symbolism]]<br/> [[Anarchist terminology|Terminology]]{{·}}[[A las barricadas]] \n"
			+ "|list6name  = Economics\n"
			+ "|list6title = [[Anarchist economics|Economics]]\n"
			+ "|list6 = [[Agorism]]{{·}} [[Anarcho-capitalism|Capitalism]]{{·}} [[Collectivist anarchism|Collectivism]]<br/> [[Anarchist communism|Communism]]{{·}} [[Cooperative|Co-operatives]]<br/> [[Counter-economics]]{{·}} [[Free-market anarchism|Free market]]<br/> [[Free school]]{{·}} [[Give-away shop|Free store]]<br/> [[Geolibertarianism]]{{·}}[[Gift economy]]<br/> [[Market abolitionism]]{{·}}[[Mutual aid (politics)|Mutual aid]]<br/> [[Mutualism (economic theory)|Mutualism]]{{·}}[[Participatory economics]]<br/> [[Really Really Free Market]]<br/> [[Self-ownership]]{{·}}[[Anarcho-syndicalism|Syndicalism]]<br/> [[Wage slavery]]<br/> [[Workers' self-management]]\n"
			+ "\n"
			+ "|list7name  = By region\n"
			+ "|list7title = [[List of anarchist movements by region|By region]]\n"
			+ "|list7 = [[Anarchism in Africa|Africa]]{{·}} [[Anarchism in Austria-Hungary|Austria-Hungary]]{{·}} [[Anarchism in Brazil|Brazil]]<br/> [[Anarchism in Canada|Canada]]{{·}} [[Anarchism in China|China]]{{·}} [[Anarchism in Cuba|Cuba]]{{·}} [[Anarchism in England|England]]<br/> [[Anarchism in France|France]]{{·}} [[Anarchism in Greece|Greece]]{{·}} [[Anarchism in India|India]]{{·}} [[Anarchism in Ireland|Ireland]]<br/> [[Anarchism in Israel|Israel]]{{·}} [[Anarchism in Italy|Italy]]{{·}} [[Anarchism in Japan|Japan]]{{·}} [[Anarchism in Korea|Korea]]<br/> [[Anarchism in Mexico|Mexico]]{{·}} [[Anarchism in Poland|Poland]]{{·}} [[Anarchism in Russia|Russia]]{{·}} [[Anarchism in Spain|Spain]]<br/> [[Anarchism in Sweden|Sweden]]{{·}} [[Anarchism in Turkey|Turkey]]{{·}} [[Anarchism in Ukraine|Ukraine]]<br/> [[Anarchism in the United States|United States]]{{·}} [[Anarchism in Vietnam|Vietnam]]\n"
			+ "\n"
			+ "|list8name  = Lists\n"
			+ "|list8title = [[Lists of anarchism topics|Lists]]\n"
			+ "|list8 = [[List of anarcho-punk bands|Anarcho-punk bands]]{{·}} [[List of anarchist books|Books]]<br/> [[List of anarchist communities|Communities]]{{·}} [[List of fictional anarchists|Fictional characters]]<br/> [[List of Jewish anarchists|Jewish anarchists]]{{·}} [[List of anarchist musicians|Musicians]]<br/> [[List of anarchist organizations|Organizations]]{{·}} [[List of anarchist periodicals|Periodicals]]{{·}} [[List of anarchist poets|Poets]]<br/> [[List of Russian anarchists|Russian anarchists]]\n"
			+ "\n"
			+ "|list9name  = Related\n"
			+ "|list9title = Related topics\n"
			+ "|list9 = [[Anti-capitalism]]{{·}} [[Anti-communism#Anarchist anti-communism|Anti-communism]]<br/> [[Anti-consumerism]]{{·}} [[Anti-corporate activism|Anti-corporatism]]<br/> [[Anti-globalization]]{{·}} [[Antimilitarism]]<br/> [[Anti-statism]]{{·}} [[Anti-war]]{{·}} [[Autarchism]]<br/> [[Autonomism]]{{·}} [[Labour movement]]<br/> [[Left communism]]{{·}} [[Libertarianism]]<br/> {{longlink|[[Libertarian perspectives on revolution]]}} [[Libertarian socialism]]<br/> [[Situationist International]]\n"
			+ "}}<noinclude>\n" + "\n" + "[[Category:Anarchism templates| Anarchism]]\n" + "\n" + "[[ar:قالب:لاسلطوية]]\n"
			+ "[[ca:Plantilla:Anarquisme]]\n" + "[[da:Skabelon:Anarkisme]]\n" + "[[es:Plantilla:Anarquismo]]\n"
			+ "[[el:Πρότυπο:Αναρχισμός]]\n" + "[[he:תבנית:אנרכיזם]]\n" + "[[id:Templat:Anarkisme]]\n"
			+ "[[is:Snið:Stjórnleysisstefna]]\n" + "[[nl:Sjabloon:Anarchisme]]\n" + "[[pl:Szablon:Anarchizm]]\n"
			+ "[[pt:Predefinição:Anarquismo]]\n" + "[[ro:Format:Anarhism]]\n" + "[[ru:Шаблон:Анархизм]]\n"
			+ "[[sk:Šablóna:Anarchizmus]]\n" + "[[sl:Predloga:Anarhizem]]\n" + "[[sv:Mall:Anarkism]]\n" + "[[tr:Şablon:Anarşizm]]\n"
			+ "</noinclude>\n" + "";

	public final static String IDEOLOGY = "{{Sidebar with collapsible lists\n" + "|style      = width:18.0em;\n"
			+ "|pretitle   = Part of [[:Category:Politics|the Politics series]] on\n"
			+ "|title      = {{#if:{{{linkoverride|}}}|{{{linkoverride}}}|{{{ideology}}}}}\n"
			+ "|titlestyle = color:black; font-size:200%; font-weight:normal;\n" + "|image      = {{{image|}}}\n"
			+ "|imagestyle = {{{imagestyle|}}}\n"
			+ "|listtitlestyle = background:transparent; border-bottom:1px solid #aaa; text-align: center; padding-left:0.4em;\n" + "\n"
			+ "|list1name   = {{{list1name|}}}\n" + "|list1title  = {{{list1title|}}}\n" + "|list1       = {{{list1 |}}}\n"
			+ "|list2name   = {{{list2name|}}} \n" + "|list2title  = {{{list2title|}}}\n" + "|list2       = {{{list2|}}}\n"
			+ "|list3name   = {{{list3name|}}} \n" + "|list3title  = {{{list3title|}}}\n" + "|list3       = {{{list3 |}}} \n"
			+ "|list4name   = {{{list4name |}}}  \n" + "|list4title  = {{{list4title|}}} \n" + "|list4       = {{{list4 |}}}\n"
			+ "|list5name   = {{{list5name|}}}\n" + "|list5title  = {{{list5title|}}}\n" + "|list5       = {{{list5|}}}\n"
			+ "|list6name   = {{{list6name|}}}\n" + "|list6title  = {{{list6title|}}}\n" + "|list6       = {{{list6|}}}\n"
			+ "|list7name   = {{{list7name|}}}\n" + "|list7title  = {{{list7title|}}}  \n" + "|list7       = {{{list7|}}}\n"
			+ "|list8name   = {{{list8name|}}}\n" + "|list8title  = {{{list8title|}}}\n" + "|list8       = {{{list8|}}}\n"
			+ "|list9name   = {{{list9name|}}}\n" + "|list9title  = {{{list9title|}}}\n" + "|list9       = {{{list9 |}}}\n"
			+ "|list10name  = {{{list10name|}}}\n" + "|list10title = {{{list10title|}}}\n" + "|list10      = {{{list10|}}}\n" + "\n"
			+ "|belowstyle = text-align:center;\n" + "|below = '''[[:portal:{{{ideology}}}|{{{ideology}}} Portal]]'''\n"
			+ "}}<noinclude>\n" + "</noinclude>\n" + "";

	public final static String SIDEBAR_WITH_COLLAPSIBLE_LISTS = "<noinclude>{{start sidebar page}}\n"
			+ "</noinclude>{{Sidebar\n"
			+ "|name            = {{{name|{{PAGENAME}}}}}\n"
			+ "|class           = {{{class|{{{bodyclass|}}}}}}\n"
			+ "|style           = {{{style|{{{bodystyle|}}}}}} <noinclude>width:15.0em;</noinclude>\n"
			+ "|cellspacing     = {{{cellspacing|}}}\n"
			+ "|cellpadding     = {{{cellpadding|}}}\n"
			+ "\n"
			+ "|outertitle      = {{{outertitle<includeonly>|</includeonly>}}}\n"
			+ "|outertitleclass = {{{outertitleclass|}}}\n"
			+ "|outertitlestyle = {{{outerttitlestyle|}}}\n"
			+ "\n"
			+ "|topimage        = {{{topimage<includeonly>|</includeonly>}}}\n"
			+ "|topimageclass   = {{{topimageclass|}}}\n"
			+ "|topimagestyle   = {{{topimagestyle|}}}\n"
			+ "|topcaption      = {{{topcaption<includeonly>|</includeonly>}}}\n"
			+ "|topcaptionstyle = {{{topcaptionstyle|}}}\n"
			+ "\n"
			+ "|pretitle        = {{{pretitle<includeonly>|</includeonly>}}}\n"
			+ "|pretitlestyle   = {{{pretitlestyle|}}}\n"
			+ "|title           = {{{title<includeonly>|</includeonly>}}}\n"
			+ "|titleclass      = {{{titleclass|}}}\n"
			+ "|titlestyle      = {{{titlestyle|}}}\n"
			+ "\n"
			+ "|image           = {{{image<includeonly>|</includeonly>}}}\n"
			+ "|imageclass      = {{{imageclass|}}}\n"
			+ "|imagestyle      = {{{imagestyle|}}}\n"
			+ "|caption         = {{{caption<includeonly>|</includeonly>}}}\n"
			+ "|captionstyle    = {{{captionstyle|}}}\n"
			+ "\n"
			+ "|abovestyle = border-top:1px solid #aaa; border-bottom:1px solid #aaa; {{{abovestyle|}}}\n"
			+ "|above = {{{above<includeonly>|</includeonly>}}}\n"
			+ "\n"
			+ "|headingstyle    = {{{headingstyle|}}}\n"
			+ "|contentstyle    = {{{contentstyle|}}}\n"
			+ "\n"
			+ "|heading1style = {{{heading1style|}}}\n"
			+ "|heading1 = {{{heading1|}}}\n"
			+ "|content1style = {{{content1style|}}}\n"
			+ "|content1 = {{#if:{{{list1<includeonly>|</includeonly>}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list1framestyle|}}}\n"
			+ "                |title      = {{{list1title<includeonly>|</includeonly>}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list1titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list1name}}} |display:inline;}} {{{liststyle|}}}{{{list1style|}}}\n"
			+ "                | {{{list1}}}\n"
			+ "               }}\n"
			+ "             | {{{content1|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading2style = {{{heading2style|}}}\n"
			+ "|heading2 = {{{heading2|}}}\n"
			+ "|content2style = {{{content2style|}}}\n"
			+ "|content2 = {{#if:{{{list2<includeonly>|</includeonly>}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list2framestyle|}}}\n"
			+ "                |title      = {{{list2title<includeonly>|</includeonly>}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list2titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list2name}}} |display:inline;}} {{{liststyle|}}}{{{list2style|}}}\n"
			+ "                | {{{list2}}}\n"
			+ "               }}\n"
			+ "             | {{{content2|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading3style = {{{heading3style|}}}\n"
			+ "|heading3 = {{{heading3|}}}\n"
			+ "|content3style = {{{content3style|}}}\n"
			+ "|content3 = {{#if:{{{list3<includeonly>|</includeonly>}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list3framestyle|}}}\n"
			+ "                |title      = {{{list3title<includeonly>|</includeonly>}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list3titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list3name}}} |display:inline;}} {{{liststyle|}}}{{{list3style|}}}\n"
			+ "                | {{{list3}}}\n"
			+ "               }}\n"
			+ "             | {{{content3|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading4style = {{{heading4style|}}}\n"
			+ "|heading4 = {{{heading4|}}}\n"
			+ "|content4style = {{{content4style|}}}\n"
			+ "|content4 = <noinclude>''(......etc......)''</noinclude><!--\n"
			+ "         --><includeonly><!--\n"
			+ "          -->{{#if:{{{list4|}}}\n"
			+ "              | {{Collapsible list\n"
			+ "                 |framestyle = {{{listframestyle|}}}{{{list4framestyle|}}}\n"
			+ "                 |title      = {{{list4title|}}}\n"
			+ "                 |titlestyle = {{{listtitlestyle|}}}{{{list4titlestyle|}}}\n"
			+ "                 |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list4name}}} |display:inline;}} {{{liststyle|}}}{{{list4style|}}}\n"
			+ "                 | {{{list4}}}\n"
			+ "                }}\n"
			+ "              | {{{content4|}}}\n"
			+ "             }}<!--\n"
			+ "         --></includeonly>\n"
			+ "\n"
			+ "|heading5style = {{{heading5style|}}}\n"
			+ "|heading5 = {{{heading5|}}}\n"
			+ "|content5style = {{{content5style|}}}\n"
			+ "|content5 = {{#if:{{{list5|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list5framestyle|}}}\n"
			+ "                |title      = {{{list5title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list5titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list5name}}} |display:inline;}} {{{liststyle|}}}{{{list5style|}}}\n"
			+ "                | {{{list5}}}\n"
			+ "               }}\n"
			+ "             | {{{content5|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading6style = {{{heading6style|}}}\n"
			+ "|heading6 = {{{heading6|}}}\n"
			+ "|content6style = {{{content6style|}}}\n"
			+ "|content6 = {{#if:{{{list6|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list6framestyle|}}}\n"
			+ "                |title      = {{{list6title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list6titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list6name}}} |display:inline;}} {{{liststyle|}}}{{{list6style|}}}\n"
			+ "                | {{{list6}}}\n"
			+ "               }}\n"
			+ "             | {{{content6|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading7style = {{{heading7style|}}}\n"
			+ "|heading7 = {{{heading7|}}}\n"
			+ "|content7style = {{{content7style|}}}\n"
			+ "|content7 = {{#if:{{{list7|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list7framestyle|}}}\n"
			+ "                |title      = {{{list7title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list7titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list7name}}} |display:inline;}} {{{liststyle|}}}{{{list7style|}}}\n"
			+ "                | {{{list7}}}\n"
			+ "               }}\n"
			+ "             | {{{content7|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading8style = {{{heading8style|}}}\n"
			+ "|heading8 = {{{heading8|}}}\n"
			+ "|content8style = {{{content8style|}}}\n"
			+ "|content8 = {{#if:{{{list8|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list8framestyle|}}}\n"
			+ "                |title      = {{{list8title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list8titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list8name}}} |display:inline;}} {{{liststyle|}}}{{{list8style|}}}\n"
			+ "                | {{{list8}}}\n"
			+ "               }}\n"
			+ "             | {{{content8|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading9style = {{{heading9style|}}}\n"
			+ "|heading9 = {{{heading9|}}}\n"
			+ "|content9style = {{{content9style|}}}\n"
			+ "|content9 = {{#if:{{{list9|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list9framestyle|}}}\n"
			+ "                |title      = {{{list9title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list9titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list9name}}} |display:inline;}} {{{liststyle|}}}{{{list9style|}}}\n"
			+ "                | {{{list9}}}\n"
			+ "               }}\n"
			+ "             | {{{content9|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading10style = {{{heading10style|}}}\n"
			+ "|heading10 = {{{heading10|}}}\n"
			+ "|content10style = {{{content10style|}}}\n"
			+ "|content10 = {{#if:{{{list10|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list10framestyle|}}}\n"
			+ "                |title      = {{{list10title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list10titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list10name}}} |display:inline;}} {{{liststyle|}}}{{{list10style|}}}\n"
			+ "                | {{{list10}}}\n"
			+ "               }}\n"
			+ "             | {{{content10|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading11style = {{{heading11style|}}}\n"
			+ "|heading11 = {{{heading11|}}}\n"
			+ "|content11style = {{{content11style|}}}\n"
			+ "|content11 = {{#if:{{{list11|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list11framestyle|}}}\n"
			+ "                |title      = {{{list11title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list11titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list11name}}} |display:inline;}} {{{liststyle|}}}{{{list11style|}}}\n"
			+ "                | {{{list11}}}\n"
			+ "               }}\n"
			+ "             | {{{content11|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading12style = {{{heading12style|}}}\n"
			+ "|heading12 = {{{heading12|}}}\n"
			+ "|content12style = {{{content12style|}}}\n"
			+ "|content12 = {{#if:{{{list12|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list12framestyle|}}}\n"
			+ "                |title      = {{{list12title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list12titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list12name}}} |display:inline;}} {{{liststyle|}}}{{{list12style|}}}\n"
			+ "                | {{{list12}}}\n"
			+ "               }}\n"
			+ "             | {{{content12|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading13style = {{{heading13style|}}}\n"
			+ "|heading13 = {{{heading13|}}}\n"
			+ "|content13style = {{{content13style|}}}\n"
			+ "|content13 = {{#if:{{{list13|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list13framestyle|}}}\n"
			+ "                |title      = {{{list13title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list13titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list13name}}} |display:inline;}} {{{liststyle|}}}{{{list13style|}}}\n"
			+ "                | {{{list13}}}\n"
			+ "               }}\n"
			+ "             | {{{content13|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "|heading14style = {{{heading14style|}}}\n"
			+ "|heading14 = {{{heading14|}}}\n"
			+ "|content14style = {{{content14style|}}}\n"
			+ "|content14 = {{#if:{{{list14|}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list14framestyle|}}}\n"
			+ "                |title      = {{{list14title|}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list14titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list14name}}} |display:inline;}} {{{liststyle|}}}{{{list14style|}}}\n"
			+ "                | {{{list14}}}\n"
			+ "               }}\n"
			+ "             | {{{content14|}}}\n"
			+ "            }}\n"
			+ "\n"
			+ "\n"
			+ "|heading15style = {{{heading15style|}}}\n"
			+ "|heading15 = {{{heading15|}}}\n"
			+ "|content15style = {{{content15style|}}}\n"
			+ "|content15 = {{#if:{{{list15<includeonly>|</includeonly>}}}\n"
			+ "             | {{Collapsible list\n"
			+ "                |framestyle = {{{listframestyle|}}}{{{list15framestyle|}}}\n"
			+ "                |title      = {{{list15title<includeonly>|</includeonly>}}}\n"
			+ "                |titlestyle = {{{listtitlestyle|}}}{{{list15titlestyle|}}}\n"
			+ "                |liststyle  = padding:0.2em 0 0.4em; text-align:center; {{#ifeq:{{{expanded|}}}|all |display:inline;}} {{#ifeq:{{{expanded|}}}|{{{list15name}}} |display:inline;}} {{{liststyle|}}}{{{list15style|}}}\n"
			+ "                | {{{list15}}}\n" + "               }}\n" + "             | {{{content15|}}}\n" + "            }}\n"
			+ "\n" + "<!--Gap between sections above and any subsequent 'below' section:-->\n"
			+ "|content16style = padding:0; line-height:0.4em;\n"
			+ "|content16 = {{#if:{{{below<includeonly>|</includeonly>}}} |&nbsp;}}\n" + "\n"
			+ "|belowstyle = border-top:1px solid #aaa; border-bottom:1px solid #aaa; {{{belowstyle|}}}\n"
			+ "|below = {{{below<includeonly>|</includeonly>}}}\n" + "\n" + "|tnavbar        = {{{tnavbar|}}}\n"
			+ "|tnavbarstyle   = padding-top:0.6em; {{{tnavbarstyle|}}}\n" + "|tnavbaroptions = {{{tnavbaroptions|}}}\n" + "\n"
			+ "}}<noinclude>\n" + "{{end sidebar page}}\n" + "\n"
			+ "<!---Please add metadata (categories, interwikis) to the <includeonly> section at the\n"
			+ "     bottom of [[Template:Sidebar with collapsible lists/doc]] page, not here - thanks!--->\n" + "";

	public final static String IF_IMAGE_TEST = "{{#if:{{{image|}}}|[[File:{{{image|}}}|{{#if:{{{image_size|{{{imagesize|}}}}}}|{{{image_size|{{{imagesize|}}}}}}|220px}}|alt={{{alt|}}}]]}}";

	boolean fSemanticWebActive;

	static {
		TagNode.addAllowedAttribute("style");
		Configuration.DEFAULT_CONFIGURATION.addUriScheme("tel");
		Configuration.DEFAULT_CONFIGURATION.addInterwikiLink("intra", "/${title}");
	}

	public WikiTestModel(String imageBaseURL, String linkBaseURL) {
		this(Locale.ENGLISH, imageBaseURL, linkBaseURL);
	}

	/**
	 * Add German namespaces to the wiki model
	 * 
	 * @param imageBaseURL
	 * @param linkBaseURL
	 */
	public WikiTestModel(Locale locale, String imageBaseURL, String linkBaseURL) {
		super(Configuration.DEFAULT_CONFIGURATION, locale, imageBaseURL, linkBaseURL);
		fSemanticWebActive = false;
	}

	/**
	 * Add templates: &quot;Test&quot;, &quot;Templ1&quot;, &quot;Templ2&quot;,
	 * &quot;Include Page&quot;
	 * 
	 */
	@Override
	public String getRawWikiContent(String namespace, String articleName, Map<String, String> map) {
		String result = super.getRawWikiContent(namespace, articleName, map);
		if (result != null) {
			// found magic word template
			return result;
		}
		String name = encodeTitleToUrl(articleName, true);
		if (isTemplateNamespace(namespace)) {
			// if (MagicWord.isMagicWord(articleName)) {
			// return MagicWord.processMagicWord(articleName, this);
			// }
			if (name.equals("Reflist")) {
				return REFLIST_TEXT;
			} else if (name.equals("!")) {
				return PIPE_SYMBOL;
			} else if (name.equals("2x")) {
				return DOUBLE_PARAMETER;
			} else if (name.equals("Cat")) {
				return CAT;
			} else if (name.equals("!")) {
				return "|<noinclude>{{template doc}}</noinclude>";
			} else if (name.equals("Infobox_Software")) {
				return INFOBOX_SOFTWARE_TEXT;
			} else if (name.equals("Cite_web")) {
				return CITE_WEB_TEXT;
			} else if (name.equals("Navbox")) {
				return NAVBOX_TEXT;
			} else if (name.equals("Tnavbar")) {
				return TNAVBAR_TEXT;
			} else if (name.equals("Nested_tempplate_test")) {
				return NESTED_TEMPLATE_TEST;
			} else if (name.equals("Nested")) {
				return NESTED;
			} else if (name.equals("Recursion")) {
				return ENDLESS_RECURSION_TEST;
			} else if (name.equals("Test")) {
				return "a) First: {{{1}}} Second: {{{2}}}";
			} else if (name.equals("Templ1")) {
				return "b) First: {{{a}}} Second: {{{2}}}";
			} else if (name.equals("Templ2")) {
				return "c) First: {{{1}}} Second: {{{2}}}";
			} else if (name.equals("Ifeq")) {
				return IFEQ_TEST;
			} else if (name.equals("Further")) {
				return FURTHER;
			} else if (name.equals("Tl")) {
				return TL;
			} else if (name.equals("PronEng")) {
				return PRON_ENG;
			} else if (name.equals("Pron-en")) {
				return PRON_EN;
			} else if (name.equals("Anarchism_sidebar")) {
				return ANARCHISM_SIDEBAR;
			} else if (name.equals("Ideology")) {
				return IDEOLOGY;
			} else if (name.equals("Sidebar_with_collapsible_lists")) {
				return SIDEBAR_WITH_COLLAPSIBLE_LISTS;
			} else if (name.equals("If_image_test")) {
				return IF_IMAGE_TEST;
			} else if (name.equals("Birth_date_and_age")) {
				return BIRTH_DATE_AND_AGE;
			} else if (name.equals("MONTHNAME")) {
				return MONTHNAME;
			} else if (name.equals("MONTHNUMBER")) {
				return MONTHNUMBER;
			} else if (name.equals("Age")) {
				return AGE;
			} else if (name.equals("Born_data")) {
				return BORN_DATA;
			}
		} else {
			if (name.equals("Include_Page")) {
				return "an include page";
			}
		}
		return null;
	}

	/**
	 * Set the German image namespace
	 */
	@Override
	public String getImageNamespace() {
		return "Bild";
	}

	@Override
	public boolean isImageNamespace(String name) {
		return super.isImageNamespace(name) || name.equals(getImageNamespace());
	}

	@Override
	public boolean isSemanticWebActive() {
		return fSemanticWebActive;
	}

	@Override
	public void setSemanticWebActive(boolean semanticWeb) {
		this.fSemanticWebActive = semanticWeb;
	}

	public boolean showSyntax(String tagName) {
		// if (tagName.equals("groovy")) {
		// return false;
		// }
		return true;
	}


	@Override
	public void appendExternalLink(String uriSchemeName, String link, String linkName, boolean withoutSquareBrackets) {
		if (uriSchemeName.equals("tel")) {
			// example for a telephone link
			link = Utils.escapeXml(link, true, false, false);
			TagNode aTagNode = new TagNode("a");
			aTagNode.addAttribute("href", link, true);
			aTagNode.addAttribute("class", "telephonelink", true);
			aTagNode.addAttribute("title", link, true);
			if (withoutSquareBrackets) {
				append(aTagNode);
				aTagNode.addChild(new ContentToken(linkName));
			} else {
				String trimmedText = linkName.trim();
				if (trimmedText.length() > 0) {
					pushNode(aTagNode);
					WikipediaParser.parseRecursive(trimmedText, this, false, true);
					popNode();
				}
			}
			return;
		}
		super.appendExternalLink(uriSchemeName, link, linkName, withoutSquareBrackets);
	}

	/**
	 * Test for <a
	 * href="http://groups.google.de/group/bliki/t/a0540e27f27f02a5">Discussion:
	 * Hide Table of Contents (toc)?</a>
	 */
	// public ITableOfContent createTableOfContent(boolean isTOCIdentifier) {
	// if (fToCSet == null) {
	// fToCSet = new HashSet<String>();
	// fTableOfContent = new ArrayList<Object>();
	// }
	// fTableOfContentTag = new TableOfContentTag("div") {
	// public void setShowToC(boolean showToC) {
	// // do nothing
	// }
	// };
	// return fTableOfContentTag;
	// }
}
