package com.webservice.travel.service;

import com.webservice.travel.dto.TravelRequest;
import com.webservice.travel.dto.TravelResponse;
import com.webservice.travel.entity.Travel;
import com.webservice.travel.repository.TravelRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class TravelService {
    private final TravelRepository repository;

    public TravelService(TravelRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TravelResponse create(TravelRequest request) {
        Travel travel = new Travel(request.destination(), request.country(), request.city(),
                request.departureDate(), request.durationDays(), request.budget());
        return TravelResponse.from(repository.save(travel));
    }

    public List<TravelResponse> findAll() {
        return repository.findAll().stream().map(TravelResponse::from).toList();
    }

    public TravelResponse findById(Long id) {
        return TravelResponse.from(findTravel(id));
    }

    @Transactional
    public TravelResponse update(Long id, TravelRequest request) {
        Travel travel = findTravel(id);
        travel.update(request.destination(), request.country(), request.city(),
                request.departureDate(), request.durationDays(), request.budget());
        return TravelResponse.from(repository.save(travel));
    }

    @Transactional
    public void delete(Long id) {
        findTravel(id);
        repository.deleteById(id);
    }

    private Travel findTravel(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Travel not found"));
    }
}
