package ws.spring.restrict;

/**
 * @author WindShadow
 * @version 2023-12-03.
 */

public class FrequencyRestrictorDefinition {

    /**
     * 频控器名称（全局唯一）
     */
    private String name;

    /**
     * 限定的频次
     */
    private int frequency;

    /**
     * 持续时长（单位：秒）
     */
    private long duration;

    public FrequencyRestrictorDefinition() {
    }

    public FrequencyRestrictorDefinition(String name, int frequency, long duration) {
        this.name = name;
        this.frequency = frequency;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
