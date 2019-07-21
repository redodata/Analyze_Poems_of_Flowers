import configure.ObjectFactory;
import crawler.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.WebController;


public class TheBookofSongs {
    private static final Logger LOGGER=LoggerFactory.getLogger(TheBookofSongs.class);
    public static void main(String[] args) {

        WebController webController=ObjectFactory.getInstance().getObject(WebController.class);
        LOGGER.info("Web Server launch......");
        //运行了web服务，提供接口。
        webController.launch();
        if(args.length==1&&args[0].equals("run-crawler")){
            Crawler crawler=ObjectFactory.getInstance().getObject(Crawler.class);
            LOGGER.info("Crawler started......");
            crawler.start();
        }


    }
}
