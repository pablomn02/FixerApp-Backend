package org.example.fixerappbackend.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/geolocalizacion")
public class GeolocalizacionController {

    @GetMapping("/reverse")
    public ResponseEntity<?> obtenerDireccion(@RequestParam double lat, @RequestParam double lon) {
        String url = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat="
                + lat + "&lon=" + lon;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "FixerApp/1.0 (pablomnavarro9@gmail.com)");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la dirección");
        }
    }
}
