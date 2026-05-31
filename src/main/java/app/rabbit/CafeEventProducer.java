package app.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CafeEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendRateCreatedEvent(Long cafeId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.RATE_QUEUE,
                cafeId
        );
    }

    public void sendMenuItemChangedEvent(Long cafeId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.MENU_ITEM_QUEUE,
                cafeId
        );
    }
}
