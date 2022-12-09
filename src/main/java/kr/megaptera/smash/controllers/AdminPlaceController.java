package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.PlacesDto;
import kr.megaptera.smash.services.GetPlacesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("places")
public class AdminPlaceController {
    private final GetPlacesService getPlacesService;

    public AdminPlaceController(GetPlacesService getPlacesService) {
        this.getPlacesService = getPlacesService;
    }

    @GetMapping
    public PlacesDto places() {
        return getPlacesService.findAllPlaces();
    }
}
