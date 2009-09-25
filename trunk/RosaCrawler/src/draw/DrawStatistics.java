package draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import crawlerutils.RosaCrawlerConstants;

import resource.CrawlerSetting;
import database.Criteria;
import database.QueryStatistics;
import database.QueryStatisticsPeer;
import database.QueryStatisticsResultPeer;
import database.Scroller;
/**
 * 绘制用户量统计的直方图
 * @author elegate
 *
 */
public class DrawStatistics
{
    /**
     * 横轴每个单位长度占据的像素数
     */
    private int horiPixsPerUnit = 20;

    /**
     * 纵轴每个单位长度占据的像素数
     */
    private int vertPixsPerUnit = 10;

    /**
     * 绘图区域四周的留空区域
     */
    public static final int GAP = 50;

    /**
     * 缩放系数
     */
    private double scale = 1;
    /**
     * 水平偏移量
     */
    private int horiShift = 0;
    /**
     * 垂直位移
     */
    private int vertShift = -35;
    /**
     * 控制水平方向每几个网格绘制一次坐标
     */
    private int horiStep = 1;
    /**
     * 控制垂直方向每几个网格绘制一次坐标
     */
    private int vertStep = 5;
    
    /**
     * 横轴单位变换
     */
    private double horiUnitScale = 1.0;
    
    /**
     * 横轴单位起始单位，比如不是从0显示而是从2.0开始显示
     */
    private int horiUnitStart = 0;
    /**
     * 纵轴单位变换
     */
    private double vertUnitScale = CrawlerSetting.getDouble("draw.vertunitscale");
    /**
     * startX,startY就是坐标原点
     */
    private int startX;

    private int startY;
    
    private int endX;
    

