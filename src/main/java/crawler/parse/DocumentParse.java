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
 * ���ӽ���
 */
public class DocumentParse implements Parse{

    @Override
    public void parse(final Page page) {//��ֹnew�µ�Page����
        if(page.isDetail()){
            return;//���page������ҳ����ֱ�ӷ��ء�
        }
        HtmlPage htmlPage=page.getHtmlPage();
        //ͨ����ǩ��ȡ����ҳ�ĳ�����
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
