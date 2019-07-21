package crawler.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储清洗的数据
 */
public class DataSet {
    /**
     * data把DOM解析、清洗之后存储的数据
     * 比如：
     * 标题：关雎
     * 作者：佚名
     * 正文：XXX
     */
    private Map<String,Object> data=new HashMap<>();

    public void putData(String key,Object value){
        this.data.put(key,value);
    }

    public Object getData(String key){
        return this.data.get(key);
    }

    public Map<String,Object> getData(){
        return new HashMap<>(this.data);//为什么要new？避免破坏原来的数据
    }
}
