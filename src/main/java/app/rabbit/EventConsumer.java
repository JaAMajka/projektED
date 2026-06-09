package app.rabbit;
import app.repositories.MenuItemRepository;
import app.repositories.RateRepository;
import app.services.CafeReadModelService;
import app.services.GlobalStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventConsumer {
    private final MenuItemRepository menuItemRepository;
    private final RateRepository rateRepository;
    private final CafeReadModelService cafeReadModelService;
    private final GlobalStatsService globalStatsService;

    @RabbitListener(queues = RabbitMQConfig.MENU_ITEM_QUEUE)
    public void recalculateMenuItemData(Long cafeId){
        cafeReadModelService.recalculatePriceAverages(cafeId, menuItemRepository.findAvgPricePerType(cafeId));
        cafeReadModelService.updateHasIcedPropertyIfIcedItemAdded(cafeId, menuItemRepository.checkIfCafeHasIcedItems(cafeId));
        globalStatsService.updatePriceAverages(menuItemRepository.findAvgPricePerTypeGlobal());
    }
    @RabbitListener(queues = RabbitMQConfig.RATE_QUEUE)
    public void recalculateRateData(Long cafeId){
        cafeReadModelService.recalculateRateAverages(cafeId, rateRepository.findStatsByRate(cafeId).orElseThrow());
    }



}