package ws.spring.restrict;

/**
 * @author WindShadow
 * @version 2024-02-23.
 */
public interface FrequencyRestrictor {

    String getName();

    /**
     * 尝试根据refer进行限制
     *
     * @param refer
     * @return 限制成功与否
     */
    boolean tryRestrict(String refer);
}
