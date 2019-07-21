package analyze.service;

import analyze.model.AuthorCount;
import analyze.model.DynastyCount;
import analyze.model.WordCount;
import java.util.List;

/**
 * 对最终业务的实现（分析作者的创作数量、词云分析）
 */
public interface AnalyzeService {
    /**
     * 分析作者的创作数量
     * @return
     */
    List<AuthorCount> analyzeAuthorCount();

    /**
     * 词云分析
     * @return
     */
    List<WordCount> analyzeWordCloud();

    List<DynastyCount> analyzeDynastyCount();
}
