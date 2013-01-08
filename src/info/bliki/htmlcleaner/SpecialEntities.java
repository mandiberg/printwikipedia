/*  Copyright (c) 2006-2007, Vladimir Nikic
    All rights reserved.
	
    Redistribution and use of this software in source and binary forms, 
    with or without modification, are permitted provided that the following 
    conditions are met:
	
    * Redistributions of source code must retain the above
      copyright notice, this list of conditions and the
      following disclaimer.
	
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the
      following disclaimer in the documentation and/or other
      materials provided with the distribution.
	
    * The name of HtmlCleaner may not be used to endorse or promote 
      products derived from this software without specific prior
      written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
    POSSIBILITY OF SUCH DAMAGE.
	
    You can contact Vladimir Nikic by sending e-mail to
    nikic_vladimir@yahoo.com. Please include the word "HtmlCleaner" in the
    subject line.
*/

package info.bliki.htmlcleaner;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>This class contains map with special entities used in HTML and their
 * unicodes.</p>
 * 
 * Created by: Vladimir Nikic<br/>
 * Date: November, 2006.
 */
public class SpecialEntities {
	
	static Map entities = new HashMap();

	static {
		entities.put("quot", Integer.valueOf(34));
		entities.put("amp", Integer.valueOf(38));
		entities.put("lt", Integer.valueOf(60));
		entities.put("gt", Integer.valueOf(62));
		entities.put("nbsp", Integer.valueOf(160));
		entities.put("iexcl", Integer.valueOf(161));
		entities.put("cent", Integer.valueOf(162));
		entities.put("pound", Integer.valueOf(163));
		entities.put("curren", Integer.valueOf(164));
		entities.put("yen", Integer.valueOf(165));
		entities.put("brvbar", Integer.valueOf(166));
		entities.put("sect", Integer.valueOf(167));
		entities.put("uml", Integer.valueOf(168));
		entities.put("copy", Integer.valueOf(169));
		entities.put("ordf", Integer.valueOf(170));
		entities.put("laquo", Integer.valueOf(171));
		entities.put("not", Integer.valueOf(172));
		entities.put("shy", Integer.valueOf(173));
		entities.put("reg", Integer.valueOf(174));
		entities.put("macr", Integer.valueOf(175));
		entities.put("deg", Integer.valueOf(176));
		entities.put("plusmn", Integer.valueOf(177));
		entities.put("sup2", Integer.valueOf(178));
		entities.put("sup3", Integer.valueOf(179));
		entities.put("acute", Integer.valueOf(180));
		entities.put("micro", Integer.valueOf(181));
		entities.put("para", Integer.valueOf(182));
		entities.put("middot", Integer.valueOf(183));
		entities.put("cedil", Integer.valueOf(184));
		entities.put("sup1", Integer.valueOf(185));
		entities.put("ordm", Integer.valueOf(186));
		entities.put("raquo", Integer.valueOf(187));
		entities.put("frac14", Integer.valueOf(188));
		entities.put("frac12", Integer.valueOf(189));
		entities.put("frac34", Integer.valueOf(190));
		entities.put("iquest", Integer.valueOf(191));
		entities.put("Agrave", Integer.valueOf(192));
		entities.put("Aacute", Integer.valueOf(193));
		entities.put("Acirc", Integer.valueOf(194));
		entities.put("Atilde", Integer.valueOf(195));
		entities.put("Auml", Integer.valueOf(196));
		entities.put("Aring", Integer.valueOf(197));
		entities.put("AElig", Integer.valueOf(198));
		entities.put("Ccedil", Integer.valueOf(199));
		entities.put("Egrave", Integer.valueOf(200));
		entities.put("Eacute", Integer.valueOf(201));
		entities.put("Ecirc", Integer.valueOf(202));
		entities.put("Euml", Integer.valueOf(203));
		entities.put("Igrave", Integer.valueOf(204));
		entities.put("Iacute", Integer.valueOf(205));
		entities.put("Icirc", Integer.valueOf(206));
		entities.put("Iuml", Integer.valueOf(207));
		entities.put("ETH", Integer.valueOf(208));
		entities.put("Ntilde", Integer.valueOf(209));
		entities.put("Ograve", Integer.valueOf(210));
		entities.put("Oacute", Integer.valueOf(211));
		entities.put("Ocirc", Integer.valueOf(212));
		entities.put("Otilde", Integer.valueOf(213));
		entities.put("Ouml", Integer.valueOf(214));
		entities.put("times", Integer.valueOf(215));
		entities.put("Oslash", Integer.valueOf(216));
		entities.put("Ugrave", Integer.valueOf(217));
		entities.put("Uacute", Integer.valueOf(218));
		entities.put("Ucirc", Integer.valueOf(219));
		entities.put("Uuml", Integer.valueOf(220));
		entities.put("Yacute", Integer.valueOf(221));
		entities.put("THORN", Integer.valueOf(222));
		entities.put("szlig", Integer.valueOf(223));
		entities.put("agrave", Integer.valueOf(224));
		entities.put("aacute", Integer.valueOf(225));
		entities.put("acirc", Integer.valueOf(226));
		entities.put("atilde", Integer.valueOf(227));
		entities.put("auml", Integer.valueOf(228));
		entities.put("aring", Integer.valueOf(229));
		entities.put("aelig", Integer.valueOf(230));
		entities.put("ccedil", Integer.valueOf(231));
		entities.put("egrave", Integer.valueOf(232));
		entities.put("eacute", Integer.valueOf(233));
		entities.put("ecirc", Integer.valueOf(234));
		entities.put("euml", Integer.valueOf(235));
		entities.put("igrave", Integer.valueOf(236));
		entities.put("iacute", Integer.valueOf(237));
		entities.put("icirc", Integer.valueOf(238));
		entities.put("iuml", Integer.valueOf(239));
		entities.put("eth", Integer.valueOf(240));
		entities.put("ntilde", Integer.valueOf(241));
		entities.put("ograve", Integer.valueOf(242));
		entities.put("oacute", Integer.valueOf(243));
		entities.put("ocirc", Integer.valueOf(244));
		entities.put("otilde", Integer.valueOf(245));
		entities.put("ouml", Integer.valueOf(246));
		entities.put("divide", Integer.valueOf(247));
		entities.put("oslash", Integer.valueOf(248));
		entities.put("ugrave", Integer.valueOf(249));
		entities.put("uacute", Integer.valueOf(250));
		entities.put("ucirc", Integer.valueOf(251));
		entities.put("uuml", Integer.valueOf(252));
		entities.put("yacute", Integer.valueOf(253));
		entities.put("thorn", Integer.valueOf(254));
		entities.put("yuml", Integer.valueOf(255));
		entities.put("OElig", Integer.valueOf(338));
		entities.put("oelig", Integer.valueOf(339));
		entities.put("Scaron", Integer.valueOf(352));
		entities.put("scaron", Integer.valueOf(353));
		entities.put("Yuml", Integer.valueOf(376));
		entities.put("fnof", Integer.valueOf(402));
		entities.put("circ", Integer.valueOf(710));
		entities.put("tilde", Integer.valueOf(732));
		entities.put("Alpha", Integer.valueOf(913));
		entities.put("Beta", Integer.valueOf(914));
		entities.put("Gamma", Integer.valueOf(915));
		entities.put("Delta", Integer.valueOf(916));
		entities.put("Epsilon", Integer.valueOf(917));
		entities.put("Zeta", Integer.valueOf(918));
		entities.put("Eta", Integer.valueOf(919));
		entities.put("Theta", Integer.valueOf(920));
		entities.put("Iota", Integer.valueOf(921));
		entities.put("Kappa", Integer.valueOf(922));
		entities.put("Lambda", Integer.valueOf(923));
		entities.put("Mu", Integer.valueOf(924));
		entities.put("Nu", Integer.valueOf(925));
		entities.put("Xi", Integer.valueOf(926));
		entities.put("Omicron", Integer.valueOf(927));
		entities.put("Pi", Integer.valueOf(928));
		entities.put("Rho", Integer.valueOf(929));
		entities.put("Sigma", Integer.valueOf(931));
		entities.put("Tau", Integer.valueOf(932));
		entities.put("Upsilon", Integer.valueOf(933));
		entities.put("Phi", Integer.valueOf(934));
		entities.put("Chi", Integer.valueOf(935));
		entities.put("Psi", Integer.valueOf(936));
		entities.put("Omega", Integer.valueOf(937));
		entities.put("alpha", Integer.valueOf(945));
		entities.put("beta", Integer.valueOf(946));
		entities.put("gamma", Integer.valueOf(947));
		entities.put("delta", Integer.valueOf(948));
		entities.put("epsilon", Integer.valueOf(949));
		entities.put("zeta", Integer.valueOf(950));
		entities.put("eta", Integer.valueOf(951));
		entities.put("theta", Integer.valueOf(952));
		entities.put("iota", Integer.valueOf(953));
		entities.put("kappa", Integer.valueOf(954));
		entities.put("lambda", Integer.valueOf(955));
		entities.put("mu", Integer.valueOf(956));
		entities.put("nu", Integer.valueOf(957));
		entities.put("xi", Integer.valueOf(958));
		entities.put("omicron", Integer.valueOf(959));
		entities.put("pi", Integer.valueOf(960));
		entities.put("rho", Integer.valueOf(961));
		entities.put("sigmaf", Integer.valueOf(962));
		entities.put("sigma", Integer.valueOf(963));
		entities.put("tau", Integer.valueOf(964));
		entities.put("upsilon", Integer.valueOf(965));
		entities.put("phi", Integer.valueOf(966));
		entities.put("chi", Integer.valueOf(967));
		entities.put("psi", Integer.valueOf(968));
		entities.put("omega", Integer.valueOf(969));
		entities.put("thetasym", Integer.valueOf(977));
		entities.put("upsih", Integer.valueOf(978));
		entities.put("piv", Integer.valueOf(982));
		entities.put("ensp", Integer.valueOf(8194));
		entities.put("emsp", Integer.valueOf(8195));
		entities.put("thinsp", Integer.valueOf(8201));
		entities.put("zwnj", Integer.valueOf(8204));
		entities.put("zwj", Integer.valueOf(8205));
		entities.put("lrm", Integer.valueOf(8206));
		entities.put("rlm", Integer.valueOf(8207));
		entities.put("ndash", Integer.valueOf(8211));
		entities.put("mdash", Integer.valueOf(8212));
		entities.put("lsquo", Integer.valueOf(8216));
		entities.put("rsquo", Integer.valueOf(8217));
		entities.put("sbquo", Integer.valueOf(8218));
		entities.put("ldquo", Integer.valueOf(8220));
		entities.put("rdquo", Integer.valueOf(8221));
		entities.put("bdquo", Integer.valueOf(8222));
		entities.put("dagger", Integer.valueOf(8224));
		entities.put("Dagger", Integer.valueOf(8225));
		entities.put("bull", Integer.valueOf(8226));
		entities.put("hellip", Integer.valueOf(8230));
		entities.put("permil", Integer.valueOf(8240));
		entities.put("prime", Integer.valueOf(8242));
		entities.put("Prime", Integer.valueOf(8243));
		entities.put("lsaquo", Integer.valueOf(8249));
		entities.put("rsaquo", Integer.valueOf(8250));
		entities.put("oline", Integer.valueOf(8254));
		entities.put("frasl", Integer.valueOf(8260));
		entities.put("euro", Integer.valueOf(8364));
		entities.put("image", Integer.valueOf(8465));
		entities.put("weierp", Integer.valueOf(8472));
		entities.put("real", Integer.valueOf(8476));
		entities.put("trade", Integer.valueOf(8482));
		entities.put("alefsym", Integer.valueOf(8501));
		entities.put("larr", Integer.valueOf(8592));
		entities.put("uarr", Integer.valueOf(8593));
		entities.put("rarr", Integer.valueOf(8594));
		entities.put("darr", Integer.valueOf(8595));
		entities.put("harr", Integer.valueOf(8596));
		entities.put("crarr", Integer.valueOf(8629));
		entities.put("lArr", Integer.valueOf(8656));
		entities.put("uArr", Integer.valueOf(8657));
		entities.put("rArr", Integer.valueOf(8658));
		entities.put("dArr", Integer.valueOf(8659));
		entities.put("hArr", Integer.valueOf(8660));
		entities.put("forall", Integer.valueOf(8704));
		entities.put("part", Integer.valueOf(8706));
		entities.put("exist", Integer.valueOf(8707));
		entities.put("empty", Integer.valueOf(8709));
		entities.put("nabla", Integer.valueOf(8711));
		entities.put("isin", Integer.valueOf(8712));
		entities.put("notin", Integer.valueOf(8713));
		entities.put("ni", Integer.valueOf(8715));
		entities.put("prod", Integer.valueOf(8719));
		entities.put("sum", Integer.valueOf(8721));
		entities.put("minus", Integer.valueOf(8722));
		entities.put("lowast", Integer.valueOf(8727));
		entities.put("radic", Integer.valueOf(8730));
		entities.put("prop", Integer.valueOf(8733));
		entities.put("infin", Integer.valueOf(8734));
		entities.put("ang", Integer.valueOf(8736));
		entities.put("and", Integer.valueOf(8743));
		entities.put("or", Integer.valueOf(8744));
		entities.put("cap", Integer.valueOf(8745));
		entities.put("cup", Integer.valueOf(8746));
		entities.put("int", Integer.valueOf(8747));
		entities.put("there4", Integer.valueOf(8756));
		entities.put("sim", Integer.valueOf(8764));
		entities.put("cong", Integer.valueOf(8773));
		entities.put("asymp", Integer.valueOf(8776));
		entities.put("ne", Integer.valueOf(8800));
		entities.put("equiv", Integer.valueOf(8801));
		entities.put("le", Integer.valueOf(8804));
		entities.put("ge", Integer.valueOf(8805));
		entities.put("sub", Integer.valueOf(8834));
		entities.put("sup", Integer.valueOf(8835));
		entities.put("nsub", Integer.valueOf(8836));
		entities.put("sube", Integer.valueOf(8838));
		entities.put("supe", Integer.valueOf(8839));
		entities.put("oplus", Integer.valueOf(8853));
		entities.put("otimes", Integer.valueOf(8855));
		entities.put("perp", Integer.valueOf(8869));
		entities.put("sdot", Integer.valueOf(8901));
		entities.put("lceil", Integer.valueOf(8968));
		entities.put("rceil", Integer.valueOf(8969));
		entities.put("lfloor", Integer.valueOf(8970));
		entities.put("rfloor", Integer.valueOf(8971));
		entities.put("lang", Integer.valueOf(9001));
		entities.put("rang", Integer.valueOf(9002));
		entities.put("loz", Integer.valueOf(9674));
		entities.put("spades", Integer.valueOf(9824));
		entities.put("clubs", Integer.valueOf(9827));
		entities.put("hearts", Integer.valueOf(9829));
		entities.put("diams", Integer.valueOf(9830));
	}
	
}