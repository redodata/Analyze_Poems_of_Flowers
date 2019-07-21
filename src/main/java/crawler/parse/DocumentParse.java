package crawler.parse;

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import crawler.common.Page;

import javax.swing.text.Document;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 链接解析
 */
public class DocumentParse implements Parse{

    @Override
    public void parse(final Page page) {//防止new新的Page对象
        if(page.isDetail()){
            return;//如果page是详情页，则直接返回。
        }
        HtmlPage htmlPage=page.getHtmlPage();
        //通过标签获取详情页的超链接
         htmlPage.getBody().getElementsByAttribute("div", "class", "typecont").forEach(div-> {
             DomNodeList<HtmlElement> nodeList=div.getElementsByTagName("a");
             nodeList.forEach(
                     aNode->{
                         String path=aNode.getAttribute("href");
                         Page subPage=new Page(page.getBase(),path,true);
                         page.getSubPage().add(subPage);
                     }
             );
         });
        }
    }
