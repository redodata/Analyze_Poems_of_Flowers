package analyze.model;

import lombok.Data;

/**
 * 该类用于统计写花的诗的朝代和其数量
 */
@Data
public class DynastyCount {
    private String dynasty;
    private Integer count;
}
