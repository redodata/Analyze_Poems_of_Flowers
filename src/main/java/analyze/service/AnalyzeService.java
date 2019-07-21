package analyze.service;

import analyze.model.AuthorCount;
import analyze.model.DynastyCount;
import analyze.model.WordCount;
import java.util.List;

/**
 * ������ҵ���ʵ�֣��������ߵĴ������������Ʒ�����
 */
public interface AnalyzeService {
    /**
     * �������ߵĴ�������
     * @return
     */
    List<AuthorCount> analyzeAuthorCount();

    /**
     * ���Ʒ���
     * @return
     */
    List<WordCount> analyzeWordCloud();

    List<DynastyCount> analyzeDynastyCount();
}
