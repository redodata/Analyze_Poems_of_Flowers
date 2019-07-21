package crawler.common;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Page {
    //url=base+path
    //������վ�ĸ���ַ  eg:https://so.gushiwen.org
    private final String base;
    //��ϸ·��
    private final String path;
    /**
     * ��ҳDOM����
     */
    private HtmlPage htmlPage;

    /**
     * ��ʶ��ҳ�Ƿ�������ҳ
     */
    private final boolean detail;

    /**
     * �����ҳ�漯��
     */
    private Set<Page> subPage=new HashSet<>();

    /**
     * ���ݶ���
     */
    private DataSet dataSet=new DataSet();

    public String getUrl(){
        return this.base+this.path;
    }

}
