package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.exceptions.PlaceNotFound;
import kr.megaptera.smash.services.GetPlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("places")
public class PlaceController {
    private final GetPlaceService getPlaceService;

    public PlaceController(GetPlaceService getPlaceService) {
        this.getPlaceService = getPlaceService;
    }

    @GetMapping("{placeId}")
    public PlaceDto place(
        @PathVariable Long placeId
    ) {
        return getPlaceService.getTargetPlace(placeId);
    }

    @ExceptionHandler(PlaceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String placeNotFound() {
        return "Place Not Found";
    }
}
