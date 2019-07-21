package crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import crawler.common.Page;
import crawler.parse.DataPageParse;
import crawler.parse.DocumentParse;
import crawler.parse.Parse;
import crawler.pipeline.ConsolePipeline;
import crawler.pipeline.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Crawler {
    private final Logger logger=LoggerFactory.getLogger(Crawler.class);
    //存放链接页
    private Queue<Page> docPageQueue=new LinkedBlockingQueue<>();
    //存放详情页
    private Queue<Page> detailPageQueue=new LinkedBlockingQueue<>();

    //采集器
    private final WebClient webClient;
    //存放解析器
    private final List<Parse> parseList=new LinkedList<>();
    //存放清洗器
    private final List<Pipeline> pipeLineList=new LinkedList<>();

    //线程调度器
    private final ExecutorService executorService;
    public Crawler(){
        this.webClient=new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        this.executorService= Executors.newFixedThreadPool(10, new ThreadFactory() {
            //设置爬虫的线程共有10个，设置线程名为Crawler-Thread-id
            private final AtomicInteger thread_id=new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                Thread thread=new Thread(r);
                thread.setName("Crawler-Thread-"+thread_id.getAndIncrement());
                return thread;
            }
        });
    }
//启动爬虫开始
    public void start(){
        this.executorService.submit((Runnable)()->{
           while(true){
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   logger.error("Parse occur Exception {}.",e.getMessage());//用{}表示占位符，将e.getMessage()的值放入{}中。
               }
               //让超链接页先出队
               final Page page=Crawler.this.docPageQueue.poll();
               if(page==null){
                   continue;
               }
               //采集（爬取）
               HtmlPage htmlPage= null;
               try {
                   htmlPage = Crawler.this.webClient.getPage(page.getUrl());
                   page.setHtmlPage(htmlPage);
                   //解析(用解析队列中的每一个解析器对网页进行解析
                   for(Parse parse:this.parseList){
                       parse.parse(page);
                   }

                   if(!page.isDetail()){
                       //如果网页不是详情页，则取出它的子页面，并添加到超链接页中去解析。
                       Iterator<Page> iterator=page.getSubPage().iterator();
                       while(iterator.hasNext()){
                           Crawler.this.docPageQueue.add(iterator.next());
                           iterator.remove();
                       }
                   }else{
                       //如果网页是详情页，则将网页添加到详情页中解析
                       Crawler.this.detailPageQueue.add(page);
                   }
               } catch (IOException e) {
                   logger.error("Parse task occur exception{}.",e.getMessage()); ;
               }
           }
        });
        //清洗
        this.executorService.submit((Runnable)()->{
           while(true){
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   logger.error("Pipeline occur exception{}.",e.getMessage()) ;
               }
               //取详情页队列的队头元素，并出队列。
               final Page page=Crawler.this.detailPageQueue.poll();
               if(page==null){
                   continue;
               }
               Crawler.this.executorService.submit(()->{
                   for(Pipeline pipeline:Crawler.this.pipeLineList){
                       pipeline.pipeline(page);
                   }
               });
           }
        });
    }

    private void parse() throws InterruptedException {
        while(true){
            Thread.sleep(1000);
            final Page page=this.docPageQueue.poll();
            if(page==null){
                continue;
            }
            HtmlPage htmlPage=page.getHtmlPage();
            page.setHtmlPage(htmlPage);
            for(Parse parse:parseList){
                parse.parse(page);
            }
            if(page.isDetail()){
                this.detailPageQueue.add(page);
            }else{
                Iterator<Page> iterator=page.getSubPage().iterator();
                while(iterator.hasNext()){
                    Page subPage=iterator.next();
                    this.docPageQueue.add(subPage);
                    iterator.remove();
                }
            }
        }
    }
    //清洗
    private void pipeLine() throws InterruptedException {
        while(true){
            Thread.sleep(1000);
            final  Page page=this.detailPageQueue.poll();
            if(page==null){
                continue;
            }
            this.executorService.submit(()->{
                for(Pipeline pipeline:Crawler.this.pipeLineList){
                    pipeline.pipeline(page);
                }
            });
        }
    }
    //添加解析器
    public void addParse(Parse parse){
        this.parseList.add(parse);
    }
    //添加清洗器
    public void addPipeline(Pipeline pipeline){
        this.pipeLineList.add(pipeline);
    }
    //关闭爬虫线程
    public void shutdown(){
        if(this.executorService!=null&&this.executorService.isShutdown()){
            this.executorService.shutdown();
        }
        logger.info("Crawler stopped ......");
    }



    public void addPage(Page page) {
        this.docPageQueue.add(page);
    }
}

