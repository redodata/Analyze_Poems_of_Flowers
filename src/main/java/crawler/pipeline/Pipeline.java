package crawler.pipeline;

import crawler.common.Page;

public interface Pipeline {
    /**
     * �ܵ�����page�е�����
     * @param page
     */
    void pipeline(final Page page);
}
