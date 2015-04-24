package com.bcus.letspark.parking;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.bcus.letspark.traveller.Car;

import java.util.Observable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParkingLotTest {

    ParkingLotOwner parkingLotOwner;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    private final int parkingSize = 2;
    private String parkingLotId = "Some parking";

    @Test
    public void createParkingLot()
    {

        ParkingLot parkingLot = new ParkingLot(parkingLotId, parkingSize);
        assertNotNull(parkingLot);
    }

    @Test
    public void addCarToParkingLotShouldReturnParkingTicket() throws Exception {
        ParkingLot parkingLot = new ParkingLot(parkingLotId, parkingSize);
        Car car = new Car("some id");
        Ticket ticket = parkingLot.parkCar(car);
        assertNotNull(ticket);
    }




    @Test
    public void shouldNotParkSameCarTwice() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage(ParkingLot.SHOULD_NOT_PARK_SAME_CAR_TWICE);
        ParkingLot parkingLot = new ParkingLot(parkingLotId, parkingSize);
        Car carOne = new Car("some id");
        parkingLot.parkCar(carOne);
        Car sameCar = carOne;
        parkingLot.parkCar(sameCar);
    }

    @Test
    public void getMyCarCorrectly() throws Exception {

        Car carToBeParked = new Car("some vehicle id");
        ParkingLot parkingLot = new ParkingLot(parkingLotId, parkingSize);
        parkingLot.parkCar(carToBeParked);
        Car carReturnedFromParkingLot = parkingLot.getCarFromParking(carToBeParked.getVehicleIdentificationNumber());
        assertEquals(carToBeParked, carReturnedFromParkingLot);

    }

    @Test
    public void shouldNotBeAbleToGetMyCarTwice() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage(ParkingLot.CAR_NOT_PARKED_IN_PARKING_LOT);
        Car carToBeParked = new Car("some vehicle id");
        ParkingLot parkingLot = new ParkingLot(parkingLotId, parkingSize);
        parkingLot.parkCar(carToBeParked);
        Car carReturnedFromParkingLot = parkingLot.getCarFromParking(carToBeParked.getVehicleIdentificationNumber());
        parkingLot.getCarFromParking(carToBeParked.getVehicleIdentificationNumber());
    }

    @Test
    public void shouldNotReturnCarIfNotParked() throws Exception {
        expectedException.expect(Exception.class);
        ParkingLot parkingLot = new ParkingLot(parkingLotId, parkingSize);
        expectedException.expectMessage(ParkingLot.CAR_NOT_PARKED_IN_PARKING_LOT);
        parkingLot.getCarFromParking("SomeCarThatDoesNotExist");

    }

    @Test
    public void notAbleToParkWhenParkingLotIsFull() throws Exception {

        expectedException.expect(Exception.class);
        expectedException.expectMessage(ParkingLot.PARKING_LOT_IS_FULL);
        int parkingSize = 1;
        ParkingLot parkinglot = new ParkingLot(parkingLotId, parkingSize);
        Car firstCar = new Car("first car id");
        parkinglot.parkCar(firstCar);

        Car secondCar = new Car("second car id");
        parkinglot.parkCar(secondCar);
    }

    @Test
    public void shouldAbleToParkANewCarWhenCarIsRemovedFromFullParkingLot() throws Exception {

        int parkingSize = 1;
        ParkingLot parkinglot = new ParkingLot(parkingLotId, parkingSize);
        Car firstCar = new Car("first car id");
        parkinglot.parkCar(firstCar);
        parkinglot.getCarFromParking(firstCar.getVehicleIdentificationNumber());
        Car secondCar = new Car("second car id");
        Ticket ticket =  parkinglot.parkCar(secondCar);
        assertNotNull(ticket);
    }

    @Test
    public void notifyOwnerWhenParkingIsFull() throws Exception {
        int parkingLotSize = 2;
        ParkingLot parkingLot = new ParkingLot(parkingLotId, 2);
        parkingLotOwner = mock(ParkingLotOwner.class);
        parkingLot.addObserver(parkingLotOwner);
        Car carOne = new Car("One");
        Car carTwo = new Car("two");
        parkingLot.parkCar(carOne);
        parkingLot.parkCar(carTwo);
        verify(parkingLotOwner,times(1)).update(parkingLot, "PARKING_FULL");
    }

    @Test
    public void notifyOwnerWhenParkingIsEmpty() throws Exception {
        int parkingLotSize = 2;
        ParkingLot parkingLot = new ParkingLot(parkingLotId,parkingLotSize);
        parkingLotOwner = mock(ParkingLotOwner.class);
        parkingLot.addObserver(parkingLotOwner);

        Car carOne = new Car("One");
        Car carTwo = new Car("two");
        parkingLot.parkCar(carOne);
        parkingLot.parkCar(carTwo);

        parkingLot.getCarFromParking(carOne.getVehicleIdentificationNumber());
        parkingLot.getCarFromParking(carTwo.getVehicleIdentificationNumber());
        verify(parkingLotOwner,times(2)).update((Observable) any(), anyString());

    }

    @Test
    public void shouldNotNotifyOwnerWhenParkingIsEmpty() throws Exception {
        int parkingLotSize = 2;
        ParkingLot parkingLot = new ParkingLot(parkingLotId,parkingLotSize);
        parkingLotOwner = mock(ParkingLotOwner.class);
        parkingLot.addObserver(parkingLotOwner);

        Car carOne = new Car("One");
        parkingLot.parkCar(carOne);

        parkingLot.getCarFromParking(carOne.getVehicleIdentificationNumber());
        verify(parkingLotOwner,never()).update((Observable) any(), anyString());

    }

    @Test
    public void shouldBeAbleNotifyFBIAgentWhenParkingIs80PercentFullWhenAddingCar() throws Exception {
        int parkingLotSize = 5;
        ParkingLot parkingLot = new ParkingLot(parkingLotId , parkingLotSize);
        FBIAgent fbiAgent = mock(FBIAgent.class);
        parkingLot.addObserver((fbiAgent));

        Car carOne = new Car("One");
        Car carTwo = new Car("Two");
        Car carThree = new Car("Three");
        Car carFour = new Car("Four");
        parkingLot.parkCar(carOne);
        parkingLot.parkCar(carTwo);
        parkingLot.parkCar(carThree);
        parkingLot.parkCar(carFour);
        verify(fbiAgent,times(1)).update(parkingLot, "PARKING_EIGHTY_PERCENT_FULL");
    }

    @Test
    public void shouldNotNotifyParkingLotOwnerParkingIs80PercentFullWhenAddingCar() throws Exception {
        int parkingLotSize = 5;
        ParkingLot parkingLot = new ParkingLot(parkingLotId, parkingLotSize);
        ParkingLotOwner parkingLotOwner = mock(ParkingLotOwner.class);
        FBIAgent fbiAgent = mock(FBIAgent.class);
        parkingLot.addObserver((fbiAgent));
        parkingLot.addObserver(parkingLotOwner);

        Car carOne = new Car("One");
        Car carTwo = new Car("Two");
        Car carThree = new Car("Three");
        Car carFour = new Car("Four");
        parkingLot.parkCar(carOne);
        parkingLot.parkCar(carTwo);
        parkingLot.parkCar(carThree);
        parkingLot.parkCar(carFour);
        verify(parkingLotOwner,never()).update(any(ParkingLot.class), anyString());

    }

    @Test
    public void shouldBeAbleNotifyFBIAgentWhenParkingIs80PercentFullWhenRemovingCar() throws Exception {
        int parkingLotSize = 5;
        ParkingLot parkingLot = new ParkingLot(parkingLotId, parkingLotSize);
        FBIAgent fbiAgent = mock(FBIAgent.class);
        parkingLot.addObserver((fbiAgent));

        Car carOne = new Car("One");
        Car carTwo = new Car("Two");
        Car carThree = new Car("Three");
        Car carFour = new Car("Four");
        Car carFive = new Car("Five");
        parkingLot.parkCar(carOne);
        parkingLot.parkCar(carTwo);
        parkingLot.parkCar(carThree);
        parkingLot.parkCar(carFour);
        parkingLot.parkCar(carFive);
        parkingLot.getCarFromParking(carFive.getVehicleIdentificationNumber());
        verify(fbiAgent,times(2)).update(parkingLot, "PARKING_EIGHTY_PERCENT_FULL");
    }




}