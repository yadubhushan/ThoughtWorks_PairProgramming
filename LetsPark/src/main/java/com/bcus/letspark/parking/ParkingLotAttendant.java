package com.bcus.letspark.parking;

import com.bcus.letspark.exceptions.EmptyInputException;
import com.bcus.letspark.strategy.NormalParkingStrategy;
import com.bcus.letspark.strategy.ParkingStrategy;
import com.bcus.letspark.traveller.Car;
import com.bcus.letspark.traveller.CarSize;

import java.util.List;


public class ParkingLotAttendant {
    public static final String PARKING_LOT_IS_EMPTY = "Parking Lot Required";
    private static final String EMPTY_TICKET_PROVIDED = "No ticket is provided toun park the car.";
    private ParkingStrategy parkingStrategy;
    public ParkingLotAttendant(List<ParkingLot> parkingLots) throws Exception {

        if(parkingLots == null || parkingLots.isEmpty()){
            throw  new EmptyInputException(PARKING_LOT_IS_EMPTY);
        }
        this.parkingLots = parkingLots;
        this.parkingStrategy = new NormalParkingStrategy();
    }


    List<ParkingLot> parkingLots = null;

    public ParkingLot getMeFreeParkingLot(Car car) {

        return parkingStrategy.findParkingLot(parkingLots, car.getSize());
    }

    public Car unparkCar(Ticket parkingTicket) throws Exception {

        if(parkingTicket == null)
        {
            throw new EmptyInputException(EMPTY_TICKET_PROVIDED);
        }
        for(ParkingLot parkingLot : parkingLots)
        {
            if(parkingLot.getParkingLotId().equals(parkingTicket.getParkingLotId()))
            {
                return parkingLot.getCarFromParking(parkingTicket.getVehicleIdentificationNumber());
            }
        }
        return null;
    }
    public void setParkingStrategy(ParkingStrategy strategy) {
        this.parkingStrategy = strategy;
    }

    public Ticket parkCar(Car car) throws Exception {

        ParkingLot parkingLot = getMeFreeParkingLot(car);
        return parkingLot.parkCar(car);

    }
}