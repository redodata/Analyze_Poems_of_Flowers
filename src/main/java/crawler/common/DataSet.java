package crawler.common;

import java.util.HashMap;
import java.util.Map;

/**
 * �洢��ϴ������
 */
public class DataSet {
    /**
     * data��DOM��������ϴ֮��洢������
     * ���磺
     * ���⣺����
     * ���ߣ�����
     * ���ģ�XXX
     */
    private Map<String,Object> data=new HashMap<>();

    public void putData(String key,Object value){
        this.data.put(key,value);
    }

    public Object getData(String key){
        return this.data.get(key);
    }

    public Map<String,Object> getData(){
        return new HashMap<>(this.data);//ΪʲôҪnew�������ƻ�ԭ��������
    }
}
