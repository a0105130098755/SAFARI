package service;

import java.util.List;

public class PlaceService {
    private PlaceRepository placeRepository;
    private LocationService locationService;
    private CategoryService categoryService;

    // 의존성 주입을 위한 생성자
    public PlaceService(PlaceRepository placeRepository, LocationService locationService, CategoryService categoryService) {
        this.placeRepository = placeRepository;
        this.locationService = locationService;
        this.categoryService = categoryService;
    }

    public List<Place> getPlacesByLocation(Location location) {
        return placeRepository.findByLocation(location);
    }

    public List<Place> getPlacesByCategory(Category category) {
        return placeRepository.findByCategory(category);
    }

    public List<Place> searchPlaces(String locationName, String categoryName) {
        Location location = locationService.getLocationByName(locationName);
        Category category = categoryService.getCategoryByName(categoryName);
        if (location == null || category == null) {
            throw new EntityNotFoundException("Location or Category not found");
        }
        // This method assumes the existence of findByLocationAndCategory method in PlaceRepository.
        // If not present, you would need to implement it or adjust this call accordingly.
        return placeRepository.findByLocationAndCategory(location, category);
    }
}
