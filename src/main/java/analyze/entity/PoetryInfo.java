package analyze.entity;

import lombok.Data;

/**
 * 描述诗的类
 */
@Data
public class PoetryInfo {
    //标题
    private String title;
    //朝代
    private String dynasty;
    //作者
    private String author;
    //内容
    private String content;
}
