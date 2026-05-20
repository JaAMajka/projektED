package app.controllers;

import app.dtos.creating.CreateCafeDTO;
import app.dtos.creating.CreateMenuItemDTO;
import app.dtos.responding.ResponseCafeDTO;
import app.dtos.responding.ResponseMenuItemDTO;
import app.dtos.updating.UpdateCafeDTO;
import app.dtos.updating.UpdateMenuItemDTO;
import app.models.Cafe;
import app.models.MenuItem;
import app.services.CafeService;
import app.services.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cafes/{cafeId}/items/")
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMenuItemDTO> getMenuItemById(@PathVariable Long id, @PathVariable Long cafeId) {
        ResponseMenuItemDTO menuItemDto =  menuItemService.getMenuItemDtoById(cafeId, id);
        return new ResponseEntity<>(menuItemDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItemById(@PathVariable Long id,  @PathVariable Long cafeId) {
        menuItemService.removeMenuItemById(cafeId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMenuItemDTO> updateMenuItemById(@PathVariable Long id, @PathVariable Long cafeId, @RequestBody UpdateMenuItemDTO dto) {
        menuItemService.updateMenuItem(dto, cafeId, id);
        ResponseMenuItemDTO menuItemDto =  menuItemService.getMenuItemDtoById(cafeId, id);
        return new ResponseEntity<>(menuItemDto, HttpStatus.OK);

    }
    @PostMapping
    public ResponseEntity<ResponseMenuItemDTO> createMenuItem(@PathVariable Long cafeId, @RequestBody CreateMenuItemDTO dto){
        MenuItem menuItem = menuItemService.createMenuItem(dto);
        ResponseMenuItemDTO menuItemDto =  menuItemService.getMenuItemDtoById(cafeId, menuItem.getId());
        return new ResponseEntity<>(menuItemDto, HttpStatus.CREATED);
    }
}
