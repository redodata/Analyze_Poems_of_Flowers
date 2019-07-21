package analyze.dao;

import analyze.entity.PoetryInfo;
import analyze.model.AuthorCount;
import analyze.model.DynastyCount;

import java.util.List;
import java.util.Map;

/**
 * 实现对数据进行查询
 */
public interface AnalyzeDao {
    /**
     * 分析诗中作者的创作数量
     * @return
     */
    List<AuthorCount> analyzeAuthorCount();

    /**
     * 查询所有的诗文，提供给业务层进行分析。
     * @return
     */
    List<PoetryInfo> queryAllPoetryInfo();

    /**
     * 分析写花诗数量与朝代的关系
     * @return
     */
    List<DynastyCount> queryDynasty();
}
