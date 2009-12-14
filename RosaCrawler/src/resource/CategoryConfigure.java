package resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;

/**
 * 读取分类配置信息的类
 * @author elegate
 *
 */
public class CategoryConfigure
{
    public static final Properties CATEGORY_CONFIGURE;
    static
    {
	CATEGORY_CONFIGURE = init(CategoryConfigure.class.getResource("/resource/categories.xml"));
    }

    protected static Properties init(URL url)
    {
	try
	{
	    Properties p = new Properties();
	    p.loadFromXML(new FileInputStream(new File(url.toURI())));
	    return p;
	}
	catch (InvalidPropertiesFormatException e)
	{
	    e.printStackTrace();
	}
	catch (FileNotFoundException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (URISyntaxException e)
	{
	    e.printStackTrace();
	}
	return null;
    }

    public static String getCategory(String key)
    {
	return CATEGORY_CONFIGURE.getProperty(key);
    }

    public static Set<Object> getAllCategories()
    {
	return CATEGORY_CONFIGURE.keySet();
    }

}
