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
 * 1.Sparkjava完成web API开发
 * 2.Servlet技术(给webController继承HttpServlet）
 *
 */
public class WebController {
    private final AnalyzeService analyzeService;

    public WebController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }
    //访问URL地址：http://127.0.0.1:4567/
    //->  /analyze/author_count
    public List<AuthorCount> analyzeAuthorCount(){
        return analyzeService.analyzeAuthorCount();
    }
    //访问URL地址：http://127.0.0.1:4567/
    //->  /analyze/word_cloud
    public List<WordCount> analyzeWordCloud(){
        return analyzeService.analyzeWordCloud();
    }
    public List<DynastyCount> analyzeDynastyCount(){
        return analyzeService.analyzeDynastyCount();
    }

    //运行web
    public void launch(){
        ResponseTransformer responseTransformer=new JSONResponseTransformer();
        //前端静态文件的目录
        Spark.staticFileLocation("/static");
        //服务端接口
        Spark.get("/analyze/author_count", ((request, response) -> analyzeAuthorCount()),responseTransformer);
        Spark.get("/analyze/word_cloud",((request, response) -> analyzeWordCloud()),responseTransformer);
        Spark.get("/analyze/dynasty_count",((request, response) -> analyzeDynastyCount()),responseTransformer);
        //爬虫停止
        Spark.get("/crawler/stop",((request, response) -> {
           Crawler crawler =ObjectFactory.getInstance().getObject(Crawler.class);
           crawler.shutdown();
           return "爬虫停止";//浏览器页面显示“爬虫停止”，后台打印出日志。
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
