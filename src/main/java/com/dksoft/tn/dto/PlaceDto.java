package com.dksoft.tn.dto;

import java.util.List;

public record PlaceDto(
        long id,
        String address,
        String mapsLink,
        List<String> images,
        int maxCapacity
) {
}