package cn.askrosa.others;

import java.util.ArrayList;

public class PageIndexResult
{

    public static ArrayList<PageIndex> getPageIndexResult(int page, int pageDisplayNumber,
	    int resultPages, String link)
    {
	ArrayList<PageIndex> pageIndex = new ArrayList<PageIndex>();
	if (page > 1)
	{
	    PageIndex first = new PageIndex();
	    first.setPage("最前页");
	    first.setLink(link + "1");
	    pageIndex.add(first);
	    PageIndex before = new PageIndex();
	    before.setPage("前一页");
	    before.setLink(link + Integer.toString(page - 1));
	    pageIndex.add(before);
	}
	int beforePages = (pageDisplayNumber - 1) / 2;
	int begin = page - beforePages;
	int end = page + beforePages;
	if (begin < 1)
	{
	    begin = 1;
	    end = pageDisplayNumber + begin - 1;
	    if (end > resultPages)
		end = resultPages;
	}
	if (end > resultPages)
	{
	    end = resultPages;
	    begin = end - pageDisplayNumber + 1;
	    if (begin < 1)
		begin = 1;
	}
	if (begin > 1)
	{
	    PageIndex before = new PageIndex();
	    before.setPage("...");
	    before.setLink(link + Integer.toString(begin - 1));
	    pageIndex.add(before);
	}

	for (int i = begin; i <= end; i++)
	{
	    PageIndex pi = new PageIndex();
	    pi.setPage("[" + Integer.toString(i) + "]");
	    pi.setLink(link + Integer.toString(i));
	    if (i == page)
		pi.setColor("#ff0000");
	    pageIndex.add(pi);
	}
	if (end < resultPages)
	{
	    PageIndex later = new PageIndex();
	    later.setPage("...");
	    later.setLink(link + Integer.toString(end + 1));
	    pageIndex.add(later);
	}
	if (page < resultPages)
	{
	    PageIndex next = new PageIndex();
	    next.setPage("后一页");
	    next.setLink(link + Integer.toString(page + 1));
	    pageIndex.add(next);
	    PageIndex last = new PageIndex();
	    last.setPage("最后页");
	    last.setLink(link + Integer.toString(resultPages));
	    pageIndex.add(last);
	}
	PageIndex all = new PageIndex();
	all.setPage("共" + resultPages + "页");
	all.setLink(link + Integer.toString(resultPages));
	pageIndex.add(all);
	return pageIndex;
    }
}
