package ws.spring.restrict;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author WindShadow
 * @version 2023-12-03.
 */

public interface FrequencyRestrictService extends FrequencyRestrictRegistrar {

    void addRestrictor(FrequencyRestrictor restrictor);

    @Nullable
    FrequencyRestrictor getRestrictor(@NonNull String restrictorName);

    void clearAllRestrictors();
}
