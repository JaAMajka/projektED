package app.rabbit;
import app.repositories.MenuItemRepository;
import app.services.CafeReadModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CafeEventConsumer {
    private final MenuItemRepository menuItemRepository;
    private final CafeReadModelService cafeReadModelService;

    @RabbitListener(queues = RabbitMQConfig.MENU_ITEM_QUEUE)
    public void recalculatePriceAverages(Long cafeId){
        cafeReadModelService.recalculateAverages(cafeId, menuItemRepository.findAvgPricePerType(cafeId));
    }



}