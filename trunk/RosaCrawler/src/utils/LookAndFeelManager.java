package utils;

import java.awt.Component;
import java.io.PrintStream;
import java.util.HashMap;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * 
 * @author elegate
 */
public class LookAndFeelManager
{
    /**
     * @author elegate
     */
    public enum LookAndFeelType
    {
	/**
	 * Windows look and feel
	 */
	WINDOWS,
	/**
	 * Motif look and feel
	 */
	CDE_MOTIF,
	/**
	 * Metal look and feel,that is java's default look and feel
	 */
	METAL;

	/**
	 * @param type
	 *                LookAndFeelType
	 * @return A string describe the type
	 * @see LookAndFeelType
	 */
	public static String toString(LookAndFeelType type)
	{
	    if (type == LookAndFeelType.WINDOWS)
	    {
		return "Windows";
	    }
	    else if (type == LookAndFeelType.CDE_MOTIF)
	    {
		return "CDE/Motif";
	    }
	    else
		return "Metal";
	}
    }

    /**
     * ����ϵͳ֧�ֵ�LookAndFeel��Ϣ
     */
    private static HashMap<String, UIManager.LookAndFeelInfo> LookAndFeelMap = new HashMap<String, UIManager.LookAndFeelInfo>();

    static
    {
	UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager
		.getInstalledLookAndFeels();
	for (int i = 0; i < lookAndFeelInfos.length; i++)
	{
	    LookAndFeelMap.put(lookAndFeelInfos[i].getName(),
		    lookAndFeelInfos[i]);
	}

    }

    /**
     * private ���캯���û�����ʵ�����Ķ���
     */
    private LookAndFeelManager()
    {

    }

    public static void changeToSystemLookAndFeel(Component c)
    {
	try
	{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    if (c != null)
		SwingUtilities.updateComponentTreeUI(c);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * �ı�������LookAndFeel
     * 
     * @param type
     *                LookAndFeel������
     * @param c
     *                Ҫ�ı�й۵����
     */
    public static void changeLookAndFeel(LookAndFeelType type, Component c)
    {
	try
	{
	    UIManager.LookAndFeelInfo info = LookAndFeelMap.get(LookAndFeelType
		    .toString(type));
	    UIManager.setLookAndFeel(info.getClassName());
	    if (c != null)
		SwingUtilities.updateComponentTreeUI(c);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * 
     * @param lookAndFeelName
     * @param c
     */
    public static void changeLookAndFeel(String lookAndFeelName, Component c)
    {
	try
	{
	    UIManager.LookAndFeelInfo info = LookAndFeelMap
		    .get(lookAndFeelName);
	    UIManager.setLookAndFeel(info.getClassName());
	    if (c != null)
		SwingUtilities.updateComponentTreeUI(c);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * 
     * @param out
     */
    public static void listAllInstalledLookAndFeelNames(PrintStream out)
    {
	for (String e : LookAndFeelMap.keySet())
	{
	    out.println(e + "->" + LookAndFeelMap.get(e));
	}
	out.println(UIManager.getSystemLookAndFeelClassName());
    }

    /**
     * 
     * @param font
     * @see FontUIResource
     */

    public static void setUIFont(FontUIResource font)
    {
	java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
	while (keys.hasMoreElements())
	{
	    Object key = keys.nextElement();
	    Object value = UIManager.get(key);
	    if (value instanceof javax.swing.plaf.FontUIResource)
	    {
		// System.out.println("key="+key+",value="+value);
		UIManager.put(key, font);
	    }
	}
    }
}
