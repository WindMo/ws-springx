package ws.spring.restrict;

import org.springframework.lang.NonNull;

/**
 * @author WindShadow
 * @version 2024-02-24.
 */
public interface FrequencyRestrictRegistrar {

    FrequencyRestrictor registerRestrictor(@NonNull FrequencyRestrictorDefinition definition) throws RestrictorDeclarationException;
}
