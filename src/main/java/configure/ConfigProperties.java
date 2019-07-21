package configure;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class ConfigProperties {
    private String crawlerBase;
    private String crawlerPath;
    private boolean crawlerDetail;

    private String dbUsername;
    private String dbPassword;
    private String dbUrl;
    private String dbDriverClass;

    private boolean enableConsole;//默认不开启控制台清洗

    public ConfigProperties(){
        //从外部文件中加载，给属性赋值。
        InputStream inputStream = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties");
        Properties p=new Properties();
        try {
            p.load(inputStream);
            System.out.println(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.crawlerBase= String.valueOf( p.get("crawler.base"));
        this.crawlerPath= String.valueOf(p.get("crawler.path"));
        this.crawlerDetail=Boolean.parseBoolean(String.valueOf(p.get("crawler.detail")));
        this.dbDriverClass=String.valueOf(p.get("db.driver_class"));
        this.dbUsername=String.valueOf(p.get("db.username"));
        this.dbPassword=String.valueOf(p.get("db.password"));
        this.dbUrl=String.valueOf(p.get("db.url"));
        this.enableConsole=Boolean.valueOf(String.valueOf(p.getProperty("config.enable_console","false")));
    }
}
