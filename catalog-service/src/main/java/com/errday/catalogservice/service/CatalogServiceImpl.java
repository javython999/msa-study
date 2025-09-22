package com.errday.catalogservice.service;

import com.errday.catalogservice.dto.CatalogDto;
import com.errday.catalogservice.jpa.CatalogEntity;
import com.errday.catalogservice.jpa.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public CatalogDto findByProductId(String productId) {
        CatalogEntity catalogEntity = catalogRepository.findByProductId(productId)
                .orElseThrow(() -> new NoSuchElementException("catalog not found by productId " + productId));

        return modelMapper.map(catalogEntity, CatalogDto.class);
    }

    @Override
    public List<CatalogDto> findAll() {
        return catalogRepository.findAll()
                .stream()
                .map(catalogEntity -> modelMapper.map(catalogEntity, CatalogDto.class))
                .toList();
    }
}
