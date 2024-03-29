package database;

import org.apache.commons.lang.builder.*;

/**
  * Autogenerated by Lisptorq 0.1.4 
*/
public class Article extends BaseArticle 
{
    /**
     * 
     */
    private static final long serialVersionUID = 7851740067067370886L;
    protected int responsePostCount=0;
    public String toString()
    {
	return new ToStringBuilder(this,ToStringStyle.DEFAULT_STYLE).append("id", id).append("author",author).append("title",title).append("ip",ip).toString();
    }
    public int getResponsePostCount()
    {
        return responsePostCount;
    }
    public void setResponsePostCount(int responsePostCount)
    {
        this.responsePostCount = responsePostCount;
    }
}