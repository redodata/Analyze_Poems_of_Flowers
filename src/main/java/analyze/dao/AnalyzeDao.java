package analyze.dao;

import analyze.entity.PoetryInfo;
import analyze.model.AuthorCount;
import analyze.model.DynastyCount;

import java.util.List;
import java.util.Map;

/**
 * ʵ�ֶ����ݽ��в�ѯ
 */
public interface AnalyzeDao {
    /**
     * ����ʫ�����ߵĴ�������
     * @return
     */
    List<AuthorCount> analyzeAuthorCount();

    /**
     * ��ѯ���е�ʫ�ģ��ṩ��ҵ�����з�����
     * @return
     */
    List<PoetryInfo> queryAllPoetryInfo();

    /**
     * ����д��ʫ�����볯���Ĺ�ϵ
     * @return
     */
    List<DynastyCount> queryDynasty();
}
