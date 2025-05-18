package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.application.rest.request.CreateDigitalItemRequest;
import edu.uoc.epcsd.user.domain.Alert;
import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.service.DigitalItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/digitalItem")
public class DigitalItemRESTController {

    private final DigitalItemService digitalItemService;

    @GetMapping("/allItems")
    @ResponseStatus(HttpStatus.OK)
    public List<DigitalItem> getAllDigitalItem() {
        log.trace("getAllDigitalItem");

        return digitalItemService.findAllDigitalItem();
    }
    
    // TODO: add the code for the missing system operations here: 
    // use the corresponding mapping HTTP request annotation with the parameter: "/{digitalItemId}"
    // and create the method getDigitalItemById(@PathVariable @NotNull Long digitalItemId)
    // which call the corresponding getDigitalItemById method 
    @GetMapping("/{digitalItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DigitalItem> getDigitalItemById(@PathVariable @NotNull Long digitalItemId) {

        log.trace("getDigitalItemById");

        // Comprovem si l'element digital existeix, si no, retornem un error 404
        return digitalItemService.getDigitalItemById(digitalItemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // TODO: add the code for the missing system operations here: 
    // use the corresponding mapping HTTP request annotation with the parameter: "/digitalItemBySession"
    // and create the method findDigitalItemBySession(@RequestParam @NotNull Long digitalSessionId)
    // which call the corresponding findDigitalItemBySession method 
    @GetMapping("/digitalItemBySession")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DigitalItem>> findDigitalItemBySession(@RequestParam @NotNull Long digitalSessionId) {

        log.trace("findDigitalItemBySession");

        // Comprovem si l'element digital existeix, si no, retornem un error 404
        return ResponseEntity.ok(digitalItemService.findDigitalItemBySession(digitalSessionId));
    }

    // TODO: add the code for the missing system operations here: 
    // use the corresponding mapping HTTP request annotation with the parameter: "/addItem"
    // and create the method addDigitalItem(@RequestBody @Valid CreateDigitalItemRequest createDigitalItemRequest)
    // which call the corresponding addDigitalItemm method 
    @PostMapping("/addItem")
    public ResponseEntity<Long> addDigitalItem(@RequestBody @Valid CreateDigitalItemRequest createDigitalItemRequest) {

        log.trace("addDigitalItem");

        // Convertim el request a un objecte DigitalItem per afegir-lo al servei
        DigitalItem digitalItem = DigitalItem.builder()
                .digitalSessionId(createDigitalItemRequest.getDigitalSessionId())
                .description(createDigitalItemRequest.getDescription())
                .link(createDigitalItemRequest.getLink())
                .lat(createDigitalItemRequest.getLat())
                .lon(createDigitalItemRequest.getLon())
                .build();

        // Agafem el ID de l'element digital creat
        Long digitalItemId = digitalItemService.addDigitalItem(digitalItem);

        // Creem la URI per a l'element digital creat per retornar-la a la resposta
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(digitalItemId)
                .toUri();

        // Retornem la resposta amb el codi 201 (creat) i la URI de l'element digital creat
        return ResponseEntity.created(uri).body(digitalItemId);

    }

    // TODO: add the code for the missing system operations here: 
    // use the corresponding mapping HTTP request annotation with the parameter: "/updateItem/{digitalItemId}"
    // and create the method updateDigitalItem(@PathVariable @NotNull Long digitalItemId, @RequestBody @Valid CreateDigitalItemRequest updateDigitalItemRequest)
    // which call the corresponding updateDigitalItem method 
    @PutMapping("/updateItem/{digitalItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DigitalItem> updateDigitalItem(
            @PathVariable @NotNull Long digitalItemId,
            @RequestBody @Valid CreateDigitalItemRequest updateDigitalItemRequest
    )
    {

        log.trace("updateDigitalItem");

        // Actualitzem l'element digital amb les dades del request
        digitalItemService.updateDigitalItem(
                digitalItemId,
                updateDigitalItemRequest.getDescription(),
                updateDigitalItemRequest.getLink(),
                updateDigitalItemRequest.getLat(),
                updateDigitalItemRequest.getLon()
        );

        // Comprovem si l'element digital existeix, si no, retornem un error 404
        DigitalItem digitalItem = digitalItemService.getDigitalItemById(digitalItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Digital item not found"));

        // Retornem l'element digital actualitzat
        return ResponseEntity.ok(digitalItem);

    }

    // TODO: add the code for the missing system operations here: 
    // use the corresponding mapping HTTP request annotation with the parameter: "/reviewDigitalItem/{digitalItemId}"
    // and create the method setDigitalItemForReview(@PathVariable @NotNull Long digitalItemId)
    // which call the corresponding setDigitalItemForReview method
    @PutMapping("/reviewDigitalItem/{digitalItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DigitalItem> setDigitalItemForReview(@PathVariable @NotNull Long digitalItemId) {

        log.trace("setDigitalItemForReview");

        // Actualitzem l'element digital amb les dades del request
        digitalItemService.setDigitalItemForReview(digitalItemId);

        // Comprovem si l'element digital existeix, si no, retornem un error 404
        DigitalItem digitalItem = digitalItemService.getDigitalItemById(digitalItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Digital item not found"));

        // Retornem l'element digital actualitzat
        return ResponseEntity.ok(digitalItem);

    }
    
    // TODO: add the code for the missing system operations here: 
    // use the corresponding mapping HTTP request annotation with the parameter: "/approveDigitalItem/{digitalItemId}"
    // and create the method approvePendingDigitalItem(@PathVariable @NotNull Long digitalItemId)
    // which call the corresponding approvePendingDigitalItem method
    @PutMapping("/approveDigitalItem/{digitalItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DigitalItem> approvePendingDigitalItem(@PathVariable @NotNull Long digitalItemId) {

        log.trace("approvePendingDigitalItem");

        // Actualitzem l'element digital amb les dades del request
        digitalItemService.approvePendingDigitalItem(digitalItemId);

        // Comprovem si l'element digital existeix, si no, retornem un error 404
        DigitalItem digitalItem = digitalItemService.getDigitalItemById(digitalItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Digital item not found"));
        // Retornem l'element digital actualitzat
        return ResponseEntity.ok(digitalItem);
    }

    
    // TODO: add the code for the missing system operations here: 
    // use the corresponding mapping HTTP request annotation with the parameter: "/rejectDigitalItem/{digitalItemId}"
    // and create the method rejectPendingDigitalItem(@PathVariable @NotNull Long digitalItemId)
    // which call the corresponding rejectPendingDigitalItem method    
    @PutMapping("/rejectDigitalItem/{digitalItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DigitalItem> rejectPendingDigitalItem(@PathVariable @NotNull Long digitalItemId) {

        log.trace("rejectPendingDigitalItem");

        // Actualitzem l'element digital amb les dades del request
        digitalItemService.rejectPendingDigitalItem(digitalItemId);

        // Comprovem si l'element digital existeix, si no, retornem un error 404
        DigitalItem digitalItem = digitalItemService.getDigitalItemById(digitalItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Digital item not found"));
        // Retornem l'element digital actualitzat
        return ResponseEntity.ok(digitalItem);
    }

    // TODO: add the code for the missing system operations here: 
    // use the corresponding mapping HTTP request annotation with the parameter: "/dropItem/{digitalItemId}"
    // and create the method dropDigitalItem(@PathVariable @NotNull Long digitalItemId)
    // which call the corresponding dropDigitalItem method 
    @DeleteMapping("/dropItem/{digitalItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> dropDigitalItem(@PathVariable @NotNull Long digitalItemId) {

        log.trace("dropDigitalItem");

        // Comprovem si l'element digital existeix, si no, retornem un error 404
        if (!digitalItemService.getDigitalItemById(digitalItemId).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Eliminem l'element digital
        digitalItemService.dropDigitalItem(digitalItemId);

        // Retornem una resposta buida amb el codi 204 (sense contingut)
        return ResponseEntity.noContent().build();

    }


}
