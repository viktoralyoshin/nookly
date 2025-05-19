package com.nookly.booking.yandex;

import com.fasterxml.jackson.databind.JsonNode;
import com.nookly.booking.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoCoderService {

    private static final String GEOCODE_URL =
            "https://geocode-maps.yandex.ru/1.x/?format=json&lang=ru_RU&apikey={apiKey}&geocode={lon},{lat}";

    @Value("${yandex.geocoder.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GeoCoderService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public BoundingBox getCityBounds(double lat, double lon) {
        String url = GEOCODE_URL
                .replace("{lat}", String.valueOf(lat))
                .replace("{lon}", String.valueOf(lon))
                .replace("{apiKey}", apiKey);

        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        assert response != null;

        // Ищем объект с типом "locality" (город) или "province" с именем "Москва"
        for (JsonNode feature : response.path("response").path("GeoObjectCollection").path("featureMember")) {
            JsonNode geoObject = feature.path("GeoObject");
            String kind = geoObject.path("metaDataProperty")
                    .path("GeocoderMetaData")
                    .path("kind")
                    .asText();

            if ("locality".equals(kind) || ("province".equals(kind))) {
                JsonNode envelope = geoObject.path("boundedBy").path("Envelope");
                String[] lowerCorner = envelope.path("lowerCorner").asText().split(" ");
                String[] upperCorner = envelope.path("upperCorner").asText().split(" ");

                return new BoundingBox(
                        Double.parseDouble(lowerCorner[1]), // minLat
                        Double.parseDouble(lowerCorner[0]), // minLon
                        Double.parseDouble(upperCorner[1]), // maxLat
                        Double.parseDouble(upperCorner[0])  // maxLon
                );
            }
        }

        return null;
    }

    public static class BoundingBox {
        private final double minLat;
        private final double minLon;
        private final double maxLat;
        private final double maxLon;

        public BoundingBox(double minLat, double minLon, double maxLat, double maxLon) {
            this.minLat = minLat;
            this.minLon = minLon;
            this.maxLat = maxLat;
            this.maxLon = maxLon;
        }

        public double getMinLat() {
            return minLat;
        }

        public double getMinLon() {
            return minLon;
        }

        public double getMaxLat() {
            return maxLat;
        }

        public double getMaxLon() {
            return maxLon;
        }
    }
}