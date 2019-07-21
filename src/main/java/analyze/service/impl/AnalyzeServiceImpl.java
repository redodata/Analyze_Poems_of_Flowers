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
        return o1.getCount().compareTo(o2.getCount()) * (-1);//降序
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        //此处结果并未排序，排序方式：1）Dao层SQL排序  2）Service层进行数据排序
        List<AuthorCount> authorCounts=analyzeDao.analyzeAuthorCount();
        sort(authorCounts, //升序
                //                return o1.getCount()-o2.getCount();
                //此处是降序
                AnalyzeServiceImpl::compare);
        return authorCounts;
    }

    @Override
    public List<WordCount> analyzeWordCloud() {
       //1.查询出所有的数据
        //2.取出title content
        //3.分词   过滤掉/w null 空 长度小于2
        //4.统计k-v k是词  v是词频。
        Map<String,Integer> map=new HashMap<>();
        List<PoetryInfo> poetryInfos=analyzeDao.queryAllPoetryInfo();
        for(PoetryInfo poetryInfo:poetryInfos){
            List<Term> terms=new ArrayList<>();
            String title=poetryInfo.getTitle();
            String content=poetryInfo.getContent();
            terms.addAll(NlpAnalysis.parse(title).getTerms());
            terms.addAll(NlpAnalysis.parse(content).getTerms());
            //ArrayList不能在遍历的时候并发修改，所以要使用迭代器。
            Iterator<Term> iterator = terms.iterator();
            while(iterator.hasNext()){
                Term term=iterator.next();
                //词性的过滤
                if(term.getNatureStr().equals("w")||term.getNatureStr()==null){//如果term的次性等于“w”
                    iterator.remove();
                    continue;
                }
                //词的过滤
                if(term.getRealName().length()<2){
                    iterator.remove();
                    continue;
                }
                //统计
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
                Comparator.comparingInt(DynastyCount::getCount));//此处是升序
        return dynastyCounts;
    }

}
