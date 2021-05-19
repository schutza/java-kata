package ie.textability.kata.parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        assertEquals(2, parkingLot.getLargeSpots().size());
        assertEquals(0, parkingLot.getRegularSpots().size());
        assertEquals(0, parkingLot.getCompactSpots().size());
    }

    @Test
    void testWithVanOverflowsIntoRegularSpotsAndTakes3SpotsThere() {
        // given setup
        // when
        parkingLot.withVan().withVan().withVan();
        // then
        assertEquals(2, parkingLot.getLargeSpots().size());
        assertEquals(3, parkingLot.getRegularSpots().size());
        assertEquals(0, parkingLot.getCompactSpots().size());
    }

    @Test
    void testWithCarAttemptsToFillRegularSpotsFirst() {
        // given setup
        // when
        parkingLot.withCar().withCar();
        // then
        assertEquals(2, parkingLot.getRegularSpots().size());
        assertEquals(0, parkingLot.getCompactSpots().size());
        assertEquals(0, parkingLot.getLargeSpots().size());
    }

    @Test
    void testWithCarOverflowsIntoCompactSpots() {
        // given setup
        // when
        parkingLot.withCar().withCar().withCar().withCar().withCar();
        // then
        assertEquals(4, parkingLot.getRegularSpots().size());
        assertEquals(1, parkingLot.getCompactSpots().size());
        assertEquals(0, parkingLot.getLargeSpots().size());
    }

    @Test
    void testWithMotorcycleAttemptsToFillCompactSpotsFirst() {
        // given setup
        // when
        parkingLot.withMotorcycle().withMotorcycle().withMotorcycle();
        // then
        assertEquals(3, parkingLot.getCompactSpots().size());
        assertEquals(0, parkingLot.getRegularSpots().size());
        assertEquals(0, parkingLot.getLargeSpots().size());
    }

    @Test
    void testWithMotorcycleOverflowsIntoRegularSpots() {
        // given setup
        // when
        parkingLot
            .withMotorcycle().withMotorcycle().withMotorcycle().withMotorcycle() // compact
            .withMotorcycle().withMotorcycle().withMotorcycle(); // regular
        // then
        assertEquals(4, parkingLot.getCompactSpots().size());
        assertEquals(3, parkingLot.getRegularSpots().size());
        assertEquals(0, parkingLot.getLargeSpots().size());
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
        assertEquals(4, parkingLot.getCompactSpots().size());
        assertEquals(4, parkingLot.getRegularSpots().size());
        assertEquals(2, parkingLot.getLargeSpots().size());
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

}
