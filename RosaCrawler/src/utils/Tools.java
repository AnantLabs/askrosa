package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import resource.CrawlerSetting;

import crawlerutils.RosaCrawlerConstants;

public final class Tools
{
    /**
     * size format string
     */
    public static final String FORMAT_SIZE_STRING = "%7.2f%3s";

    public static final DateFormat DEFAULT_LOCAL_DATE_FORMAT = DateFormat.getDateTimeInstance();

    public static final char COPYRIGHT_SYMBOL = 0xA9;

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static boolean DEBUG = false;

    /**
     * @param path
     */
    public static void delDir(String path)
    {
	File dir = new File(path);
	if (dir.exists())
	{
	    File[] tmp = dir.listFiles();
	    for (int i = 0; i < tmp.length; i++)
	    {
		if (tmp[i].isDirectory())
		{
		    delDir(path + "/" + tmp[i].getName());
		}
		else
		{
		    tmp[i].delete();
		}
	    }
	    dir.delete();
	}
    }

    /**
     * @param sourcedir
     * @param destdir
     * @throws Exception
     */
    public static void copyDir(String sourcedir, String destdir) throws Exception
    {

	File source = new File(sourcedir);
	if (!source.exists())
	{
	    return;
	}
	String[] files = source.list();
	delDir(destdir);
	File dest = new File(destdir);
	try
	{
	    dest.mkdirs();
	}
	catch (Exception ex)
	{
	    throw new Exception("CopyDir:" + ex.getMessage());
	}
	for (int i = 0; i < files.length; i++)
	{
	    String sourcefile = source + File.separator + files[i];
	    String destfile = dest + File.separator + files[i];
	    File temp = new File(sourcefile);
	    if (temp.isFile())
	    {
		copy(sourcefile, destfile);
	    }
	    else
	    {
		copyDir(sourcefile, destfile);
	    }
	}

    }

    /**
     * @param input
     * @param output
     * @return true if successfully copied,false otherwise
     * @throws Exception
     */
    public static boolean copy(String input, String output) throws Exception
    {
	int BUFSIZE = 65536;
	try
	{
	    FileInputStream fis = new FileInputStream(input);
	    FileOutputStream fos = new FileOutputStream(output);
	    int s;
	    byte[] buf = new byte[BUFSIZE];
	    while ((s = fis.read(buf)) > -1)
	    {
		fos.write(buf, 0, s);
	    }
	    fis.close();
	    fos.close();
	}
	catch (Exception ex)
	{
	    throw new Exception("makehome" + ex.getMessage());
	}
	return true;
    }

    /**
     * convert file size to user friendly string
     * 
     * @return user friendly string of file size
     */
    public static String getSizeInString(long size)
    {
	double s = size;
	if (s > RosaCrawlerConstants.GB)
	{
	    return String.format(FORMAT_SIZE_STRING, s / RosaCrawlerConstants.GB, "GB");
	}
	else if (s > RosaCrawlerConstants.MB)
	{
	    return String.format(FORMAT_SIZE_STRING, s / RosaCrawlerConstants.MB, "MB");
	}
	else if (s > RosaCrawlerConstants.KB)
	{
	    return String.format(FORMAT_SIZE_STRING, s / RosaCrawlerConstants.KB, "KB");
	}
	else
	{
	    return String.format(FORMAT_SIZE_STRING, s, "B");
	}
    }

    /**
     * ��λ���嵽��Ļ�м�
     * 
     * @param win
     *            Window ����
     */
    public static void center(Window win)
    {
	Toolkit tkit = Toolkit.getDefaultToolkit();
	Dimension screenSize = tkit.getScreenSize();
	Dimension windowSize = win.getSize();
	if (windowSize.height > screenSize.height)
	{
	    windowSize.height = screenSize.height;
	}
	if (windowSize.width > screenSize.width)
	{
	    windowSize.width = screenSize.width;
	}
	win.setLocation((screenSize.width - windowSize.width) / 2,
		(screenSize.height - windowSize.height) / 2);
    }

