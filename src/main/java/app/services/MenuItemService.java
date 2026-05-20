package app.services;

import app.Exceptions.CafeNotFoundException;
import app.Exceptions.ItemAlreadyBelongsToCafeException;
import app.Exceptions.ItemDoesNotBelongToCafeException;
import app.Exceptions.MenuItemNotFoundException;
import app.dtos.creating.CreateMenuItemDTO;
import app.dtos.responding.ResponseMenuItemDTO;
import app.dtos.responding.ResponseScheduleDTO;
import app.dtos.updating.UpdateMenuItemDTO;
import app.mappers.MenuItemMapper;
import app.models.Cafe;
import app.models.MenuItem;
import app.repositories.CafeRepository;
import app.repositories.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final CafeRepository cafeRepository;
    private final MenuItemMapper menuItemMapper;

    public ResponseMenuItemDTO getMenuItemDtoById(Long cafeId, Long menuItemId) {
        getMenuItemById(cafeId, menuItemId);
        return menuItemMapper.toDto(getMenuItemById(cafeId, menuItemId));
    }

    private MenuItem getMenuItemById(Long cafeId, Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new MenuItemNotFoundException("This item does not exist."));
        if (menuItem.getCafe().getId().equals(cafeId)) {
            return menuItem;
        } else {
            throw new ItemDoesNotBelongToCafeException("This item does not belong to this cafe");
        }
    }

    public void removeMenuItemById(Long cafeId, Long menuItemId) {
        MenuItem menuItem = getMenuItemById(cafeId, menuItemId);
        menuItemRepository.delete(menuItem);
    }

    public MenuItem createMenuItem(CreateMenuItemDTO dto) {
        MenuItem menuItem = menuItemMapper.toEntity(dto);
        if (menuItemRepository.findByCafeAndName(menuItem.getCafe(), menuItem.getName()).isPresent()) {
            throw new ItemAlreadyBelongsToCafeException("This item already exists in this cafe");
        }
        Cafe cafe = cafeRepository.findById(dto.cafeId()).orElseThrow(() -> new CafeNotFoundException("This cafe does not exist"));
        menuItem.setCafe(cafe);
        return menuItemRepository.save(menuItem);
    }

    public void updateMenuItem(UpdateMenuItemDTO dto, Long cafeId, Long menuItemId) {
        MenuItem menuItem = getMenuItemById(cafeId, menuItemId);
        menuItemMapper.updateMenuItemFromDto(dto, menuItem);
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException("This cafe does not exist"));
        menuItem.setCafe(cafe);
        menuItemRepository.save(menuItem);
    }
    public List<ResponseMenuItemDTO> getMenuItemDtosByCafeId(Long cafeId){
        cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException("Cafe not found."));
        return menuItemRepository.findAllByCafeId(cafeId).
                stream()
                .map(menuItemMapper::toDto)
                .toList();

    }
}