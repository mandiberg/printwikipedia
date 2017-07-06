/*
 */

package wikitopdf.html;


import com.lowagie.text.Chunk;
import com.lowagie.text.DocListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.html.Markup;
import com.lowagie.text.html.simpleparser.ALink;
import com.lowagie.text.html.simpleparser.ChainedProperties;
import com.lowagie.text.html.simpleparser.FactoryProperties;
import com.lowagie.text.html.simpleparser.ImageProvider;
import com.lowagie.text.html.simpleparser.Img;
import com.lowagie.text.html.simpleparser.IncCell;
import com.lowagie.text.html.simpleparser.IncTable;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.xml.simpleparser.SimpleXMLDocHandler;
import com.lowagie.text.xml.simpleparser.SimpleXMLParser;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import wikitopdf.pdf.PdfPageWrapper;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WHTMLWorker implements SimpleXMLDocHandler, DocListener {

    /**
     *
     */
    protected ArrayList objectList;

    /**
     *
     */
    private DocListener document;
    public PdfWriter writer;

	private Paragraph currentParagraph;

	private ChainedProperties cprops = new ChainedProperties();

	private Stack stack = new Stack();

	private boolean pendingTR = false;

	private boolean pendingTD = false;

	private boolean pendingLI = false;
        private boolean pendingOL = false;
        private  boolean is_cell = false;

	private StyleSheet style = new StyleSheet();

	private boolean isPRE = false;
        private boolean isH3 = false;
        private boolean isTD = false;
        private boolean isLI = false;

	private Stack tableState = new Stack();

	private boolean skipText = false;

	private HashMap interfaceProps;
        
        private FontSelector whtmlfs = new FontSelector();
        private FontSelector whtmlprefs = new FontSelector();
        private ArrayList whtmlas = new ArrayList();

	private FactoryProperties factoryProperties = new FactoryProperties();
        
	/** Creates a new instance of WHTMLWorker
	 * @param document A class that implements <CODE>DocListener</CODE>
	 * */
	public WHTMLWorker(DocListener document, PdfWriter pdfWriter) {
		this.document = document;
                this.writer = pdfWriter;
	}

        /**
         *
         * @param style
         */
        public void setStyleSheet(StyleSheet style) {
		this.style = style;
	}

        /**
         *
         * @return
         */
        public StyleSheet getStyleSheet() {
		return style;
	}

        /**
         *
         * @param interfaceProps
         */
        public void setInterfaceProps(HashMap interfaceProps) {
		this.interfaceProps = interfaceProps;
		FontFactoryImp ff = null;
		if (interfaceProps != null)
			ff = (FontFactoryImp) interfaceProps.get("font_factory");
		if (ff != null)
			factoryProperties.setFontImp(ff);
	}
        /**
         *
         * @return
         */
        public Phrase createPhrase(String str, ChainedProperties cprops){
            Phrase ph = whtmlfs.process(str);
            ph.setHyphenation(factoryProperties.getHyphenation(cprops));
            return ph;
        }

        /**
         *
         * @return
         */
        public HashMap getInterfaceProps() {
		return interfaceProps;
	}

        /**
         *
         * @param reader
         * @throws IOException
         */
        public void parse(Reader reader) throws IOException {
		SimpleXMLParser.parse(this, null, reader, true);
	}

        /**
         *
         * @param reader
         * @param style
         * @return
         * @throws IOException
         */
        public static ArrayList parseToList(Reader reader, StyleSheet style, PdfWriter pdfWriter)
			throws IOException {
		return parseToList(reader, style, null,pdfWriter);
	}

        /**
         *
         * @param reader
         * @param style
         * @param interfaceProps
         * @return
         * @throws IOException
         */
        public static ArrayList parseToList(Reader reader, StyleSheet style,
                                            HashMap interfaceProps, PdfWriter pdfWriter) throws IOException {
                
		WHTMLWorker worker = new WHTMLWorker(null, pdfWriter);
		if (style != null)
			worker.style = style;
		worker.document = worker;
		worker.setInterfaceProps(interfaceProps);
		worker.objectList = new ArrayList();
		worker.parse(reader);
		return worker.objectList;
	}

	public void endDocument() {
		try {
			for (int k = 0; k < stack.size(); ++k){
				document.add((Element) stack.elementAt(k));
                        }
			if (currentParagraph != null){
                            document.add(currentParagraph);
                        }
			currentParagraph = null;
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	public void startDocument() {
		HashMap h = new HashMap();
		style.applyStyle("body", h);
		cprops.addToChain("body", h);
	}

    @Override
	public void startElement(String tag, HashMap h) {              
		if (!tagsSupported.containsKey(tag))
			return;
		try {
                    whtmlfs = PdfPageWrapper.fs;
                    whtmlas = PdfPageWrapper.as;
                    whtmlprefs = PdfPageWrapper.pfs;
                    BaseFont bsCardo = BaseFont.createFont("fonts/Cardo_no_hebrew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font cardo = new Font(bsCardo);
                    cardo.setSize(10f);
			style.applyStyle(tag, h);
			String follow = (String) FactoryProperties.followTags.get(tag);
                        
			if (follow != null) {
				HashMap prop = new HashMap();
				prop.put(follow, null);
				cprops.addToChain(follow, prop);
				return;
			}
			FactoryProperties.insertStyle(h, cprops);
			if (tag.equals(HtmlTags.ANCHOR)) {
				cprops.addToChain(tag, h);
				if (currentParagraph == null) {
					currentParagraph = new Paragraph();
				}
				stack.push(currentParagraph);
				currentParagraph = new Paragraph();
				return;
			}
			if (tag.equals(HtmlTags.NEWLINE)) {
				if (currentParagraph == null) {
					currentParagraph = new Paragraph();
				}
                                currentParagraph.add(new Chunk("\n",cardo));
				return;
			}

			if (tag.equals(HtmlTags.CHUNK) || tag.equals(HtmlTags.SPAN)) {
				cprops.addToChain(tag, h);
				return;
			}
//			if (tag.equals(HtmlTags.IMAGE)) {
//				String src = (String) h.get(ElementTags.SRC);
//				if (src == null)
//					return;
//				cprops.addToChain(tag, h);
//				Image img = null;
//				if (interfaceProps != null) {
//					ImageProvider ip = (ImageProvider) interfaceProps
//							.get("img_provider");
//					if (ip != null)
//						img = ip.getImage(src, h, cprops, document);
//					if (img == null) {
//						HashMap images = (HashMap) interfaceProps
//								.get("img_static");
//						if (images != null) {
//							Image tim = (Image) images.get(src);
//							if (tim != null)
//								img = Image.getInstance(tim);
//						} else {
//							if (!src.startsWith("http")) { // relative src references only
//								String baseurl = (String) interfaceProps
//										.get("img_baseurl");
//								if (baseurl != null) {
//                                                                    src = baseurl + src;
//                                                                    img = Image.getInstance(src);
//								}
//							}
//						}
//					}
//				}
//				if (img == null) {
//					if (!src.startsWith("http")) {
//						String path = cprops.getProperty("image_path");
//						if (path == null)
//							path = "";
//						src = new File(path, src).getPath();
//					}
//					img = Image.getInstance(src);
//				}
//				String align = (String) h.get("align");
//				String width = (String) h.get("width");
//				String height = (String) h.get("height");
//				String before = cprops.getProperty("before");
//				String after = cprops.getProperty("after");
//				if (before != null)
//					img.setSpacingBefore(Float.parseFloat(before));
//				if (after != null)
//					img.setSpacingAfter(Float.parseFloat(after));
//				float actualFontSize = Markup.parseLength(cprops
//						.getProperty(ElementTags.SIZE),
//						Markup.DEFAULT_FONT_SIZE);
//				if (actualFontSize <= 0f)
//					actualFontSize = Markup.DEFAULT_FONT_SIZE;
//				float widthInPoints = Markup.parseLength(width, actualFontSize);
//				float heightInPoints = Markup.parseLength(height,
//						actualFontSize);
//				if (widthInPoints > 0 && heightInPoints > 0) {
//					img.scaleAbsolute(widthInPoints, heightInPoints);
//				} else if (widthInPoints > 0) {
//					heightInPoints = img.getHeight() * widthInPoints
//							/ img.getWidth();
//					img.scaleAbsolute(widthInPoints, heightInPoints);
//				} else if (heightInPoints > 0) {
//					widthInPoints = img.getWidth() * heightInPoints
//							/ img.getHeight();
//					img.scaleAbsolute(widthInPoints, heightInPoints);
//				}
//				img.setWidthPercentage(0);
//				if (align != null) {
//					endElement("p");
//					int ralign = Image.MIDDLE;
//					if (align.equalsIgnoreCase("left"))
//						ralign = Image.LEFT;
//					else if (align.equalsIgnoreCase("right"))
//						ralign = Image.RIGHT;
//					img.setAlignment(ralign);
//					Img i = null;
//					boolean skip = false;
//					if (interfaceProps != null) {
//						i = (Img) interfaceProps.get("img_interface");
//						if (i != null)
//							skip = i.process(img, h, cprops, document);
//					}
//					if (!skip)
//						document.add(img);
//					cprops.removeChain(tag);
//				} else {
//					cprops.removeChain(tag);
//					if (currentParagraph == null) {
//						currentParagraph = FactoryProperties
//								.createParagraph(cprops);
//					}
//					currentParagraph.add(new Chunk(img, 0, 0));
//				}
//				return;
//			}
//			endElement("p");
			if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3") 
					|| tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {
                            if(tag.equals("h3") || tag.equals("H3")){
                                isH3 = true;
                            }
				if (!h.containsKey(ElementTags.SIZE)) {
					int v = 7 - Integer.parseInt( tag.substring(1));
//                                    int v = 10;
					h.put(ElementTags.SIZE, Integer.toString(v));                                        
				}
                            String s_space = "\n";
                            Phrase space = whtmlfs.process(s_space);
                            space.setLeading(4f);//add spacing before the Htags.
                            document.add(space);
                            cprops.addToChain(tag, h);

                                return;
			}
			if (tag.equals(HtmlTags.UNORDEREDLIST)) {
//                            System.out.println("what is pending li in this UL?  " + pendingLI);
				if (pendingLI)
					endElement(HtmlTags.LISTITEM);
				skipText = true;

				cprops.addToChain(tag, h);
				com.lowagie.text.List list = new com.lowagie.text.List(false);
				try{
					list.setIndentationLeft(new Float(cprops.getProperty("indent")).floatValue());
                                        
				}catch (Exception e) {
					list.setAutoindent(false);
                                        list.setSymbolIndent(7f);
				}
                                Chunk lSymbol = new Chunk("\u2022",cardo);
				list.setListSymbol(lSymbol);
				stack.push(list);
				return;
			}
			if (tag.equals(HtmlTags.ORDEREDLIST)) {
//                            System.out.println("what is pending li in this OL?  " + pendingLI);
                            //start of a new ordered list. make sure that this ordered list is not in the middle of an open LI.
                            //new bool for being in open ordered list. 
                            //if pendingOL is true and so is pendingLI then close LI before opening OL.
                            pendingOL = true;
                            if (pendingLI){
                                endElement(HtmlTags.LISTITEM);
                            }
                                    
                            skipText = true;

                            cprops.addToChain(tag, h);
                            com.lowagie.text.List list = new com.lowagie.text.List(true);
                            try{
                                    list.setIndentationLeft(new Float(cprops.getProperty("indent")).floatValue());
                            }catch (Exception e) {
                                    list.setAutoindent(false);
                                    list.setSymbolIndent(5f);
                            }
                            Chunk lSymbol = new Chunk("", cardo);
                            cardo.setSize(8f);
                            list.setListSymbol(lSymbol);
                            list.setSymbolIndent(12f);
                            stack.push(list);
                            return;
			}
			if (tag.equals(HtmlTags.LISTITEM)) {
                            if (pendingLI)
                                    endElement(HtmlTags.LISTITEM);
                            skipText = false;
                            pendingLI = true;

                            cprops.addToChain(tag, h);
                            ListItem item = FactoryProperties.createListItem(cprops);
                            stack.push(item);
                            return;
			}
			if (tag.equals(HtmlTags.DIV) || tag.equals(HtmlTags.BODY) || tag.equals("p")) {
				cprops.addToChain(tag, h);
				return;
			}
			if (tag.equals(HtmlTags.PRE)) {
				if (!h.containsKey(ElementTags.FACE)) {
					h.put(ElementTags.FACE, "Courier");
				}
				cprops.addToChain(tag, h);
				isPRE = true;
				return;
			}
			if (tag.equals("tr")) {
				if (pendingTR)
					endElement("tr");
				skipText = true;
				pendingTR = true;
				cprops.addToChain("tr", h);
				return;
			}
			if (tag.equals("td") || tag.equals("th")) {
				if (pendingTD)
					endElement(tag);
				skipText = false;
				pendingTD = true;
				cprops.addToChain("td", h);
                                IncCell k = new IncCell(tag, cprops);
                                PdfPCell x = k.getCell();
				stack.push(new IncCell(tag, cprops));
				return;
			}
			if (tag.equals("table")) {
                            isTD = true;
				cprops.addToChain("table", h);
				IncTable table = new IncTable(h);
                                
				stack.push(table);
				tableState.push(new boolean[] { pendingTR, pendingTD });
				pendingTR = pendingTD = false;
				skipText = true;
				return;
			}

		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	public void endElement(String tag) {
//            System.out.println(tag);
            
		if (!tagsSupported.containsKey(tag))
			return;
		try {
			String follow = (String) FactoryProperties.followTags.get(tag);
			if (follow != null) {
				cprops.removeChain(follow);
				return;
			}
			if (tag.equals("font") || tag.equals("span")) {
				cprops.removeChain(tag);
				return;
			}
			if (tag.equals("a")) {
				if (currentParagraph == null) {
					currentParagraph = new Paragraph();
				}
				boolean skip = false;
				if (interfaceProps != null) {
					ALink i = (ALink) interfaceProps.get("alink_interface");
					if (i != null)
						skip = i.process(currentParagraph, cprops);
				}
				if (!skip) {
					String href = cprops.getProperty("href");
				}
				Paragraph tmp = (Paragraph) stack.pop();
                                tmp.setLeading(12f);
				Phrase tmp2 = new Phrase();//what's going on here?
				tmp2.add(currentParagraph);
				tmp.add(tmp2);//^^formats whole paragraph....
                                
				currentParagraph = tmp;
				cprops.removeChain("a");
				return;
			}
			if (tag.equals("br")) {
				return;
			}
			if (currentParagraph != null) {
				if (stack.empty())
					document.add(currentParagraph);
				else {
					Object obj = stack.pop();
					if (obj instanceof TextElementArray) {
						TextElementArray current = (TextElementArray) obj;
						current.add(currentParagraph);
					}
					stack.push(obj);
				}
			}
			currentParagraph = null;
			if (tag.equals(HtmlTags.UNORDEREDLIST)|| tag.equals(HtmlTags.ORDEREDLIST)) {
				if (pendingLI)
					endElement(HtmlTags.LISTITEM);
				skipText = false;
				cprops.removeChain(tag);
				if (stack.empty()){//here i should add on the last ul.
                                    return;
                                }
				Object obj = stack.pop();
				if (!(obj instanceof com.lowagie.text.List)) {
//                                    System.out.println("this is a lowagie list");
					stack.push(obj);
					return;
				}
				if (stack.empty()){
					document.add((Element) obj);
                                        if(obj instanceof com.lowagie.text.List){
//                                            System.out.println("i am dumpin this list in there");
                                            com.lowagie.text.List l = (com.lowagie.text.List) obj;

                                        }
                                        BaseFont bsCardo = BaseFont.createFont("fonts/Cardo_no_hebrew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                                        Font cardo = new Font(bsCardo);
                                        cardo.setSize(10f);
                                        document.add((Element) new Chunk("\n",cardo));
                                }
				else
					((TextElementArray) stack.peek()).add(obj);
				return;
			}
			if (tag.equals(HtmlTags.LISTITEM)) {
//                                System.out.println("I AM A LIST ITEM HERE");
				pendingLI = false;
				skipText = true;
				cprops.removeChain(tag);
				if (stack.empty()){
                                    System.out.println("stack is empty");
                                    return;    
                                }
				
				Object obj = stack.pop();
                                
                                
				if (!(obj instanceof ListItem)) {
					stack.push(obj);
					return;
				}
				if (stack.empty()) {
                                    System.out.println("this doesn't make sense becuase how can you be empty and then add an item.?");
					document.add((Element) obj);
					return;
				}
				Object list = stack.pop();
				if (!(list instanceof com.lowagie.text.List)) {
                                    //what is this?
					stack.push(list);
					return;
				}
				ListItem item = (ListItem) obj;

				ArrayList cks = item.getChunks();
                                Phrase lph = new Phrase();
                                Phrase tempph = new Phrase();
                                for(int i = 0; i < (cks.size()-1);i++){
                                    String tmp = cks.get(i).toString();
                                    PdfPTable pp = null;
                                    
                                    if(isTD)
                                        tempph = whtmlprefs.process(tmp);
                                    else
                                        tempph = whtmlfs.process(tmp);
                                    
                                    if( isRTL(whtmlas,tempph) ) {
                                        System.out.println("yes i am rtl");
                                        pp = arabicText(tempph,writer);
                                    }
                                    if(pp!=null)
                                        lph.add(pp);
                                    else
                                        lph.add(tempph);//add temp phrase to larger phrase to set leading outside and add to listitem

                                }
                                
                                lph.setLeading(-3f);
                                
                                ListItem li = new ListItem();
                                li.add(lph);
                                li.setLeading(10f);
                                ((com.lowagie.text.List) list).add(li);
                                
				stack.push(list);
				return;
			}
			if (tag.equals("div") || tag.equals("body")) {
				cprops.removeChain(tag);
				return;
			}
			if (tag.equals(HtmlTags.PRE)) {
				cprops.removeChain(tag);
				isPRE = false;
				return;
			}
			if (tag.equals("p")) {
				cprops.removeChain(tag);
				return;
			}
			if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3")
					|| tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {
                            if(tag.equals("h3")||tag.equals("H3")){
                                isH3 = false;
                            }
                            cprops.removeChain(tag);
				return;
			}
			if (tag.equals("table")) {
                            if (pendingTR)
                                    endElement("tr");
                            isTD = false;
                            cprops.removeChain("table");
                            IncTable table = (IncTable) stack.pop();
                            
                            PdfPTable tb = table.buildTable();
                            tb.setTotalWidth(102);
                            tb.setWidthPercentage(100);
                            tb.setSpacingBefore(20f);
                            tb.setSplitRows(true);
                            if (stack.empty()){
                                BaseFont bsCardo = BaseFont.createFont("fonts/Cardo_no_hebrew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                                Font cardo = new Font(bsCardo);
                                cardo.setSize(10f);
                                document.add(new Phrase("\n",cardo));
                                document.add(tb);
                            }
                            else{
                             TextElementArray tea = ((TextElementArray) stack.peek());
                             ArrayList x = tea.getChunks();
                             tea.add(tb);
                            }
                            boolean state[] = (boolean[]) tableState.pop();
                            pendingTR = state[0];
                            pendingTD = state[1];
                            skipText = false;
                            return;
			}
			if (tag.equals("tr")) {
                           
				if (pendingTD)
					endElement("td");
				pendingTR = false;
				cprops.removeChain("tr");
				ArrayList cells = new ArrayList();
				IncTable table = null;
				while (true) {
					Object obj = stack.pop();
					if (obj instanceof IncCell) { 
                                            IncCell cur_cell = (IncCell) obj;
                                            PdfPCell p_cell = cur_cell.getCell();
                                            p_cell.setBorderWidth(.5f);
                                            p_cell.setBorderColor(Color.BLACK);
                                            is_cell = true;
                                            cells.add(p_cell);
					}
					if (obj instanceof IncTable) {
						table = (IncTable) obj;
						break;
					}
                                }
				table.addCols(cells);
				table.endRow();
				stack.push(table);
				skipText = true;
				return;
			}
			if (tag.equals("td") || tag.equals("th")) {
				pendingTD = false;
				cprops.removeChain("td");
				skipText = true;
				return;
			}
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

    @Override
	public void text(String str) {
		if (skipText)
			return;
		String content = str;
		if (content.trim().length() == 0 && content.indexOf(' ') < 0) {
			return;
		}

		StringBuffer buf = new StringBuffer();
		int len = content.length();
		char character;
		boolean newline = false;
		for (int i = 0; i < len; i++) {
			switch (character = content.charAt(i)) {
			case ' ':
				if (!newline) {
					buf.append(character);
				}
				break;
			case '\n':
				if (i > 0) {
					newline = true;
					buf.append(' ');
				}
				break;
			case '\r':
				break;
			case '\t':
				break;
			default:
				newline = false;
				buf.append(character);
			}
		}
		if (currentParagraph == null) {
			currentParagraph = FactoryProperties.createParagraph(cprops);
		}
                if (isPRE) {
                    Chunk chunk = factoryProperties.createChunk(buf.toString(), cprops);
                    Phrase ph = whtmlprefs.process(buf.toString());
                    ph.setLeading(4f);
                    PdfPTable pp = null;
//                    if( isRTL(whtmlas,ph) ) {
//                        System.out.println("yes i am rtl");
//                        pp = arabicText(ph,writer);
//                    }
//                    if(pp!=null)
//                        currentParagraph.add(pp);
//                    else
                        currentParagraph.add(ph);
                    isPRE = false;
//			return;
		}
                else if(isH3){
                    Chunk chunk = factoryProperties.createChunk(buf.toString(), cprops);
                    Phrase ph = whtmlprefs.process(buf.toString());
//                    ph.setLeading(4f);
                    PdfPTable pp = null;
//                    if( isRTL(whtmlas,ph) ) {
//                        System.out.println("yes i am rtl");
//                        pp = arabicText(ph,writer);
//                    }
//                    if(pp!=null)
//                        currentParagraph.add(pp);
//                    else
                        currentParagraph.add(ph);
                    isH3 = false;
                }
                else if(isTD){
                    Chunk chunk = factoryProperties.createChunk(buf.toString(), cprops);
                    Phrase ph = whtmlprefs.process(buf.toString());
                    ph.setLeading(4f);
                    PdfPTable pp = null;
//                    if( isRTL(whtmlas,ph) ) {
//                        System.out.println("yes i am rtl");
//                        pp = arabicText(ph,writer);
//                    }
//                    if(pp!=null)
//                        currentParagraph.add(pp);
//                    else
                        currentParagraph.add(ph);
//                    isTD = false;
                }
                else{
                    Chunk chunk = factoryProperties.createChunk(buf.toString(), cprops);
                    
                
                Phrase ph = whtmlfs.process(buf.toString());
                PdfPTable pp = null;
//                if( isRTL(whtmlas,ph) ) {
//                    System.err.println("yes i am rtl");
//                    System.err.println("soup ");
//                    ph = processToArabic(ph,writer, whtmlas);
//                      pp = arabicText(ph,writer);
//                }
//                if(pp!=null){
//                    System.err.println(ph.toString());
//                    System.err.println("this was arabic");
//                        try {
//                            document.add(pp);
//                        } catch (DocumentException ex) {
//                            System.err.println(ex);
//                        }
//                }
//                else
                    currentParagraph.add(ph);
                }
		
	}
        public boolean isRTL(ArrayList as, Phrase ph){
        ArrayList chunks = ph.getChunks();
                for(int i=0; i < chunks.size(); i++){
                    Chunk lilchunk = (Chunk) chunks.get(i);
//                    System.out.println("begin lil chunk " + lilchunk.toString() + " end lil chunk");
                    if(as.contains(lilchunk.getFont())){
                        return true;
                        
                    }
                }
        
        return false;
    }
        
    public Phrase processToArabic(Phrase ph, PdfWriter pdfWriter, ArrayList as) {
        Phrase arab_phrase = new Phrase();
        
        ArrayList chunks = ph.getChunks();
        for(int i=0; i < chunks.size(); i++){
            Chunk lilchunk = (Chunk) chunks.get(i);
                    
                    if(as.contains(lilchunk.getFont())){

                        arab_phrase.add("\u202B");
                        arab_phrase.add(lilchunk);
                        arab_phrase.add("\u200F"+"\u202C");
//                        arab_phrase.add();
                        continue;                        
                    }
                     
            arab_phrase.add(lilchunk);
        }
        System.out.println("here is my arab phrase yo.");
        System.out.println(arab_phrase);
        return arab_phrase;
    }
        public PdfPTable arabicText(Phrase ph, PdfWriter pdfWriter){
            Paragraph pr = new Paragraph(ph);
            PdfPTable table = new PdfPTable(1);
            PdfPCell celly = new PdfPCell();
            celly.addElement(pr);
            celly.setBorderColor(Color.white);
            celly.setBorderWidth(0);
            celly.setPadding(0);
            celly.setRunDirection(pdfWriter.RUN_DIRECTION_RTL);
            table.setSpacingAfter(0);
            table.setSpacingBefore(0);
            table.addCell(celly);  
            table.completeRow();
            System.out.println("yes you are arabic.");
            return table;
        }

	public boolean add(Element element) throws DocumentException {
		objectList.add(element);
		return true;
	}

        /**
         *
         * @throws DocumentException
         */
        public void clearTextWrap() throws DocumentException {
	}

	public void close() {
	}

	public boolean newPage() {
		return true;
	}

	public void open() {
	}

	public void resetFooter() {
	}

	public void resetHeader() {
	}

	public void resetPageCount() {
	}

	public void setFooter(HeaderFooter footer) {
	}

	public void setHeader(HeaderFooter header) {
	}

	public boolean setMarginMirroring(boolean marginMirroring) {
		return false;
	}

	/**
         * @param marginMirroring
         * @return
         * @see com.lowagie.text.DocListener#setMarginMirroring(boolean)
	 * @since	2.1.6
	 */
	public boolean setMarginMirroringTopBottom(boolean marginMirroring) {
		return false;
	}

	public boolean setMargins(float marginLeft, float marginRight,
			float marginTop, float marginBottom) {
		return true;
	}

	public void setPageCount(int pageN) {
	}

	public boolean setPageSize(Rectangle pageSize) {
		return true;
	}

        /**
         *
         */
        public static final String tagsSupportedString = "ol ul li a pre span br p div body i u sub sup em strong s strike"
			+ " h1 h2 h3 h4 h5 h6"
                        + " table td tr th dd dl";
                        //+ " b font table tr td th";

        /**
         *
         */
        public static final HashMap tagsSupported = new HashMap();

	static {
		StringTokenizer tok = new StringTokenizer(tagsSupportedString);
		while (tok.hasMoreTokens())
			tagsSupported.put(tok.nextToken(), null);
	}
        
}
