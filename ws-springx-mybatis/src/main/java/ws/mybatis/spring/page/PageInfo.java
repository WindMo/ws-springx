package ws.mybatis.spring.page;

import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author WindShadow
 * @version 2022-01-08.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
class PageInfo {

    private String pageNumberParam;
    private String pageSizeParam;

    private Integer expectedPageNum;
    private Integer expectedPageSize;

    private Integer actualPageNum;
    private Integer actualPageSize;

    private Long actualTotal;

    private Page<?> page;

    void updateActualPageResult() {

        if (this.page != null) {
            actualPageNum = page.getPageNum();
            actualPageSize = page.getPageSize();
            actualTotal = page.getTotal();
        }
    }
}
