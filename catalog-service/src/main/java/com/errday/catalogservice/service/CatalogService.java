package com.errday.catalogservice.service;

import com.errday.catalogservice.dto.CatalogDto;

import java.util.List;

public interface CatalogService {
    CatalogDto findByProductId(String productId);
    List<CatalogDto> findAll();
}
