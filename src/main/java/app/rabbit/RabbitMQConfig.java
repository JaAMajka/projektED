package app.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    public static final String RATE_QUEUE = "rate.queue";
    public static final String MENU_ITEM_QUEUE = "menu.item.queue";
    public static final String EXCHANGE = "cafe.exchange";

    @Bean
    public Queue rateQueue() {
        return new Queue(RATE_QUEUE);
    }

    @Bean
    public Queue menuItemQueue() {
        return new Queue(MENU_ITEM_QUEUE);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding rateBinding(Queue rateQueue, DirectExchange exchange) {
        return BindingBuilder.bind(rateQueue).to(exchange).with(RATE_QUEUE);
    }

    @Bean
    public Binding menuItemBinding(Queue menuItemQueue, DirectExchange exchange) {
        return BindingBuilder.bind(menuItemQueue).to(exchange).with(MENU_ITEM_QUEUE);
    }
}
