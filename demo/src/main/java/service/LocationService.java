package service;

public class LocationService {
    private LocationRepository locationRepository;

    // 의존성 주입을 위한 생성자
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationByName(String name) {
        Location location = locationRepository.findByName(name);
        if (location == null) {
            throw new EntityNotFoundException("Location not found with name: " + name);
        }
        return location;
    }
}
