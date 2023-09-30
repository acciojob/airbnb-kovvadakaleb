package com.driver.repositores;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class HotelManagementRepository {

    HashMap<String,Hotel> hoteldb;

    HashMap<Integer,User> userdb;

    HashMap<String,Booking> bookingdb;

    HotelManagementRepository(){
        hoteldb = new HashMap<>();
        userdb = new HashMap<>();
        bookingdb = new HashMap<>();
    }
    public String addHotel(Hotel hotel) {
        if(hotel.getHotelName()==null || hotel==null){
            return "Empty Failure";
        }
        if(hoteldb.containsKey(hotel.getHotelName())){
            return "FAILURE";
        }
        hoteldb.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }


    public Integer addUser(User user) {
        userdb.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }


    public String getHotelWithMostFacilities() {
        int maxFacility = 0;
        String goodHotel = "";
        for(Hotel hotel : hoteldb.values()){
             int no_of_facility = 0;
            for(Facility facility : hotel.getFacilities()){
                no_of_facility = no_of_facility+1;
            }
            if(no_of_facility>maxFacility){
                maxFacility = no_of_facility;
                goodHotel = hotel.getHotelName();
            }
            else if(no_of_facility!=0 && no_of_facility==maxFacility){
                String s1 = goodHotel;
                String s2 = hotel.getHotelName();
                if(s1.compareTo(s2)<0) goodHotel=s1;
                else if(s1.compareTo(s2)>0) goodHotel=s2;

            }
        }
           return goodHotel;
    }

    public int bookARoom(Booking booking) {
       int totalAmount = 0;
       String hotel = booking.getHotelName();
       if(hoteldb.containsKey(hotel)) {
           Hotel hotelDetails = hoteldb.get(hotel);
           int available = hotelDetails.getAvailableRooms();
           if (available < booking.getNoOfRooms()) return -1;
           UUID uuid = UUID.randomUUID();
           String bookingID = uuid.toString();
           bookingdb.put(bookingID, booking);

           totalAmount = booking.getNoOfRooms() * hotelDetails.getPricePerNight();
           hotelDetails.setAvailableRooms(available - booking.getNoOfRooms());
       }
        return totalAmount;
    }

    public int getBookings(Integer aadharCard) {
        int count=0;
        for(Booking booking : bookingdb.values()){
            if(booking.getBookingAadharCard()==aadharCard){
                count++;
            }
        }
        return count;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {


        if(hoteldb.containsKey(hotelName)) {
            Hotel hotel = hoteldb.get(hotelName);
             List<Facility> updateFacility = new ArrayList<>(hotel.getFacilities());
                 for(Facility newFacility : newFacilities){
                     boolean found = false;
                     for(Facility facility : hotel.getFacilities()){
                         if(facility.equals(newFacility)){
                             found=true;
                         }
                     }
                     if(found==false){
                         updateFacility.add(newFacility);
                     }
             }

            hotel.setFacilities(updateFacility);
            hoteldb.put(hotelName, hotel);
            return hotel;
        }
        return new Hotel();
    }
}
