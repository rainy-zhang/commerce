package org.rainy.commerce.notifier;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Component
public class ConsoleNotifier extends AbstractEventNotifier {

    protected ConsoleNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    @NonNull
    protected Mono<Void> doNotify(@NonNull InstanceEvent event, @NonNull Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                log.info("Instance Status Change: [{}],[{}],[{}]", instance.getRegistration().getName(), instance.getId(), ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());
            } else {
                log.info("Instance Status: [{}],[{}],[{}]", instance.getRegistration().getName(), instance.getId(), event.getType());
            }
        });
    }
}
