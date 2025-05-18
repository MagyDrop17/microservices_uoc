package edu.uoc.epcsd.productcatalog.application.rest;


import edu.uoc.epcsd.productcatalog.application.rest.request.CreateItemRequest;
import edu.uoc.epcsd.productcatalog.domain.Item;
import edu.uoc.epcsd.productcatalog.domain.ItemStatus;
import edu.uoc.epcsd.productcatalog.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/items")
public class ItemRESTController {


    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getAllItems() {
        log.trace("getAllItems");

        return itemService.findAllItems();
    }

    @GetMapping("/{serialNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Item> getItemById(@PathVariable @NotBlank String serialNumber) {
        log.trace("getItemById");

        return itemService.findBySerialNumber(serialNumber).map(item -> ResponseEntity.ok().body(item))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{serialNumber}")
    public ResponseEntity<Item> setOperationalItem(@PathVariable @NotBlank String serialNumber, @RequestBody @NotNull boolean operational) {

        try {
            return ResponseEntity.ok().body(itemService.setOperational(serialNumber, operational));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified item serial number '" + serialNumber + "' does not exist.");
        }
    }

    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody @NotNull @Valid CreateItemRequest createItemRequest) {
        log.trace("createItem");

        try {
            log.trace("Creating item " + createItemRequest);
            String serialNumber = itemService.createItem(createItemRequest.getProductId(),
                    createItemRequest.getSerialNumber());
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{serialNumber}")
                    .buildAndExpand(serialNumber)
                    .toUri();

            return ResponseEntity.created(uri).body(serialNumber);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified product id " + createItemRequest.getProductId() + " does not exist.", e);
        }
    }

    @GetMapping("/by-product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getItemsByProductId(@PathVariable @NotNull Long productId) {
        // Funcio per a obtenir els items d'un producte
        log.trace("getItemsByProductId for productId={}", productId);

        // Comprovem que el producte existeix
        return itemService.findByProductId(productId);
    }

    @PutMapping("/{serialNumber}/status")
    public ResponseEntity<Void> changeItemStatus(@PathVariable @NotBlank String serialNumber,
                                                 @RequestParam ItemStatus status) {
        // Funcio per a canviar l'estat d'un item
        itemService.changeItemStatus(serialNumber, status);

        // Comprovem que l'item existeix
        return ResponseEntity.ok().build();
    }

}
