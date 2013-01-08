package info.bliki.wiki.addon.model;

import info.bliki.wiki.addon.tags.AppletTag;
import info.bliki.wiki.addon.tags.CTeXTag;
import info.bliki.wiki.addon.tags.CalcTag;
import info.bliki.wiki.addon.tags.ChartTag;
import info.bliki.wiki.addon.tags.EvalTag;
import info.bliki.wiki.addon.tags.PlotTag;
import info.bliki.wiki.addon.tags.SampleTag;
import info.bliki.wiki.addon.tags.TeXTag;
import info.bliki.wiki.addon.tags.WCMTag;
import info.bliki.wiki.addon.tags.YacasEvalTag;
import info.bliki.wiki.addon.tags.YacasTag;
import info.bliki.wiki.model.Configuration;

public class AddonConfiguration extends Configuration {

	static {

		TAG_TOKEN_MAP.put("applet", new AppletTag());
		TAG_TOKEN_MAP.put("calc", new CalcTag());
		TAG_TOKEN_MAP.put("chart", new ChartTag());
		TAG_TOKEN_MAP.put("eval", new EvalTag());
		TAG_TOKEN_MAP.put("plot", new PlotTag());
		TAG_TOKEN_MAP.put("sample", new SampleTag());
		TAG_TOKEN_MAP.put("wcm", new WCMTag());
		TAG_TOKEN_MAP.put("yacas", new YacasTag());
		TAG_TOKEN_MAP.put("yacaseval", new YacasEvalTag()); 
		TAG_TOKEN_MAP.put("tex", new TeXTag());
		TAG_TOKEN_MAP.put("ctex", new CTeXTag());
	}

	public static AddonConfiguration DEFAULT_CONFIGURATION = new AddonConfiguration();

	public AddonConfiguration() {
	}

}
