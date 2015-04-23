package com.bcus.letspark.parking;

import junit.framework.Assert;
import org.junit.Test;
import traveller.Car;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by sawai on 4/23/2015.
 */
public class TicketTest {
    @Test
    public void shouldBeAbleToCreateTicket(){
        Car car = new Car("vechile Number");
        Assert.assertNotNull(new Ticket("parkingLotId", car.getVehicleIdentificationNumber()));
    }

    @Test
    public void shouldBeAbleToRetrieveParkingLotIdAndVehicleIdentificationNumber(){
        Car car = new Car("vechile Number");
        String parkingLotId = "parkingLotId";
        Ticket ticket = new Ticket(parkingLotId ,car.getVehicleIdentificationNumber());
        assertEquals(car.getVehicleIdentificationNumber(), ticket.getVehicleIdentificationNumber());
        assertEquals(parkingLotId, ticket.getParkingLotId());

    }


}