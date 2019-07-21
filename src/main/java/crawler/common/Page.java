package crawler.common;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Page {
    //url=base+path
    //数据网站的根地址  eg:https://so.gushiwen.org
    private final String base;
    //详细路径
    private final String path;
    /**
     * 网页DOM对象
     */
    private HtmlPage htmlPage;

    /**
     * 标识网页是否是详情页
     */
    private final boolean detail;

    /**
     * 存放子页面集合
     */
    private Set<Page> subPage=new HashSet<>();

    /**
     * 数据对象
     */
    private DataSet dataSet=new DataSet();

    public String getUrl(){
        return this.base+this.path;
    }

}