    /**
     * ������<code>title</code>Ϊ�����TitledBorder,������ɫΪ<code>c</code>
     * 
     * @param title
     *            ����
     * @param c
     *            ������ɫ
     * @return TitledBorder����
     * @see BorderFactory
     * @see TitledBorder
     */
    public static TitledBorder getTitledBorder(String title, Color c)
    {
	TitledBorder border = BorderFactory.createTitledBorder(title);
	border.setTitleColor(c);
	return border;
    }

    public static TitledBorder getTitledBorder(String title)
    {
	return BorderFactory.createTitledBorder(title);
    }

    /**
     */
    public static void addComponent(Container c, int gridx, int gridy, int gridWidth,
	    int gridHeight, GridBagConstraints gbCon, Component comp)
    {
	gbCon.insets = new Insets(5, 5, 5, 5);
	gbCon.gridx = gridx;
	gbCon.gridy = gridy;
	gbCon.gridwidth = gridWidth;
	gbCon.gridheight = gridHeight;
	c.add(comp, gbCon);
    }

    public static JTextField getDateInputField(SimpleDateFormat df)
    {
	JFormattedTextField txtField = new JFormattedTextField(df);
	txtField.setColumns(20);
	return txtField;
    }

    public static JFormattedTextField getIntegerFormattedTextField(String format)
    {
	AbstractFormatter formatter = null;
	try
	{
	    formatter = new MaskFormatter(format);
	    MaskFormatter f = ((MaskFormatter) formatter);
	    f.setPlaceholderCharacter('0');
	}
	catch (Exception e)
	{
	    NumberFormat nf = NumberFormat.getIntegerInstance();
	    nf.setGroupingUsed(false);
	    formatter = new NumberFormatter(nf);
	    e.printStackTrace();
	}
	JFormattedTextField txtField = new JFormattedTextField(formatter);
	txtField.setColumns(20);
	return txtField;
    }

    public static JFormattedTextField getIntegerFormattedTextField()
    {
	NumberFormat nf = NumberFormat.getIntegerInstance();
	nf.setGroupingUsed(false);
	JFormattedTextField txtField = new JFormattedTextField(nf);
	txtField.setColumns(20);
	return txtField;
    }

    public static JFormattedTextField getNumberInputField()
    {
	NumberFormat nf = NumberFormat.getNumberInstance();

	nf.setMaximumFractionDigits(10);
	nf.setGroupingUsed(false);
	JFormattedTextField txtField = new JFormattedTextField(nf);
	txtField.setColumns(20);
	return txtField;
    }

    // public static TableCellEditor getIntegerTableCellEditor()
    // {
    // return new DefaultCellEditor(getIntegerFormattedTextField());
    // }
    public static JPanel getFlowLayoutPanel(JComponent c)
    {
	return Tools.getFlowLayoutPanel(c, FlowLayout.LEFT);
    }

    public static JPanel getFlowLayoutPanel(JComponent c, int align)
    {
	JPanel p = new JPanel(new FlowLayout(align, 10, 10));
	p.setOpaque(false);
	p.add(c);
	return p;
    }

    public static String getFileType(String file)
    {
	int index = file.lastIndexOf('.');
	if (index == -1)
	    return null;
	else
	    return file.substring(index + 1, file.length()).toLowerCase();
    }

    public static String getFileName(String file)
    {
	int index = file.lastIndexOf('.');
	if (index == -1)
	    return file;
	else
	    return file.substring(0, index);
    }

    /**
     * Return the extension portion of the file's name .
     * 
     * @see #getExtension
     * @see FileFilter#accept
     */
    public static String getExtension(File f)
    {
	if (f != null)
	{
	    String filename = f.getName();
	    int i = filename.lastIndexOf('.');
	    if (i > 0 && i < filename.length() - 1)
	    {
		return filename.substring(i + 1).toLowerCase();
	    }
	}
	return null;
    }

    public static String changeFileType(String file, String type)
    {
	String name = getFileName(file);
	return name + "." + type;
    }

