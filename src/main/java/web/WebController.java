package web;

import analyze.model.AuthorCount;
import analyze.model.DynastyCount;
import analyze.model.WordCount;
import analyze.service.AnalyzeService;
import com.google.gson.Gson;
import configure.ObjectFactory;
import crawler.Crawler;
import spark.*;

import java.util.List;

/**
 * web API
 * 1.Sparkjava���web API����
 * 2.Servlet����(��webController�̳�HttpServlet��
 *
 */
public class WebController {
    private final AnalyzeService analyzeService;

    public WebController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }
    //����URL��ַ��http://127.0.0.1:4567/
    //->  /analyze/author_count
    public List<AuthorCount> analyzeAuthorCount(){
        return analyzeService.analyzeAuthorCount();
    }
    //����URL��ַ��http://127.0.0.1:4567/
    //->  /analyze/word_cloud
    public List<WordCount> analyzeWordCloud(){
        return analyzeService.analyzeWordCloud();
    }
    public List<DynastyCount> analyzeDynastyCount(){
        return analyzeService.analyzeDynastyCount();
    }

    //����web
    public void launch(){
        ResponseTransformer responseTransformer=new JSONResponseTransformer();
        //ǰ�˾�̬�ļ���Ŀ¼
        Spark.staticFileLocation("/static");
        //����˽ӿ�
        Spark.get("/analyze/author_count", ((request, response) -> analyzeAuthorCount()),responseTransformer);
        Spark.get("/analyze/word_cloud",((request, response) -> analyzeWordCloud()),responseTransformer);
        Spark.get("/analyze/dynasty_count",((request, response) -> analyzeDynastyCount()),responseTransformer);
        //����ֹͣ
        Spark.get("/crawler/stop",((request, response) -> {
           Crawler crawler =ObjectFactory.getInstance().getObject(Crawler.class);
           crawler.shutdown();
           return "����ֹͣ";//�����ҳ����ʾ������ֹͣ������̨��ӡ����־��
        }));
    }

    public static class JSONResponseTransformer implements ResponseTransformer{
        private Gson gson=new Gson();
        //object-->String
        @Override
        public String render(Object o) throws Exception {
            return gson.toJson(o);
        }
    }
}
