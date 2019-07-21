package analyze.model;

import lombok.Data;

/**
 * 描述作者和其创作诗数量关系的模型
 */
@Data
public class AuthorCount {
    private String author;
    private Integer count;
}
