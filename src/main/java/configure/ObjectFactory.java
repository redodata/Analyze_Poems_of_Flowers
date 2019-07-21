package configure;

import analyze.dao.AnalyzeDao;
import analyze.dao.impl.AnalyzeDaoImpl;
import analyze.service.AnalyzeService;
import analyze.service.impl.AnalyzeServiceImpl;
import com.alibaba.druid.pool.DruidDataSource;
import crawler.Crawler;
import crawler.common.Page;
import crawler.parse.DataPageParse;
import crawler.parse.DocumentParse;
import crawler.pipeline.ConsolePipeline;
import crawler.pipeline.DataPipeline;
import org.slf4j.LoggerFactory;
import web.WebController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {
    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(ObjectFactory.class);
    //存放所有的对象
    private static final Map<Class,Object> objectHashMap=new HashMap<>();
    private static final ObjectFactory instance=new ObjectFactory();//单例
    public static ObjectFactory getInstance(){
        return instance;
    }
    //打印对象
    private void printObject(){
       logger.info("================ObjectFactory List==============");
        for(Map.Entry<Class,Object> entry:objectHashMap.entrySet()){
            logger.info(String.format("\t\t[%-5s]===>[%s]",entry.getKey().getCanonicalName(),entry.getValue().getClass().getCanonicalName()));//getCanonicalName()获取类的全名称
            logger.info("===========================================");
        }
    }
    private ObjectFactory(){
        //1.初始化配置对象
        initConfigProperties();
        //2.数据源对象
        initDataSource();
        //3.爬虫对象
        initCrawler();
        //4.web对象
        initWebController();
        //5.对象清单打印输出
        printObject();
    }

    private void initWebController() {
        DataSource dataSource=getObject(DataSource.class);
        AnalyzeDao analyzeDao=new AnalyzeDaoImpl(dataSource);
        AnalyzeService analyzeService=new AnalyzeServiceImpl(analyzeDao);
        WebController webController=new WebController(analyzeService);
        objectHashMap.put(WebController.class,webController);
    }

    private void initCrawler() {
        ConfigProperties configProperties=getObject(ConfigProperties.class);//使用静态方法获取对象
        final Page page=new Page(configProperties.getCrawlerBase(),configProperties.getCrawlerPath(),configProperties.isCrawlerDetail());
        DataSource dataSource=getObject(DataSource.class);
        Crawler crawler=new Crawler();
        crawler.addParse(new DocumentParse());
        crawler.addParse(new DataPageParse());
        crawler.addPipeline(new DataPipeline(dataSource));
        if(configProperties.isEnableConsole()) {
            crawler.addPipeline(new ConsolePipeline());
        }
        crawler.addPage(page);
        objectHashMap.put(Crawler.class,crawler);
//        crawler.start();//如果不注释，则可以直接通过创建webController对象启动。
    }

    private void initDataSource() {
        ConfigProperties configProperties=getObject(ConfigProperties.class);//使用静态方法获取对象
        DataSource dataSource=new DruidDataSource();
        ((DruidDataSource) dataSource).setUsername(configProperties.getDbUsername());
        ((DruidDataSource) dataSource).setPassword(configProperties.getDbPassword());
        ((DruidDataSource) dataSource).setDriverClassName(configProperties.getDbDriverClass());
        ((DruidDataSource) dataSource).setUrl(configProperties.getDbUrl());
        objectHashMap.put(DataSource.class,dataSource);
    }

    private void initConfigProperties() {
        ConfigProperties configProperties=new ConfigProperties();
        objectHashMap.put(ConfigProperties.class,configProperties);
        logger.info("ConfigProperties info:\n{}",configProperties.toString());//打印配置信息
    }

    public <T> T getObject(Class classz){
        if(!objectHashMap.containsKey(classz)){
            throw new IllegalArgumentException("Class "+classz.getName()+"not found Object");
        }
        return (T)objectHashMap.get(classz);
    }
}
