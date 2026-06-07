package app.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendRateEvent(Long cafeId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.RATE_QUEUE,
                cafeId
        );
    }

    public void sendMenuItemEvent(Long cafeId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.MENU_ITEM_QUEUE,
                cafeId
        );
    }
}
