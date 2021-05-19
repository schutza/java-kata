package ie.textability.kata.parking;

public class ParkingLotFullException extends RuntimeException {
    private String detail;

    public ParkingLotFullException(String message) {
        super(message);
    }

    public ParkingLotFullException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ParkingLotFullException{" +
            "message='" + super.getMessage() + '\'' +
            "detail='" + detail + '\'' +
            '}';
    }
}