    public static void copyFile(String src, String dst)
    {
	try
	{
	    FileInputStream in = new FileInputStream(src);
	    FileOutputStream out = new FileOutputStream(dst);
	    byte[] buffer = new byte[8192];
	    while ((in.read(buffer)) != -1)
	    {
		out.write(buffer);
	    }
	    out.flush();
	    out.close();
	    in.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void printArray(Object[] arr)
    {
	for (int i = 0; i < arr.length; i++)
	{
	    System.out.print(arr[i] + " ");
	}
	System.out.println();
    }

    public static void printArray(double[][] arr)
    {
	for (int i = 0; i < arr.length; i++)
	{
	    for (int j = 0; j < arr[0].length; j++)
		System.out.print(arr[i][j] + " ");
	    System.out.println();
	}
    }

    public static void printMap(Map<?, ?> map)
    {
	if (map.size() == 0)
	{
	    System.out.println("Empty map");
	    return;
	}
	for (Object ele : map.keySet())
	{
	    System.out.println(ele + " -> " + map.get(ele));
	}
    }

    public static void printSystemProperties()
    {
	Properties p = System.getProperties();
	printMap(p);
    }

    public static void enableDebug(boolean b)
    {
	DEBUG = b;
    }

    public static void DEBUG(String str)
    {
	if (DEBUG)
	{
	    System.out.println(str);
	}
    }

    public static String readLine()
    {
	String line = null;
	try
	{
	    line = reader.readLine();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return line;
    }

    public static int readInt()
    {
	String input = readLine();
	return Integer.parseInt(input.trim());
    }

    public static double readDouble()
    {
	String input = readLine();
	return Double.parseDouble(input.trim());
    }

    public static Object callMethod(Object obj, String methodName, Class<?>[] parameterTypes,
	    Object... args) throws SecurityException, NoSuchMethodException,
	    IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
	Method m;
	m = obj.getClass().getMethod(methodName, parameterTypes);
	return m.invoke(obj, args);
    }

    public static Object getStaticFieldValue(Class<?> cls, String fieldName)
	    throws SecurityException, NoSuchFieldException, IllegalArgumentException,
	    IllegalAccessException
    {
	Field f = cls.getField(fieldName);
	return f.get(null);
    }

    public static Object getFieldValue(Object obj, String fieldName) throws SecurityException,
	    NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
	Field f = obj.getClass().getField(fieldName);
	return f.get(obj);
    }

    public static void showMethods(Object o)
    {
	Class<?> c = o.getClass();
	Method[] theMethods = c.getMethods();
	for (int i = 0; i < theMethods.length; i++)
	{
	    String methodString = theMethods[i].getName();
	    System.out.println("Name: " + methodString);
	    String returnString = theMethods[i].getReturnType().getName();
	    System.out.println("   Return Type: " + returnString);
	    Class<?>[] parameterTypes = theMethods[i].getParameterTypes();
	    System.out.print("   Parameter Types:");
	    for (int k = 0; k < parameterTypes.length; k++)
	    {
		String parameterString = parameterTypes[k].getName();
		System.out.print(" " + parameterString);
	    }
	    System.out.println();
	}
    }

    public static void sendEmail(String host, boolean ssl, String port, String from, String psw,
	    String fromname, String to, String subject, String msg) throws EmailException
    {
	SimpleEmail email = new SimpleEmail();
	email.setHostName(host);
	email.setSSL(ssl); // 设定是否使用SSL
	email.setSslSmtpPort(port);
	email.setAuthentication(from, psw);// 邮件服务器验证：用户名/密码
	email.setCharset("UTF-8");// 必须放在前面，否则乱码
	email.addTo(to);
	email.setFrom(from, fromname);
	email.setSubject(subject);
	email.setMsg(msg);
	email.send();
    }

    public static void main(String[] args) throws UnsupportedEncodingException, EmailException
    {
	CrawlerSetting.setProxy();
	String host = "smtp.gmail.com";
	String from = "askrosateam@gmail.com";
	String psw = "welovefreedom";
	String to = "ww.wang.cs@gmail.com";
	String fromname = "AskRosa Team";
	String subject = "AskRosa消息通知";
	String msg = "尊敬的用户，您所请求的资源 \"" + "无用" + "\" 已经出现在搜索结果中，下面是对应的链接地址："
		+ "http://askrosa.cn/search.do?keyword=" + URLEncoder.encode("无用", "utf-8")
		+ "\n\n\n\n AskRosaTeam敬上";
	System.out.println(msg);
	try
	{
	    Tools.sendEmail(host, true, "465", from, psw, fromname, to, subject, msg);
	}
	catch (EmailException e)
	{
	    e.printStackTrace();
	}
    }
}
