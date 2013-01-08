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

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * <p>Command line usage class.</p>
 *
 * Created by: Vladimir Nikic <br/>
 * Date: November, 2006.
 */
public class CommandLine {

    private static String getArgValue(String[] args, String name) {
        for (int i = 0; i < args.length; i++) {
            String curr = args[i];
            int eqIndex = curr.indexOf('=');
            if (eqIndex >= 0) {
                String argName = curr.substring(0, eqIndex).trim();
                String argValue = curr.substring(eqIndex+1).trim();

                if (argName.toLowerCase().startsWith(name.toLowerCase())) {
                    return argValue;
                }
            }
        }

        return "";
    }

    private static boolean toBoolean(String s) {
        return s != null && ( "on".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) );
    }

    public static void main(String[] args) throws IOException {
        String source = getArgValue(args, "src");
        if ( "".equals(source) ) {
            System.err.println("Usage: java -jar htmlcleanerXX.jar src = <url | file> [incharset = <charset>] [dest = <file>] [outcharset = <charset>] [options...]");
            System.err.println("");
            System.err.println("where options include:");
            System.err.println("    outputtype = simple | compact | pretty");
            System.err.println("    advancedxmlescape = true | false");
            System.err.println("    usecdata = true | false");
            System.err.println("    specialentities = true | false");
            System.err.println("    unicodechars = true | false");
            System.err.println("    omitunknowntags = true | false");
            System.err.println("    omitdeprtags = true | false");
            System.err.println("    omitcomments = true | false");
            System.err.println("    omitxmldecl = true | false");
            System.err.println("    omitdoctypedecl = true | false");
            System.err.println("    omitxmlnsatt = true | false");
            System.err.println("    hyphenreplacement = <string value>");
            System.exit(1);
        }

        String inCharset = getArgValue(args, "incharset");
        if ("".equals(inCharset)) {
            inCharset = HtmlCleaner.DEFAULT_CHARSET;
        }

        String outCharset = getArgValue(args, "outcharset");
        if ("".equals(outCharset)) {
            outCharset = HtmlCleaner.DEFAULT_CHARSET;
        }

        String destination = getArgValue(args, "dest");
        String outputType = getArgValue(args, "outputtype");
        String advancedXmlEscape = getArgValue(args, "advancedxmlescape");
        String useCData = getArgValue(args, "usecdata");
        String translateSpecialEntities = getArgValue(args, "specialentities");
        String unicodeChars = getArgValue(args, "unicodechars");
        String omitUnknownTags = getArgValue(args, "omitunknowntags");
        String omitDeprecatedTags = getArgValue(args, "omitdeprtags");
        String omitComments = getArgValue(args, "omitcomments");
        String omitXmlDeclaration = getArgValue(args, "omitxmldecl");
        String omitDoctypeDeclaration = getArgValue(args, "omitdoctypedecl");
        String omitXmlnsAttributes = getArgValue(args, "omitxmlnsatt");
        String commentHyphen = getArgValue(args, "hyphenreplacement");

        HtmlCleaner cleaner = null;

        String src = source.toLowerCase();
        if ( src.startsWith("http://") || src.startsWith("https://") ) {
            cleaner = new HtmlCleaner(new URL(src), inCharset);
        } else {
            cleaner = new HtmlCleaner(new File(src), inCharset);
        }

        if ( !"".equals(omitUnknownTags) ) {
            cleaner.setOmitUnknownTags( toBoolean(omitUnknownTags) );
        }

        if ( !"".equals(omitDeprecatedTags) ) {
            cleaner.setOmitDeprecatedTags( toBoolean(omitDeprecatedTags) );
        }

        if ( !"".equals(advancedXmlEscape) ) {
            cleaner.setAdvancedXmlEscape( toBoolean(advancedXmlEscape) );
        }

        if ( !"".equals(useCData) ) {
            cleaner.setUseCdataForScriptAndStyle( toBoolean(useCData) );
        }

        if ( !"".equals(translateSpecialEntities) ) {
            cleaner.setTranslateSpecialEntities( toBoolean(translateSpecialEntities) );
        }

        if ( !"".equals(unicodeChars) ) {
            cleaner.setRecognizeUnicodeChars( toBoolean(unicodeChars) );
        }

        if ( !"".equals(omitComments) ) {
            cleaner.setOmitComments( toBoolean(omitComments) );
        }

        if ( !"".equals(omitXmlDeclaration) ) {
            cleaner.setOmitXmlDeclaration( toBoolean(omitXmlDeclaration) );
        }
        
        if ( !"".equals(omitDoctypeDeclaration) ) {
        	cleaner.setOmitDoctypeDeclaration( toBoolean(omitDoctypeDeclaration) );
        }
        
        if ( !"".equals(omitXmlnsAttributes) ) {
        	cleaner.setOmitXmlnsAttributes( toBoolean(omitXmlnsAttributes) );
        }

        if ( !"".equals(commentHyphen) ) {
            cleaner.setHyphenReplacementInComment(commentHyphen);
        }

        cleaner.clean();

        if ( "".equals(destination) ) {
            if ( "compact".equals(outputType) ) {
                cleaner.writeCompactXmlToStream(System.out, outCharset);
            } else if ( "pretty".equals(outputType) ) {
                cleaner.writePrettyXmlToStream(System.out, outCharset);
            } else {
                cleaner.writeXmlToStream(System.out, outCharset);
            }
        } else {
            if ( "compact".equals(outputType) ) {
                cleaner.writeCompactXmlToFile(destination, outCharset);
            } else if ( "pretty".equals(outputType) ) {
                cleaner.writePrettyXmlToFile(destination, outCharset);
            } else {
                cleaner.writeXmlToFile(destination, outCharset);
            }
        }
    }

}