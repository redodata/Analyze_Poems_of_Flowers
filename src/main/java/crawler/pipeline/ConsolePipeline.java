package crawler.pipeline;

import crawler.common.Page;

import java.util.Map;

public class ConsolePipeline implements Pipeline{

    @Override
    public void pipeline(Page page) {
        Map<String,Object> data=page.getDataSet().getData();
    }
}
