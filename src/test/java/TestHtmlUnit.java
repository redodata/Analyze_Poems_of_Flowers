import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

/**
 * 这是一个用于测试HtmlUnit类的包
 */
public class TestHtmlUnit {
    public static void main(String[] args) {
        try(WebClient webClient=new WebClient(BrowserVersion.CHROME)){
            webClient.getOptions().setJavaScriptEnabled(false);//不启用执行js文件
            HtmlPage htmlPage= webClient.getPage("https://so.gushiwen.org/shiwenv_4c5705b99143.aspx");//将网页抽取为一个HtmlPage对象（完整的url）
            HtmlElement body = htmlPage.getBody();
//            System.out.println(body.asText());
//            System.out.println("=====================================");
//            System.out.println(body.asXml());
            DomElement elementById = htmlPage.getElementById("contson4c5705b99143");//取得《关雎》的内容
//            System.out.println(elementById.asText());
            //标题
            String titlePath = "//div[@class='cont']/h1/text()";
            DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
            String title = titleDom.asText();
            System.out.println(title);

            //作者
            String authorPath="//div[@class='cont']/p/a[2]";
            HtmlAnchor authorDom= (HtmlAnchor) body.getByXPath(authorPath).get(0);
            String author=authorDom.asText();
            System.out.println("作者："+author);
//        body.getByXPath(authorPath).forEach(o->{
//            HtmlAnchor anchor=(HtmlAnchor)o;
//            System.out.println(anchor.asText());
//        });
            //朝代//div[@class='cont']/p/a[1]";
            String dynastyPath="//div[@class='cont']/p/a[1]";
            HtmlAnchor dynastyDom= (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
            String dynasty=dynastyDom.asText();
            System.out.println("朝代"+dynasty);

        //正文  不能直接使用id,而要分析结构。
            String contentPath = "//div[@class='cont']/div[@class='contson']";
            HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
            String content = contentDom.asText();
            System.out.println(content);
        } catch (IOException e) {
                e.printStackTrace();
            }

        }

}

