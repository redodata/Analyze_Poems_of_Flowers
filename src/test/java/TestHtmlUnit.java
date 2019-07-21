import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

/**
 * ����һ�����ڲ���HtmlUnit��İ�
 */
public class TestHtmlUnit {
    public static void main(String[] args) {
        try(WebClient webClient=new WebClient(BrowserVersion.CHROME)){
            webClient.getOptions().setJavaScriptEnabled(false);//������ִ��js�ļ�
            HtmlPage htmlPage= webClient.getPage("https://so.gushiwen.org/shiwenv_4c5705b99143.aspx");//����ҳ��ȡΪһ��HtmlPage����������url��
            HtmlElement body = htmlPage.getBody();
//            System.out.println(body.asText());
//            System.out.println("=====================================");
//            System.out.println(body.asXml());
            DomElement elementById = htmlPage.getElementById("contson4c5705b99143");//ȡ�á����¡�������
//            System.out.println(elementById.asText());
            //����
            String titlePath = "//div[@class='cont']/h1/text()";
            DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
            String title = titleDom.asText();
            System.out.println(title);

            //����
            String authorPath="//div[@class='cont']/p/a[2]";
            HtmlAnchor authorDom= (HtmlAnchor) body.getByXPath(authorPath).get(0);
            String author=authorDom.asText();
            System.out.println("���ߣ�"+author);
//        body.getByXPath(authorPath).forEach(o->{
//            HtmlAnchor anchor=(HtmlAnchor)o;
//            System.out.println(anchor.asText());
//        });
            //����//div[@class='cont']/p/a[1]";
            String dynastyPath="//div[@class='cont']/p/a[1]";
            HtmlAnchor dynastyDom= (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
            String dynasty=dynastyDom.asText();
            System.out.println("����"+dynasty);

        //����  ����ֱ��ʹ��id,��Ҫ�����ṹ��
            String contentPath = "//div[@class='cont']/div[@class='contson']";
            HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
            String content = contentDom.asText();
            System.out.println(content);
        } catch (IOException e) {
                e.printStackTrace();
            }

        }

}

