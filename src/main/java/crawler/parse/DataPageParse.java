package crawler.parse;

import com.gargoylesoftware.htmlunit.html.*;
import crawler.common.Page;


/**
 * 详情页面的解析
 */
public class DataPageParse implements Parse {
    public void parse(final Page page) {
        if (!page.isDetail()) {
            return;
        }

        HtmlPage htmlPage = page.getHtmlPage();
        final HtmlElement body = htmlPage.getBody();
        //标题
        String titlePath = "//div[@class='cont']/h1/text()";
        DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
        String title = titleDom.asText();


        String dynastyPath = "//div[@class='cont']/p/a[1]";
        HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
        String dynasty = dynastyDom.asText();

        //作者
        String authorPath = "//div[@class='cont']/p/a[2]";
        HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author = authorDom.asText();

        //正文  不能直接使用id,而要分析结构。
        String contentPath = "//div[@class='cont']/div[@class='contson']";
        HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content = contentDom.asText();

        page.getDataSet().putData("title",title);
        page.getDataSet().putData("dynasty",dynasty);
        page.getDataSet().putData("author",author);
        page.getDataSet().putData("content",content);//可以添加更多的数据


    }
}
