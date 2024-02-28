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

    /**
     * 重置对refer的限制记录
     * @param refer
     */
    void resetRestrict(String refer);

    /**
     * 重置此频控器
     */
    void resetRestrictor();
}
