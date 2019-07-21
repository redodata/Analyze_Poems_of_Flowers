package crawler.pipeline;

import crawler.common.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataPipeline implements Pipeline{
    private final Logger logger=LoggerFactory.getLogger(DataPipeline.class);
    private final DataSource dataSource;//数据源

    public DataPipeline(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void pipeline(final Page page) {
        String title= (String) page.getDataSet().getData("title");
        String dynasty= (String) page.getDataSet().getData("dynasty");
        String author= (String) page.getDataSet().getData("author");
        String content= (String) page.getDataSet().getData("content");

        String sql="insert into poetryinfo (title,dynasty,author,content) values (?,?,?,?)";
        try(Connection connection=dataSource.getConnection()){//准备连接
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setString(1,title);
            statement.setString(2,dynasty);
            statement.setString(3,author);
            statement.setString(4,content);

            statement.executeUpdate();//执行更新
        } catch (SQLException e) {
           logger.error("Database insert occur exception {}.",e.getMessage());
        }
    }
}