    /**
     * 绘图，并将结果存入文件
     * @param graph 文件路径
     */
    public void drawGraph(String graph)
    {
	Criteria c = new Criteria();
	final long HOUR = RosaCrawlerConstants.ONE_HOUR_IN_MILLISECOND;
	final int LEN = (int) (RosaCrawlerConstants.ONE_DAY_IN_MILLISECOND / HOUR);
	int[] hist = new int[LEN];

	long startTime = System.currentTimeMillis()
		- RosaCrawlerConstants.ONE_DAY_IN_MILLISECOND;
	Calendar cal = Calendar.getInstance();
	cal.setTimeInMillis(startTime);
	c.add(QueryStatistics.TIME, QueryStatisticsResultPeer.DATE_FORMAT.format(cal.getTime()), Criteria.GREATER);
	Scroller<QueryStatistics> scr;
	try
	{
	    scr = QueryStatisticsPeer.doSelect(c);
	    while (scr.hasNext())
	    {
		QueryStatistics s = (QueryStatistics) scr.next();
//		System.out.println(QueryStatisticsResultPeer.DATE_FORMAT.format(cal.getTime())+","+QueryStatisticsResultPeer.DATE_FORMAT.format(s.getTime()));
		int index = (int) ((s.getTime().getTime() - startTime) / HOUR);
		hist[index]++;
	    }
	    BufferedImage image = new BufferedImage(600, 800,
		    BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = image.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g.setColor(Color.BLACK);
	  
	    Rectangle2D rect = new Rectangle2D.Float(0, 0, 600, 800);
	    g.setClip(rect);
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, (calendar.get(Calendar.HOUR_OF_DAY)-24)%24 );
	    this.setHoriUnitStart(calendar.get(Calendar.HOUR_OF_DAY));
	    this.setHoriShift(1);
	    Rectangle area = calculateDrawingRect(g);
	    //area.height/vertPixsPerUnit*vertUnitScale<=max(hist)
	   
	    ArrayList<Integer> list = new ArrayList<Integer>();
	    for(int i:hist)
		list.add(i);
	    vertUnitScale = Math.max(Math.ceil(((double)Collections.max(list))/(area.height/vertPixsPerUnit)),CrawlerSetting.getDouble("draw.vertunitscale"));
	    drawCoordinate(g, area, "Time(H)", "Queries");
	    BasicStroke stroke = new BasicStroke(2.0f);
	    g.setStroke(stroke);
	    g.setColor(Color.RED);
	    drawCurve(g,hist);
	    writeImage(image, graph);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void writeImage(BufferedImage image, String file)
	    throws IOException
    {
	java.util.Iterator<ImageWriter> iter = ImageIO
		.getImageWritersByFormatName("PNG");
	ImageWriter writer = iter.next();
	ImageOutputStream imageOut = ImageIO.createImageOutputStream(new File(
		file));
	writer.setOutput(imageOut);
	writer.write(image);
	imageOut.close();
    }

    /**
     * 绘制曲线函数
     * @param g 绘图句柄
     */
    private void drawCurve(Graphics2D g,int[] hist)
    {
        	double baseY = startY - vertPixsPerUnit * scale * vertShift;
        	double width = horiPixsPerUnit*scale;
        	for (int x = (int)(startX + horiShift * horiPixsPerUnit*scale/horiUnitScale), j = 0; x <= endX
        		&& j < hist.length; x += horiPixsPerUnit * scale/horiUnitScale, j++)
        	{
        	    int y = startY -(int)((hist[j] * vertPixsPerUnit * scale / vertUnitScale + vertShift*vertPixsPerUnit*scale)) ;
        	    Rectangle2D rect = new Rectangle2D.Double(x-width,y,width,baseY - y+1);
        	    g.fill(rect);
        	}
    }
    
    private Rectangle calculateDrawingRect(Graphics2D g)
    {
	Rectangle rect = g.getClipBounds();
	
	rect.x += GAP;
	rect.y += GAP;
	rect.width -= 2 * GAP;
	rect.height -= 2 * GAP;
	
	
	double horiUnit = horiPixsPerUnit * scale;
	double vertUnit = vertPixsPerUnit * scale;
	int horiUnits = (int)(rect.width /horiUnit) ;
	int width = (int) (horiUnits * horiUnit);
	int vertUnits = (int)(rect.height/vertUnit)/2*2 ;
	int height = (int)(vertUnits * vertUnit);
	int hgap = rect.width - width;
	int vgap = rect.height - height;
	
	rect.x += hgap/2;
	rect.y += vgap/2;
	rect.width -= hgap;
	rect.height -= vgap;
	return rect;
    }
    
    /**
     * 绘制坐标轴函数
     * @param g 绘图句柄
     * @param xLabel 横轴单位
     * @param yLabel 纵轴单位
     */
    private void drawCoordinate(Graphics2D g, Rectangle rect,String xLabel, String yLabel)
    {

	g.draw(rect);
	
	
	startX = rect.x;
	startY = rect.y + rect.height/2;
	endX = rect.x + rect.width;
	
//	stroke = new BasicStroke(1.0f);
//	g.setStroke(stroke);
	
	FontMetrics fm = g.getFontMetrics();
	// endY = rect.y;

	// 绘制纵横坐标轴
//	Stroke strokeBack = g.getStroke();
//	BasicStroke newStroke = new BasicStroke(2.0f);
//	g.setStroke(newStroke);
//	Line2D.Float hori = new Line2D.Float(startX, startY, startX
//		+ rect.width, startY);
//	Line2D.Float vert = new Line2D.Float(startX, startY-rect.height/2, startX, startY
//		+ rect.height/2);
//	g.draw(hori);
//	g.draw(vert);
//	g.setStroke(strokeBack);
	// 绘制单位线
	// 横轴单位线
	int count = horiUnitStart - horiShift;
	for (int x = startX, y = startY; x <= startX + rect.width; x += horiPixsPerUnit*scale)
	{
		 Line2D.Float unitLine = new Line2D.Float(x, y - rect.height / 2, x, y + rect.height/2);
		    g.draw(unitLine);
	    if(count%horiStep==0 || x==startX)
	    {
		String str = String.format("%.0f", (count*horiUnitScale)%24);
		Rectangle2D bound = fm.getStringBounds(str, g);
		g.drawString(str, (int)(x-bound.getWidth()/2), (int)(y + rect.height/2 + bound.getHeight()+2 ));
	    }
	    count++;
	}
	//纵轴负半轴单位线
	count=vertShift;
	for (int x = startX, y = startY; y <= startY + rect.height/2; y += vertPixsPerUnit*scale)
	{

		Line2D.Float unitLine = new Line2D.Float(x, y, x + rect.width, y);
		g.draw(unitLine);
	    if(count%vertStep==0 )
	    {
		String str = String.format("%.0f", - count*vertUnitScale);
		Rectangle2D bound = fm.getStringBounds(str, g);
		g.drawString(str, (int)(x - bound.getWidth()-4), (int)(y + bound.getHeight()/2 ));
	    }
	    count++;
	}
	//纵轴正半轴单位线
	count=-vertShift;
	for (int x = startX, y = startY; y >= startY - rect.height/2; y -= vertPixsPerUnit*scale)
	{
	    Line2D.Float unitLine = new Line2D.Float(x, y, x + rect.width, y);
	    g.draw(unitLine);
	    if(count%vertStep==0)
	    {
		String str = String.format("%.0f", count*vertUnitScale);
		Rectangle2D bound = fm.getStringBounds(str, g);
		g.drawString(str, (int)(x - bound.getWidth()-4), (int)(y + bound.getHeight()/2 ));
	    }
	    count++;
	}
	
	// 绘制坐标说明
	Rectangle2D xLabelBound = fm.getStringBounds(xLabel, g);
	Rectangle2D yLabelBound = fm.getStringBounds(yLabel, g);
	int x = startX + rect.width + 5 ; //- (int) (xLabelBound.getWidth() / 2);
	int y = startY + rect.height/2 + (int) xLabelBound.getHeight()/2-2;
	g.drawString(xLabel, x, y);

	x = startX - (int) yLabelBound.getWidth()/2;
	y = startY - rect.height/2 - 5;
	g.drawString(yLabel, x, y);
	
	
    }
  

    public int getHoriPixsPerUnit()
    {
        return horiPixsPerUnit;
    }

    public void setHoriPixsPerUnit(int horiPixsPerUnit)
    {
        this.horiPixsPerUnit = horiPixsPerUnit;
    }

    public int getVertPixsPerUnit()
    {
        return vertPixsPerUnit;
    }

    public void setVertPixsPerUnit(int vertPixsPerUnit)
    {
        this.vertPixsPerUnit = vertPixsPerUnit;
    }

    public double getScale()
    {
        return scale;
    }

    public void setScale(double scale)
    {
        this.scale = scale;
    }

    public int getHoriShift()
    {
        return horiShift;
    }

    public void setHoriShift(int horiShift)
    {
        this.horiShift = horiShift;
    }

    public int getVertShift()
    {
        return vertShift;
    }

    public void setVertShift(int vertShift)
    {
        this.vertShift = vertShift;
    }

    public int getHoriStep()
    {
        return horiStep;
    }

    public void setHoriStep(int horiStep)
    {
        this.horiStep = horiStep;
    }

    public int getVertStep()
    {
        return vertStep;
    }

    public void setVertStep(int vertStep)
    {
        this.vertStep = vertStep;
    }

    public double getHoriUnitScale()
    {
        return horiUnitScale;
    }

    public void setHoriUnitScale(double horiUnitScale)
    {
        this.horiUnitScale = horiUnitScale;
    }

    public int getHoriUnitStart()
    {
        return horiUnitStart;
    }

    public void setHoriUnitStart(int horiUnitStart)
    {
        this.horiUnitStart = horiUnitStart;
    }

    public double getVertUnitScale()
    {
        return vertUnitScale;
    }

    public void setVertUnitScale(double vertUnitScale)
    {
        this.vertUnitScale = vertUnitScale;
    }
    
    public static void main(String[] args)
    {
	DrawStatistics d = new DrawStatistics();
	d.drawGraph("hist.png");
    }
}
