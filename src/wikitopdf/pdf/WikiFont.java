/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikitopdf.pdf;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiFont {

    /**
     *
     * @return
     */
    public int[] getFontColor() {
        return fontColor;
    }

    /**
     *
     * @param fontColor
     */
    public void setFontColor(int[] fontColor) {
        this.fontColor = fontColor;
    }

    /**
     *
     * @return
     */
    public String getFontFamily() {
        return fontFamily;
    }

    /**
     *
     * @param fontFamily
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    /**
     *
     * @return
     */
    public float getFontSize() {
        return fontSize;
    }

    /**
     *
     * @param fontSize
     */
    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }
    private String fontFamily = "";
    private int[] fontColor = {0, 0, 0};
    private float fontSize = 10;
}
