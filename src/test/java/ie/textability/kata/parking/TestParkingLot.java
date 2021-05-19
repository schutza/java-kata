package ie.textability.kata.parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestParkingLot {

    ParkingLot parkingLot;

    @BeforeEach
    void setUp() {
        int numCompactSpots = 4, numRegularSpots = 4, numLargeSpots = 2;
        parkingLot = new ParkingLot(numCompactSpots, numRegularSpots, numLargeSpots);
    }

    @Test
    void testConstructor() {
        // given
        int numCompactSpots = 4, numRegularSpots = 4, numLargeSpots = 2;
        // when
        ParkingLot parkingLot = new ParkingLot(numCompactSpots, numRegularSpots, numLargeSpots);
        // then
        assertEquals(4, parkingLot.getCompactSpotsCapacity());
        assertEquals(4, parkingLot.getRegularSpotsCapacity());
        assertEquals(2, parkingLot.getLargeSpotsCapacity());
    }

    @Test
    void testWithVanAttemptsToFillLargeSpotsFirst() {
        // given setup
        // when
        parkingLot.withVan().withVan();
        // then
        assertEquals(2, parkingLot.getVehiclesInLargeSpots().size());
        assertEquals(0, parkingLot.getVehiclesInRegularSpots().size());
        assertEquals(0, parkingLot.getVehiclesInCompactSpots().size());
    }

    @Test
    void testWithVanOverflowsIntoRegularSpotsAndTakes3SpotsThere() {
        // given setup
        // when
        parkingLot.withVan().withVan().withVan();
        // then
        assertEquals(2, parkingLot.getVehiclesInLargeSpots().size());
        assertEquals(3, parkingLot.getVehiclesInRegularSpots().size());
        assertEquals(0, parkingLot.getVehiclesInCompactSpots().size());
    }

    @Test
    void testWithCarAttemptsToFillRegularSpotsFirst() {
        // given setup
        // when
        parkingLot.withCar().withCar();
        // then
        assertEquals(2, parkingLot.getVehiclesInRegularSpots().size());
        assertEquals(0, parkingLot.getVehiclesInCompactSpots().size());
        assertEquals(0, parkingLot.getVehiclesInLargeSpots().size());
    }

    @Test
    void testWithCarOverflowsIntoCompactSpots() {
        // given setup
        // when
        parkingLot.withCar().withCar().withCar().withCar().withCar();
        // then
        assertEquals(4, parkingLot.getVehiclesInRegularSpots().size());
        assertEquals(1, parkingLot.getVehiclesInCompactSpots().size());
        assertEquals(0, parkingLot.getVehiclesInLargeSpots().size());
    }

    @Test
    void testWithMotorcycleAttemptsToFillCompactSpotsFirst() {
        // given setup
        // when
        parkingLot.withMotorcycle().withMotorcycle().withMotorcycle();
        // then
        assertEquals(3, parkingLot.getVehiclesInCompactSpots().size());
        assertEquals(0, parkingLot.getVehiclesInRegularSpots().size());
        assertEquals(0, parkingLot.getVehiclesInLargeSpots().size());
    }

    @Test
    void testWithMotorcycleOverflowsIntoRegularSpots() {
        // given setup
        // when
        parkingLot
            .withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle() // compact
            .withMotorcycle().withMotorcycle().withMotorcycle(); // regular
        // then
        assertEquals(4, parkingLot.getVehiclesInCompactSpots().size());
        assertEquals(3, parkingLot.getVehiclesInRegularSpots().size());
        assertEquals(0, parkingLot.getVehiclesInLargeSpots().size());
    }

    @Test
    void testWithMotorcycleOverflowsIntoRegularSpotsThenLargeSpots() {
        // given setup
        // when
        parkingLot
            .withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle() // compact
            .withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle() // regular
            .withMotorcycle().withMotorcycle(); // large
        // then
        assertEquals(4, parkingLot.getVehiclesInCompactSpots().size());
        assertEquals(4, parkingLot.getVehiclesInRegularSpots().size());
        assertEquals(2, parkingLot.getVehiclesInLargeSpots().size());
    }


    @Test
    void testWithVanParkingLotFullExceptionThrown() {
        // given setup
        // when
        Exception exception = assertThrows(ParkingLotFullException.class, () -> {
            parkingLot
                .withVan().withVan() // large
                .withVan() // regular
                .withVan(); // no space left
        });
        // then
        assertEquals(exception.getMessage(), "FULL");
    }

    @Test
    void testWithCarParkingLotFullExceptionThrown() {
        // given setup
        // when
        Exception exception = assertThrows(ParkingLotFullException.class, () -> {
            parkingLot
                .withCar().withCar().withCar().withCar() // regular
                .withCar().withCar().withCar().withCar() // compact
                .withCar(); // no space left
        });
        // then
        assertEquals(exception.getMessage(), "FULL");
    }

    @Test
    void testWithMotorcycleParkingLotFullExceptionThrown() {
        // given setup
        // when
        Exception exception = assertThrows(ParkingLotFullException.class, () -> {
            parkingLot
                .withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle() // compact
                .withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle() // regular
                .withMotorcycle().withMotorcycle() // large
                .withMotorcycle(); // no space left
        });
        // then
        assertEquals(exception.getMessage(), "FULL");
    }

    @Test
    void testGetCompactSpotsCapacity() {
        // given setup
        // when
        int actualCompactSpotsCapacity = parkingLot.getCompactSpotsCapacity();
        // then
        assertEquals(4, actualCompactSpotsCapacity);
    }

    @Test
    void testGetRegularSpotsCapacity() {
        // given setup
        // when
        int actualRegularSpotsCapacity = parkingLot.getRegularSpotsCapacity();
        // then
        assertEquals(4, actualRegularSpotsCapacity);
    }

    @Test
    void testGetLargeSpotsCapacity() {
        // given setup
        // when
        int actualLargeSpotsCapacity = parkingLot.getLargeSpotsCapacity();
        // then
        assertEquals(2, actualLargeSpotsCapacity);
    }

    @Test
    void testGetVehiclesInCompactSpots() {
        // given setup
        parkingLot.withMotorcycle().withMotorcycle();
        List<VehicleType> expectedVehiclesInCompactSpots = List.of(
            VehicleType.MOTORCYCLE, VehicleType.MOTORCYCLE
        );
        // when
        List<VehicleType> actualVehiclesInCompactSpots = parkingLot.getVehiclesInCompactSpots();
        // then
        assertEquals(expectedVehiclesInCompactSpots, actualVehiclesInCompactSpots);
    }

    @Test
    void testGetVehiclesInCompactSpotsWithCarOverflow() {
        // given setup
        parkingLot.withMotorcycle().withMotorcycle()
            .withCar().withCar().withCar().withCar()
            .withCar();
        List<VehicleType> expectedVehiclesInCompactSpots = List.of(
            VehicleType.MOTORCYCLE, VehicleType.MOTORCYCLE, VehicleType.CAR
        );
        // when
        List<VehicleType> actualVehiclesInCompactSpots = parkingLot.getVehiclesInCompactSpots();
        // then
        assertEquals(expectedVehiclesInCompactSpots, actualVehiclesInCompactSpots);
    }

    @Test
    void testGetVehiclesInRegularSpots() {
        // given setup
        parkingLot.withCar().withCar().withCar();
        List<VehicleType> expectedVehiclesInRegularSpots = List.of(
            VehicleType.CAR, VehicleType.CAR, VehicleType.CAR
        );
        // when
        List<VehicleType> actualVehiclesInRegularSpots = parkingLot.getVehiclesInRegularSpots();
        // then
        assertEquals(expectedVehiclesInRegularSpots, actualVehiclesInRegularSpots);
    }

    @Test
    void testGetVehiclesInRegularSpotsWithVanOverflow() {
        // given setup
        parkingLot.withCar().withVan().withVan().withVan();
        List<VehicleType> expectedVehiclesInRegularSpots = List.of(
            VehicleType.CAR,
            VehicleType.VAN, VehicleType.VAN, VehicleType.VAN
        );
        // when
        List<VehicleType> actualVehiclesInRegularSpots = parkingLot.getVehiclesInRegularSpots();
        // then
        assertEquals(expectedVehiclesInRegularSpots, actualVehiclesInRegularSpots);
    }

    @Test
    void testGetVehiclesInRegularSpotsWithMotorcycleOverflow() {
        // given setup
        parkingLot.withCar().withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle();
        List<VehicleType> expectedVehiclesInRegularSpots = List.of(
            VehicleType.CAR, VehicleType.MOTORCYCLE
        );
        // when
        List<VehicleType> actualVehiclesInRegularSpots = parkingLot.getVehiclesInRegularSpots();
        // then
        assertEquals(expectedVehiclesInRegularSpots, actualVehiclesInRegularSpots);
    }

    @Test
    void testGetVehiclesInLargeSpots() {
        // given setup
        parkingLot.withVan();
        List<VehicleType> expectedVehiclesInLargeSpots = List.of(
            VehicleType.VAN
        );
        // when
        List<VehicleType> actualVehiclesInLargeSpots = parkingLot.getVehiclesInLargeSpots();
        // then
        assertEquals(expectedVehiclesInLargeSpots, actualVehiclesInLargeSpots);
    }

    @Test
    void testGetVehiclesInLargeSpotsWithMotorcycleOverflow() {
        // given setup
        parkingLot.withVan().withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle()
            .withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle()
            .withMotorcycle();
        List<VehicleType> expectedVehiclesInLargeSpots = List.of(
            VehicleType.VAN, VehicleType.MOTORCYCLE
        );
        // when
        List<VehicleType> actualVehiclesInLargeSpots = parkingLot.getVehiclesInLargeSpots();
        // then
        assertEquals(expectedVehiclesInLargeSpots, actualVehiclesInLargeSpots);
    }

}
