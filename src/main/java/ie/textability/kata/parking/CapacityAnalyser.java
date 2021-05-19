package ie.textability.kata.parking;

import java.util.Map;

public class CapacityAnalyser {

    private final ParkingLot parkingLot;

    private Map<VehicleType, ParkingSpotType> vehicleTypeToParkingSpot = Map.ofEntries(
        Map.entry(VehicleType.MOTORCYCLE, ParkingSpotType.COMPACT),
        Map.entry(VehicleType.CAR, ParkingSpotType.REGULAR),
        Map.entry(VehicleType.VAN, ParkingSpotType.LARGE)
    );

    public CapacityAnalyser(ParkingLot parkingLot){
        this.parkingLot = parkingLot;
    }

    public boolean isEmpty() {
        if (parkingLot.getVehiclesInCompactSpots().isEmpty() && parkingLot.getVehiclesInRegularSpots().isEmpty() && parkingLot.getVehiclesInLargeSpots().isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if (
            parkingLot.getVehiclesInCompactSpots().size() >= parkingLot.getCompactSpotsCapacity()
                && parkingLot.getVehiclesInRegularSpots().size() >= parkingLot.getRegularSpotsCapacity()
                && parkingLot.getVehiclesInLargeSpots().size() >= parkingLot.getLargeSpotsCapacity()
        ) {
            return true;
        }
        return false;
    }

    /*
     * Returns total capacity by spot category.
     * Clients may decide how to translate or aggregate that into a single value.
     */
    public Map<ParkingSpotType, Integer> totalCapacity() {
        Map<ParkingSpotType, Integer> totalCapacity = Map.ofEntries(
            Map.entry(ParkingSpotType.COMPACT, parkingLot.getCompactSpotsCapacity()),
            Map.entry(ParkingSpotType.REGULAR, parkingLot.getRegularSpotsCapacity()),
            Map.entry(ParkingSpotType.LARGE, parkingLot.getLargeSpotsCapacity())
        );
        return totalCapacity;
    }

    /*
     * Returns remaining capacity by spot category.
     * Clients may decide how to translate or aggregate that into a single value.
     */
    public Map<ParkingSpotType, Integer> remainingCapacity() {
        Map<ParkingSpotType, Integer> remainingCapacity = Map.ofEntries(
            Map.entry(ParkingSpotType.COMPACT, parkingLot.getCompactSpotsCapacity() - parkingLot.getVehiclesInCompactSpots().size()),
            Map.entry(ParkingSpotType.REGULAR, parkingLot.getRegularSpotsCapacity() - parkingLot.getVehiclesInRegularSpots().size()),
            Map.entry(ParkingSpotType.LARGE, parkingLot.getLargeSpotsCapacity() - parkingLot.getVehiclesInLargeSpots().size())
        );
        return remainingCapacity;
    }

    public boolean isFullFor(VehicleType vehicleType) {
        ParkingSpotType parkingSpotType = vehicleTypeToParkingSpot.get(vehicleType);
        return remainingCapacity().get(parkingSpotType) <= 0;
    }

    public int occupancyByVehicleType(VehicleType vehicleType) {
        int occupancy = 0;
        switch (vehicleType) {
            case VAN:
                occupancy = occupancyForVan();
                break;
            case CAR:
                occupancy = occupancyForCar();
                break;
            case MOTORCYCLE:
                occupancy = occupancyForMotorcycle();
                break;
        }
        return occupancy;
    }

    private int occupancyForVan() {
        VehicleType vehicleType = VehicleType.VAN;
        // check in large spots
        int vanOccupancyLargeSpots = parkingLot.getVehiclesInLargeSpots()
            .stream().filter(parkedVehicleType -> parkedVehicleType == vehicleType)
            .reduce(0, (subtotal, element) -> subtotal + 1, Integer::sum);
        // check in regular spots
        int vanOccupancyRegularSpots = parkingLot.getVehiclesInRegularSpots()
            .stream().filter(parkedVehicleType -> parkedVehicleType == vehicleType)
            .reduce(0, (subtotal, element) -> subtotal + 1, Integer::sum);

        return vanOccupancyLargeSpots + (vanOccupancyRegularSpots / 3);
    }

    private int occupancyForCar() {
        VehicleType vehicleType = VehicleType.CAR;
        // check in large spots
        int carOccupancyRegularSpots = parkingLot.getVehiclesInRegularSpots()
            .stream().filter(parkedVehicleType -> parkedVehicleType == vehicleType)
            .reduce(0, (subtotal, element) -> subtotal + 1, Integer::sum);
        // check in regular spots
        int carOccupancyCompactSpots = parkingLot.getVehiclesInCompactSpots()
            .stream().filter(parkedVehicleType -> parkedVehicleType == vehicleType)
            .reduce(0, (subtotal, element) -> subtotal + 1, Integer::sum);

        return carOccupancyRegularSpots + carOccupancyCompactSpots;
    }

    private int occupancyForMotorcycle() {
        VehicleType vehicleType = VehicleType.MOTORCYCLE;
        // check in all spots - compact spots first
        int motorcycleOccupancyCompactSpots = parkingLot.getVehiclesInCompactSpots()
            .stream().filter(parkedVehicleType -> parkedVehicleType == vehicleType)
            .reduce(0, (subtotal, element) -> subtotal + 1, Integer::sum);
        // check in all spots - regular spots first
        int motorcycleOccupancyRegularSpots = parkingLot.getVehiclesInRegularSpots()
            .stream().filter(parkedVehicleType -> parkedVehicleType == vehicleType)
            .reduce(0, (subtotal, element) -> subtotal + 1, Integer::sum);
        // check in all spots - large spots first
        int motorcycleOccupancyLargeSpots = parkingLot.getVehiclesInLargeSpots()
            .stream().filter(parkedVehicleType -> parkedVehicleType == vehicleType)
            .reduce(0, (subtotal, element) -> subtotal + 1, Integer::sum);

        return motorcycleOccupancyCompactSpots + motorcycleOccupancyRegularSpots + motorcycleOccupancyLargeSpots;
    }
}
