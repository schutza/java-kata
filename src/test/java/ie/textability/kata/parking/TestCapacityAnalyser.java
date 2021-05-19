package ie.textability.kata.parking;

import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestCapacityAnalyser {

    @Test
    void testIsEmptyWithEmptyParkingLot() {
        // given
        ParkingLot emptyParkingLot = new ParkingLot(3, 2, 2);
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(emptyParkingLot);
        // when
        boolean actualIsEmpty = capacityAnalyser.isEmpty();
        // then
        assertTrue(actualIsEmpty, "ParkingLot is not empty");
    }

    @Test
    void testIsEmptyWithNonEmptyParkingLot() {
        // given
        ParkingLot somewhatOccupiedParkingLot = new ParkingLot(3, 2,2)
            .withMotorcycle().withCar().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(somewhatOccupiedParkingLot);
        // when
        boolean actualIsEmpty = capacityAnalyser.isEmpty();
        // then
        assertFalse(actualIsEmpty, "ParkingLot is empty");
    }

    @Test
    void testIsFullWithFullParkingLot() {
        // given
        ParkingLot fullParkingLot = new ParkingLot(3, 2,2)
            .withMotorcycle().withMotorcycle().withMotorcycle().withCar().withCar().withVan().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(fullParkingLot);
        // when
        boolean actualIsFull = capacityAnalyser.isFull();
        // then
        assertTrue(actualIsFull, "ParkingLot is not full");
    }

    @Test
    void testIsFullWithNonFullParkingLot() {
        // given
        ParkingLot somewhatOccupiedParkingLot = new ParkingLot(3, 2,2)
            .withMotorcycle().withCar().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(somewhatOccupiedParkingLot);
        // when
        boolean actualIsFull = capacityAnalyser.isFull();
        // then
        assertFalse(actualIsFull, "ParkingLot is full");
    }

    @Test
    void testTotalCapacity() {
        // given
        int numCompactSpots = 3, numRegularSpots = 2, numLargeSpots = 2;
        ParkingLot somewhatOccupiedParkingLot = new ParkingLot(numCompactSpots, numRegularSpots,numLargeSpots)
            .withMotorcycle().withCar().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(somewhatOccupiedParkingLot);
        // when
        Map<ParkingSpotType, Integer> actual = capacityAnalyser.totalCapacity();
        // then
        assertEquals(actual.get(ParkingSpotType.COMPACT), numCompactSpots);
        assertEquals(actual.get(ParkingSpotType.REGULAR), numRegularSpots);
        assertEquals(actual.get(ParkingSpotType.LARGE), numLargeSpots);
    }

    @Test
    void testRemainingCapacityWithEmptyParkingLot() {
        // given
        int numCompactSpots = 3, numRegularSpots = 2, numLargeSpots = 2;
        ParkingLot emptyParkingLot = new ParkingLot(numCompactSpots, numRegularSpots,numLargeSpots);
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(emptyParkingLot);
        // when
        Map<ParkingSpotType, Integer> actualRemainingCapacity = capacityAnalyser.remainingCapacity();
        // then
        assertEquals(actualRemainingCapacity.get(ParkingSpotType.COMPACT), numCompactSpots);
        assertEquals(actualRemainingCapacity.get(ParkingSpotType.REGULAR), numRegularSpots);
        assertEquals(actualRemainingCapacity.get(ParkingSpotType.LARGE), numLargeSpots);
    }

    @Test
    void testRemainingCapacityWithFullParkingLot() {
        // given
        int numCompactSpots = 3, numRegularSpots = 2, numLargeSpots = 2;
        ParkingLot fullParkingLot = new ParkingLot(numCompactSpots, numRegularSpots,numLargeSpots)
            .withMotorcycle().withMotorcycle().withMotorcycle().withCar().withCar().withVan().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(fullParkingLot);
        // when
        Map<ParkingSpotType, Integer> actualRemainingCapacity = capacityAnalyser.remainingCapacity();
        // then
        assertEquals(actualRemainingCapacity.get(ParkingSpotType.COMPACT), 0);
        assertEquals(actualRemainingCapacity.get(ParkingSpotType.REGULAR), 0);
        assertEquals(actualRemainingCapacity.get(ParkingSpotType.LARGE), 0);
    }

    @Test
    void testRemainingCapacityWithSomewhatOccupiedParkingLot() {
        // given
        int numCompactSpots = 3, numRegularSpots = 2, numLargeSpots = 2;
        ParkingLot somewhatOccupiedParkingLot = new ParkingLot(numCompactSpots, numRegularSpots,numLargeSpots)
            .withMotorcycle().withCar().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(somewhatOccupiedParkingLot);
        // when
        Map<ParkingSpotType, Integer> actualRemainingCapacity = capacityAnalyser.remainingCapacity();
        // then
        assertEquals(actualRemainingCapacity.get(ParkingSpotType.COMPACT), 2);
        assertEquals(actualRemainingCapacity.get(ParkingSpotType.REGULAR), 1);
        assertEquals(actualRemainingCapacity.get(ParkingSpotType.LARGE), 1);
    }

    @Test
    void testIsFullForVans() {
        // given
        int numCompactSpots = 3, numRegularSpots = 2, numLargeSpots = 2;
        ParkingLot somewhatOccupiedParkingLotFullForVans = new ParkingLot(numCompactSpots, numRegularSpots,
            numLargeSpots)
            .withMotorcycle().withCar().withVan().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(somewhatOccupiedParkingLotFullForVans);
        // when
        boolean actualIsFullForMotorcycles = capacityAnalyser.isFullFor(VehicleType.MOTORCYCLE);
        boolean actualIsFullForCars = capacityAnalyser.isFullFor(VehicleType.CAR);
        boolean actualIsFullForVans = capacityAnalyser.isFullFor(VehicleType.VAN);
        // then
        assertTrue(actualIsFullForVans, "Van Capacity is "+ capacityAnalyser.remainingCapacity());
        assertFalse(actualIsFullForMotorcycles);
        assertFalse(actualIsFullForCars);
    }

    @Test
    void testIsFullForCars() {
        // given
        int numCompactSpots = 3, numRegularSpots = 2, numLargeSpots = 2;
        ParkingLot somewhatOccupiedParkingLotFullForCars = new ParkingLot(numCompactSpots, numRegularSpots,
            numLargeSpots)
            .withMotorcycle().withCar().withCar().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(somewhatOccupiedParkingLotFullForCars);
        // when
        boolean actualIsFullForMotorcycles = capacityAnalyser.isFullFor(VehicleType.MOTORCYCLE);
        boolean actualIsFullForCars = capacityAnalyser.isFullFor(VehicleType.CAR);
        boolean actualIsFullForVans = capacityAnalyser.isFullFor(VehicleType.VAN);
        // then
        assertTrue(actualIsFullForCars, "Capacity is "+ capacityAnalyser.remainingCapacity());
        assertFalse(actualIsFullForMotorcycles);
        assertFalse(actualIsFullForVans);
    }

    @Test
    void testIsFullForMotorcycles() {
        // given
        int numCompactSpots = 3, numRegularSpots = 2, numLargeSpots = 2;
        ParkingLot somewhatOccupiedParkingLotFullForMotorcycles = new ParkingLot(numCompactSpots, numRegularSpots,
            numLargeSpots)
            .withMotorcycle().withMotorcycle().withMotorcycle().withCar().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(somewhatOccupiedParkingLotFullForMotorcycles);
        // when
        boolean actualIsFullForMotorcycles = capacityAnalyser.isFullFor(VehicleType.MOTORCYCLE);
        boolean actualIsFullForCars = capacityAnalyser.isFullFor(VehicleType.CAR);
        boolean actualIsFullForVans = capacityAnalyser.isFullFor(VehicleType.VAN);
        // then
        assertTrue(actualIsFullForMotorcycles, "Capacity is "+ capacityAnalyser.remainingCapacity());
        assertFalse(actualIsFullForCars);
        assertFalse(actualIsFullForVans);
    }

    @Test
    void testOverflowVan() {
        // given
        int numCompactSpots = 3, numRegularSpots = 4, numLargeSpots = 2;
        ParkingLot occupiedParkingLotOverflownWithVans = new ParkingLot(numCompactSpots, numRegularSpots,
            numLargeSpots)
            .withVan().withVan().withCar().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(occupiedParkingLotOverflownWithVans);
        // when
        boolean actualIsFullForVans = capacityAnalyser.isFullFor(VehicleType.VAN);
        // then
        assertTrue(actualIsFullForVans, "Capacity is "+ capacityAnalyser.remainingCapacity());
    }

    @Test
    void testVanOccupancy() {
        // given
        int numCompactSpots = 3, numRegularSpots = 5, numLargeSpots = 2;
        ParkingLot occupiedParkingLotOverflownWithVans = new ParkingLot(numCompactSpots, numRegularSpots,
            numLargeSpots)
            .withVan().withVan().withCar().withVan();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(occupiedParkingLotOverflownWithVans);
        // when
        int actualVanOccupancy = capacityAnalyser.occupancyByVehicleType(VehicleType.VAN);
        // then
        assertEquals(3, actualVanOccupancy);
    }

    @Test
    void testCarOccupancy() {
        // given
        int numCompactSpots = 3, numRegularSpots = 5, numLargeSpots = 2;
        ParkingLot occupiedParkingLotOverflownWithVans = new ParkingLot(numCompactSpots, numRegularSpots,
            numLargeSpots)
            .withVan().withVan().withCar().withVan().withCar().withCar().withCar().withMotorcycle();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(occupiedParkingLotOverflownWithVans);
        // when
        int actualVanOccupancy = capacityAnalyser.occupancyByVehicleType(VehicleType.CAR);
        // then
        assertEquals(4, actualVanOccupancy);
    }

    @Test
    void testMotorcycleOccupancy() {
        // given
        int numCompactSpots = 3, numRegularSpots = 5, numLargeSpots = 2;
        ParkingLot occupiedParkingLotOverflownWithVans = new ParkingLot(numCompactSpots, numRegularSpots,
            numLargeSpots)
            .withVan().withVan().withCar().withVan().withCar().withCar().withCar().withMotorcycle();
        CapacityAnalyser capacityAnalyser = new CapacityAnalyser(occupiedParkingLotOverflownWithVans);
        // when
        int actualMotorcycleOccupancy = capacityAnalyser.occupancyByVehicleType(VehicleType.MOTORCYCLE);
        // then
        assertEquals(1, actualMotorcycleOccupancy);
    }

}
