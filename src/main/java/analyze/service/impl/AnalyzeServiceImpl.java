package analyze.service.impl;

import analyze.dao.AnalyzeDao;
import analyze.entity.PoetryInfo;
import analyze.model.AuthorCount;
import analyze.model.DynastyCount;
import analyze.model.WordCount;
import analyze.service.AnalyzeService;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.*;

import static java.util.Collections.*;

public class AnalyzeServiceImpl implements AnalyzeService {
    private final AnalyzeDao analyzeDao;

    public AnalyzeServiceImpl(AnalyzeDao analyzeDao) {
        this.analyzeDao = analyzeDao;
    }

    private static int compare(AuthorCount o1, AuthorCount o2) {
        return o1.getCount().compareTo(o2.getCount()) * (-1);//����
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        //�˴������δ��������ʽ��1��Dao��SQL����  2��Service�������������
        List<AuthorCount> authorCounts=analyzeDao.analyzeAuthorCount();
        sort(authorCounts, //����
                //                return o1.getCount()-o2.getCount();
                //�˴��ǽ���
                AnalyzeServiceImpl::compare);
        return authorCounts;
    }

    @Override
    public List<WordCount> analyzeWordCloud() {
       //1.��ѯ�����е�����
        //2.ȡ��title content
        //3.�ִ�   ���˵�/w null �� ����С��2
        //4.ͳ��k-v k�Ǵ�  v�Ǵ�Ƶ��
        Map<String,Integer> map=new HashMap<>();
        List<PoetryInfo> poetryInfos=analyzeDao.queryAllPoetryInfo();
        for(PoetryInfo poetryInfo:poetryInfos){
            List<Term> terms=new ArrayList<>();
            String title=poetryInfo.getTitle();
            String content=poetryInfo.getContent();
            terms.addAll(NlpAnalysis.parse(title).getTerms());
            terms.addAll(NlpAnalysis.parse(content).getTerms());
            //ArrayList�����ڱ�����ʱ�򲢷��޸ģ�����Ҫʹ�õ�������
            Iterator<Term> iterator = terms.iterator();
            while(iterator.hasNext()){
                Term term=iterator.next();
                //���ԵĹ���
                if(term.getNatureStr().equals("w")||term.getNatureStr()==null){//���term�Ĵ��Ե��ڡ�w��
                    iterator.remove();
                    continue;
                }
                //�ʵĹ���
                if(term.getRealName().length()<2){
                    iterator.remove();
                    continue;
                }
                //ͳ��
                String realName=term.getRealName();
                Integer count=0;
                if(map.containsKey(realName)){
                    count=map.get(realName)+1;
                }else{
                    count=1;
                }
                map.put(realName,count);
            }
        }
        List<WordCount> wordCounts=new ArrayList<>();
        for(Map.Entry<String,Integer> entry:map.entrySet()){
            WordCount wordCount=new WordCount();
            wordCount.setCount(entry.getValue());
            wordCount.setWord(entry.getKey());
            wordCounts.add(wordCount);
        }
        return wordCounts;
    }

    @Override
    public List<DynastyCount> analyzeDynastyCount() {
        List<DynastyCount> dynastyCounts=analyzeDao.queryDynasty();
        sort(dynastyCounts,
                Comparator.comparingInt(DynastyCount::getCount));//�˴�������
        return dynastyCounts;
    }

}
