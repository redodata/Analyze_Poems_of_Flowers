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
    //�������ҳ
    private Queue<Page> docPageQueue=new LinkedBlockingQueue<>();
    //�������ҳ
    private Queue<Page> detailPageQueue=new LinkedBlockingQueue<>();

    //�ɼ���
    private final WebClient webClient;
    //��Ž�����
    private final List<Parse> parseList=new LinkedList<>();
    //�����ϴ��
    private final List<Pipeline> pipeLineList=new LinkedList<>();

    //�̵߳�����
    private final ExecutorService executorService;
    public Crawler(){
        this.webClient=new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        this.executorService= Executors.newFixedThreadPool(10, new ThreadFactory() {
            //����������̹߳���10���������߳���ΪCrawler-Thread-id
            private final AtomicInteger thread_id=new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                Thread thread=new Thread(r);
                thread.setName("Crawler-Thread-"+thread_id.getAndIncrement());
                return thread;
            }
        });
    }
//�������濪ʼ
    public void start(){
        this.executorService.submit((Runnable)()->{
           while(true){
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   logger.error("Parse occur Exception {}.",e.getMessage());//��{}��ʾռλ������e.getMessage()��ֵ����{}�С�
               }
               //�ó�����ҳ�ȳ���
               final Page page=Crawler.this.docPageQueue.poll();
               if(page==null){
                   continue;
               }
               //�ɼ�����ȡ��
               HtmlPage htmlPage= null;
               try {
                   htmlPage = Crawler.this.webClient.getPage(page.getUrl());
                   page.setHtmlPage(htmlPage);
                   //����(�ý��������е�ÿһ������������ҳ���н���
                   for(Parse parse:this.parseList){
                       parse.parse(page);
                   }

                   if(!page.isDetail()){
                       //�����ҳ��������ҳ����ȡ��������ҳ�棬����ӵ�������ҳ��ȥ������
                       Iterator<Page> iterator=page.getSubPage().iterator();
                       while(iterator.hasNext()){
                           Crawler.this.docPageQueue.add(iterator.next());
                           iterator.remove();
                       }
                   }else{
                       //�����ҳ������ҳ������ҳ��ӵ�����ҳ�н���
                       Crawler.this.detailPageQueue.add(page);
                   }
               } catch (IOException e) {
                   logger.error("Parse task occur exception{}.",e.getMessage()); ;
               }
           }
        });
        //��ϴ
        this.executorService.submit((Runnable)()->{
           while(true){
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   logger.error("Pipeline occur exception{}.",e.getMessage()) ;
               }
               //ȡ����ҳ���еĶ�ͷԪ�أ��������С�
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
    //��ϴ
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
    //��ӽ�����
    public void addParse(Parse parse){
        this.parseList.add(parse);
    }
    //�����ϴ��
    public void addPipeline(Pipeline pipeline){
        this.pipeLineList.add(pipeline);
    }
    //�ر������߳�
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

