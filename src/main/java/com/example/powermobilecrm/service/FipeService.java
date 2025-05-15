package com.example.powermobilecrm.service;

import com.example.powermobilecrm.dto.fipe.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class FipeService {

    private static final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/carros";
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(FipeService.class);

    public FipeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("brands")
    public List<FipeBrandDTO> getFipeBrands() {
        String url = BASE_URL + "/marcas";
        return List.of(restTemplate.getForObject(url, FipeBrandDTO[].class));
    }

    @Cacheable(value = "models", key = "#brandId")
    public List<FipeModelDTO> getFipeModels(String brandId) {
        String url = BASE_URL + "/marcas/" + brandId + "/modelos";
        FipeWrapperDTO wrapper = restTemplate.getForObject(url, FipeWrapperDTO.class);
        return wrapper.modelos();
    }

    @Cacheable(value = "fipePrice", key = "#brandId + '-' + #modelId + '-' + #yearCode")
    public BigDecimal getFipePrice(String brandId, String modelId, String yearCode) {
        log.info("Consultando API FIPE para brand={}, model={}, ano={}", brandId, modelId, yearCode);

        String url = BASE_URL + "/marcas/" + brandId + "/modelos/" + modelId + "/anos/" + yearCode;
        FipePriceDTO dto = restTemplate.getForObject(url, FipePriceDTO.class);

        if (dto != null && dto.valor() != null) {
            String clean = dto.valor().replace("R$", "").replace(".", "").replace(",", ".").trim();
            try {
                return new BigDecimal(clean);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Erro ao converter valor da FIPE: " + dto.valor());
            }
        }

        return null;
    }

    public String getBrandName(String brandId) {
        List<FipeBrandDTO> brands = getFipeBrands();
        return brands.stream()
                .filter(b -> b.codigo().equals(brandId))
                .findFirst()
                .map(FipeBrandDTO::nome)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Marca inválida"));
    }

    public String getModelName(String brandId, String modelId) {
        List<FipeModelDTO> modelos = getFipeModels(brandId);
        return modelos.stream()
                .filter(m -> m.codigo().equals(modelId))
                .findFirst()
                .map(FipeModelDTO::nome)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modelo inválido"));
    }

    public void validateYear(String brandId, String modelId, String yearFipeCode) {
        List<FipeYearDTO> years = getYears(brandId, modelId);
        boolean exists = years.stream().anyMatch(y -> y.codigo().equals(yearFipeCode));
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ano inválido na FIPE");
        }
    }

    @Cacheable(value = "fipeYears", key = "#brandId + '-' + #modelId")
    public List<FipeYearDTO> getYears(String brandId, String modelId) {
        String url = BASE_URL + "/marcas/" + brandId + "/modelos/" + modelId + "/anos";
        FipeYearDTO[] years = restTemplate.getForObject(url, FipeYearDTO[].class);
        return years != null ? Arrays.asList(years) : Collections.emptyList();
    }

}
