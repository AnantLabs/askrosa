package resource;

import java.util.ResourceBundle;

public class WebSetting
{

    public static final ResourceBundle SETTINGS;
    static
    {
	SETTINGS = ResourceBundle.getBundle("cn.askrosa.struts.ApplicationResources");
    }

    public static String getProperty(String key)
    {
	return SETTINGS.getString(key).trim();
    }

    public static void main(String[] args)
    {
	System.out.println(CrawlerSetting.getProperty("searcher.searchparameter.sort.default"));
    }

}
