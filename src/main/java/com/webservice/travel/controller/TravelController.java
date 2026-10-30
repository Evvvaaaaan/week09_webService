package com.webservice.travel.controller;

import com.webservice.travel.dto.TravelRequest;
import com.webservice.travel.dto.TravelResponse;
import com.webservice.travel.service.TravelService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/travels")
public class TravelController {
    private final TravelService service;

    public TravelController(TravelService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TravelResponse> create(@Valid @RequestBody TravelRequest request) {
        TravelResponse response = service.create(request);
        return ResponseEntity.created(URI.create("/api/travels/" + response.id())).body(response);
    }

    @GetMapping
    public List<TravelResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TravelResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public TravelResponse update(@PathVariable Long id, @Valid @RequestBody TravelRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
