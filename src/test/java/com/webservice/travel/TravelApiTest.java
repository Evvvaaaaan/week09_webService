package com.webservice.travel;

import com.webservice.travel.dto.TravelRequest;
import com.webservice.travel.dto.TravelResponse;
import com.webservice.travel.repository.TravelRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TravelApiTest {
    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private TravelRepository repository;

    @BeforeEach
    void clearTestDatabase() {
        repository.deleteAll();
    }

    @Test
    void createsReadsUpdatesAndDeletesTravelOverHttp() {
        ResponseEntity<TravelResponse> created = rest.postForEntity(
                "/api/travels", request(), TravelResponse.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TravelResponse body = created.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(created.getHeaders().getLocation().getPath()).isEqualTo("/api/travels/" + body.id());
        String path = "/api/travels/" + body.id();

        ResponseEntity<TravelResponse[]> all = rest.getForEntity("/api/travels", TravelResponse[].class);
        assertThat(all.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(all.getBody()).containsExactly(body);
        ResponseEntity<TravelResponse> single = rest.getForEntity(path, TravelResponse.class);
        assertThat(single.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(single.getBody()).isEqualTo(body);

        TravelRequest update = new TravelRequest("Kyoto temples", "Japan", "Kyoto",
                LocalDate.of(2027, 2, 15), 5, new BigDecimal("900000.00"));
        ResponseEntity<TravelResponse> updated = rest.exchange(path, HttpMethod.PUT,
                new HttpEntity<>(update), TravelResponse.class);
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        TravelResponse expected = new TravelResponse(body.id(), update.destination(), update.country(),
                update.city(), update.departureDate(), update.durationDays(), update.budget());
        assertThat(updated.getBody()).isEqualTo(expected);
        assertThat(rest.getForObject(path, TravelResponse.class)).isEqualTo(expected);
        assertThat(repository.findById(body.id()).orElseThrow().getCity()).isEqualTo("Kyoto");

        ResponseEntity<Void> deleted = rest.exchange(path, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(repository.findById(body.id())).isEmpty();
        assertThat(rest.getForEntity(path, String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(rest.getForObject("/api/travels", TravelResponse[].class)).isEmpty();
    }

    @Test
    void returnsNotFoundForMissingTravel() {
        String path = "/api/travels/999999";
        assertThat(rest.getForEntity(path, String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(rest.exchange(path, HttpMethod.PUT, new HttpEntity<>(request()), String.class)
                .getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(rest.exchange(path, HttpMethod.DELETE, HttpEntity.EMPTY, String.class)
                .getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void rejectsInvalidCreateAndUpdateWithoutChangingStoredData() {
        TravelRequest invalid = new TravelRequest(" ", "South Korea", "Jeju",
                LocalDate.of(2027, 1, 10), 0, new BigDecimal("-1.00"));
        assertThat(rest.postForEntity("/api/travels", invalid, String.class).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(repository.count()).isZero();

        TravelResponse created = rest.postForObject("/api/travels", request(), TravelResponse.class);
        String path = "/api/travels/" + created.id();
        assertThat(rest.exchange(path, HttpMethod.PUT, new HttpEntity<>(invalid), String.class)
                .getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(rest.getForObject(path, TravelResponse.class)).isEqualTo(created);
    }

    private TravelRequest request() {
        return new TravelRequest("Jeju Island", "South Korea", "Jeju",
                LocalDate.of(2027, 1, 10), 3, new BigDecimal("350000.00"));
    }
}
