/*
Design a parking lot using object-oriented principles

Goals:
- Your solution should be in Java - if you would like to use another language, please let the interviewer know.
- Boilerplate is provided. Feel free to change the code as you see fit

Assumptions:
- The parking lot can hold motorcycles, cars and vans
- The parking lot has motorcycle spots, car spots and large spots
- A motorcycle can park in any spot
- A car can park in a single compact spot, or a regular spot
- A van can park, but it will take up 3 regular spots
- These are just a few assumptions. Feel free to ask your interviewer about more assumptions as needed

Here are a few methods that you should be able to run:
- Tell us how many spots are remaining
- Tell us how many total spots are in the parking lot
- Tell us when the parking lot is full
- Tell us when the parking lot is empty
- Tell us when certain spots are full e.g. when all motorcycle spots are taken
- Tell us how many spots vans are taking up

Hey candidate! Welcome to your interview. I'll start off by giving you a Solution class. To run the code at any time, please hit the run button located in the top left corner.
*/
package ie.textability.kata.parking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParkingLot {

    private Map<ParkingSpotType, Integer> maxCapacity;
    private Map<ParkingSpotType, List<VehicleType>> occupiedSpace;

    public ParkingLot(int numCompactSpots, int numRegularSpots, int numLargeSpots) {
        maxCapacity = Map.ofEntries(
            Map.entry(ParkingSpotType.COMPACT, numCompactSpots),
            Map.entry(ParkingSpotType.REGULAR, numRegularSpots),
            Map.entry(ParkingSpotType.LARGE, numLargeSpots)
        );
        occupiedSpace = Map.ofEntries(
            Map.entry(ParkingSpotType.COMPACT, new ArrayList<VehicleType>(numCompactSpots)),
            Map.entry(ParkingSpotType.REGULAR, new ArrayList<VehicleType>(numRegularSpots)),
            Map.entry(ParkingSpotType.LARGE, new ArrayList<VehicleType>(numLargeSpots))
        );
    }

    /*
     * Fluent api for initial allocation
     */
    public ParkingLot withMotorcycle() {
        System.out.println("Accommodating "+ VehicleType.MOTORCYCLE +": " + occupiedSpace.get(ParkingSpotType.COMPACT).size());
        if (occupiedSpace.get(ParkingSpotType.COMPACT).size() < maxCapacity.get(ParkingSpotType.COMPACT)) {
            occupiedSpace.get(ParkingSpotType.COMPACT).add(VehicleType.MOTORCYCLE);
        } else if (occupiedSpace.get(ParkingSpotType.REGULAR).size() < maxCapacity.get(ParkingSpotType.REGULAR)) {
            occupiedSpace.get(ParkingSpotType.REGULAR).add(VehicleType.MOTORCYCLE);
        } else if(occupiedSpace.get(ParkingSpotType.LARGE).size() < maxCapacity.get(ParkingSpotType.LARGE)) {
            occupiedSpace.get(ParkingSpotType.LARGE).add(VehicleType.MOTORCYCLE);
        } else {
            throw new ParkingLotFullException("FULL");
        }
        return this;
    }


    public ParkingLot withCar() {
        System.out.println("Accommodating "+ VehicleType.CAR +": " + occupiedSpace.get(ParkingSpotType.REGULAR).size());
        if (occupiedSpace.get(ParkingSpotType.REGULAR).size() < maxCapacity.get(ParkingSpotType.REGULAR)) {
            occupiedSpace.get(ParkingSpotType.REGULAR).add(VehicleType.CAR);
        } else if (canAccommodateCarInCompactSpots()) {
            occupiedSpace.get(ParkingSpotType.COMPACT).add(VehicleType.CAR);
        } else {
            throw new ParkingLotFullException("FULL");
        }
        return this;
    }

    private boolean canAccommodateCarInCompactSpots() {
        if (occupiedSpace.get(ParkingSpotType.COMPACT).size() < maxCapacity.get(ParkingSpotType.COMPACT)) {
            return true;
        }
        return false;
    }

    public ParkingLot withVan() {
        // when large spots are full, allow overflow into regular spot allocation
        System.out.println("Accommodating "+ VehicleType.VAN +": " + occupiedSpace.get(ParkingSpotType.LARGE).size());
        if (occupiedSpace.get(ParkingSpotType.LARGE).size() < maxCapacity.get(ParkingSpotType.LARGE)) {
            occupiedSpace.get(ParkingSpotType.LARGE).add(VehicleType.VAN);
        } else if (canAccommodateVanInRegularSpots()) {
            occupiedSpace.get(ParkingSpotType.REGULAR).add(VehicleType.VAN);
            occupiedSpace.get(ParkingSpotType.REGULAR).add(VehicleType.VAN);
            occupiedSpace.get(ParkingSpotType.REGULAR).add(VehicleType.VAN);
        } else {
            throw new ParkingLotFullException("FULL");
        }
        return this;
    }

    private boolean canAccommodateVanInRegularSpots() {
        if (occupiedSpace.get(ParkingSpotType.REGULAR).size() + 3 <= maxCapacity.get(ParkingSpotType.REGULAR)) {
            return true;
        }
        return false;
    }

    public int getCompactSpotsCapacity() {
        return maxCapacity.get(ParkingSpotType.COMPACT);
    }

    public int getRegularSpotsCapacity() {
        return maxCapacity.get(ParkingSpotType.REGULAR);
    }

    public int getLargeSpotsCapacity() {
        return maxCapacity.get(ParkingSpotType.LARGE);
    }

    public List<VehicleType> getCompactSpots() {
        return occupiedSpace.get(ParkingSpotType.COMPACT);
    }

    public List<VehicleType> getRegularSpots() {
        return occupiedSpace.get(ParkingSpotType.REGULAR);
    }

    public List<VehicleType> getLargeSpots() {
        return occupiedSpace.get(ParkingSpotType.LARGE);
    }

    /*
     * Business logic
     */
    public Map<ParkingSpotType, Integer> remainingCapacity() {
        return new CapacityAnalyser(this).remainingCapacity();
    }

    public Map<ParkingSpotType, Integer> totalCapacity() {
        return new CapacityAnalyser(this).totalCapacity();
    }

    public boolean isFull() {
        return new CapacityAnalyser(this).isFull();
    }

    public boolean isEmpty() {
        return new CapacityAnalyser(this).isEmpty();
    }

    public boolean isFullFor(VehicleType vehicleType) {
        return new CapacityAnalyser(this).isFullFor(vehicleType);
    }

    public int occupancyByVehicleType(VehicleType vehicleType) {
        // must represent vehicle types in parking lot - occupied spots is not rich enough
        return new CapacityAnalyser(this).occupancyByVehicleType(vehicleType);
    }

}
