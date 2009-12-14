package gui;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import utils.Tools;
import database.FtpSiteInfo;
import database.Scroller;
/**
 * 显示FTP信息的表格模型
 * @author elegate
 *
 */
public class FtpSitesTableModel extends AbstractTableModel
{

    /**
     * 
     */
    private static final long serialVersionUID = -5275761900083321208L;

    private static String[] COLUMN_FIELDS_NAME =
    { "id", "Server", "Port", "Username", "Password", "UpdateTime",
	    "TotalFileCount", "CrawlInterval" };

    private static final String[] COLUMN_NAMES =
    { "", "Server", "Port", "Username", "Password", "Update",
	    "Total File Count", "Interval" };

    private static final String[] METHOD_NAMES =
    { "", "getServer", "getPort", "getUsername", "getPassword",
	    "getUpdateTime", "getTotalFileCount", "getCrawlInterval" };

    private static final Class<?>[] COLUMN_CLASSES =
    { Integer.class, String.class, Integer.class, String.class, String.class,
	    Timestamp.class, Long.class, Integer.class };

    private List<FtpSiteInfo> ftpSiteInfos = new LinkedList<FtpSiteInfo>();

    
    public int getColumnCount()
    {
	return COLUMN_NAMES.length;
    }

    public String getColumnFieldName(int column)
    {
	return COLUMN_FIELDS_NAME[column];
    }

    public void update(Scroller<FtpSiteInfo> scr)
    {
	ftpSiteInfos.clear();
	while (scr.hasNext())
	{
	    this.ftpSiteInfos.add((FtpSiteInfo) scr.next());
	}
	this.fireTableDataChanged();
    }

    
    public int getRowCount()
    {
	return ftpSiteInfos.size();
    }

    
    public Object getValueAt(int rowIndex, int columnIndex)
    {
	if (columnIndex == 0)
	    return rowIndex + 1;
	FtpSiteInfo ftpSite = (FtpSiteInfo) ftpSiteInfos.get(rowIndex);
	try
	{
	    return Tools.callMethod(ftpSite, METHOD_NAMES[columnIndex],
		    new Class<?>[0], (Object[]) null);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return null;
    }

    public String getColumnName(int column)
    {
	return COLUMN_NAMES[column];
    }

    public Class<?> getColumnClass(int columnIndex)
    {
	return COLUMN_CLASSES[columnIndex];
    }
}
