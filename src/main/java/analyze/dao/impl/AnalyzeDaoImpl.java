package analyze.dao.impl;

import analyze.dao.AnalyzeDao;
import analyze.entity.PoetryInfo;
import analyze.model.AuthorCount;
import analyze.model.DynastyCount;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AnalyzeDaoImpl implements AnalyzeDao {
    private org.slf4j.Logger logger=LoggerFactory.getLogger(AnalyzeDaoImpl.class);
    private final DataSource dataSource;

    public AnalyzeDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        List<AuthorCount> datas=new ArrayList<>();
        String sql="select count(*) as count,author from poetryinfo group by author;";
        //try()自动关闭
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet rs=statement.executeQuery()) {
            while(rs.next()){
                AuthorCount authorCount=new AuthorCount();
                authorCount.setAuthor(rs.getString("author"));
                authorCount.setCount(rs.getInt("count"));
                datas.add(authorCount);//将数据存放到集合中
            }
        } catch (SQLException e) {
            logger.error("DataBase query occur exception{}.",e.getMessage());
        }
        return datas;
    }

    @Override
    public List<PoetryInfo> queryAllPoetryInfo() {
        List<PoetryInfo> datas=new ArrayList<>();
        String sql="select title,dynasty,author,content from poetryinfo;";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet rs=statement.executeQuery();){
            while(rs.next()){
                PoetryInfo poetryInfo=new PoetryInfo();
                poetryInfo.setTitle(rs.getString("title"));
                poetryInfo.setDynasty(rs.getString("dynasty"));
                poetryInfo.setAuthor(rs.getString("author"));
                poetryInfo.setContent(rs.getString("content"));

                datas.add(poetryInfo);//将诗放入集合
            }

        } catch (SQLException e) {
            logger.error("DataBase query occur exception{}.",e.getMessage());
        }
        return datas;
    }

    @Override
    public List<DynastyCount> queryDynasty() {
        List<DynastyCount> res=new ArrayList<>();
        String sql="select count(*) as count,dynasty from poetryinfo group by dynasty;";
        //try()自动关闭
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet rs=statement.executeQuery()) {
            while(rs.next()){
                DynastyCount dynastyCount=new DynastyCount();
                dynastyCount.setDynasty(rs.getString("dynasty"));
                dynastyCount.setCount(rs.getInt("count"));
                res.add(dynastyCount);
            }
        } catch (SQLException e) {
            logger.error("DataBase query occur exception{}.",e.getMessage());
        }
        return res;
    }
}
