package app.rabbit;
import app.repositories.MenuItemRepository;
import app.repositories.RateRepository;
import app.services.CafeReadModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventConsumer {
    private final MenuItemRepository menuItemRepository;
    private final RateRepository rateRepository;
    private final CafeReadModelService cafeReadModelService;

    @RabbitListener(queues = RabbitMQConfig.MENU_ITEM_QUEUE)
    public void recalculateMenuItemData(Long cafeId){
        cafeReadModelService.recalculatePriceAverages(cafeId, menuItemRepository.findAvgPricePerType(cafeId));
        cafeReadModelService.updateHasIcedPropertyIfIcedItemAdded(cafeId, menuItemRepository.checkIfCafeHasIcedItems(cafeId));
    }
    @RabbitListener(queues = RabbitMQConfig.RATE_QUEUE)
    public void recalculateRateAverages(Long cafeId){
        cafeReadModelService.recalculateRateAverages(cafeId, rateRepository.findAvgRate(cafeId).orElseThrow());
    }




}