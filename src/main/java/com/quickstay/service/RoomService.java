package com.quickstay.service;

import com.quickstay.dto.HotelDto;
import com.quickstay.dto.RoomDto;
import com.quickstay.model.Hotel;
import com.quickstay.model.Room;
import com.quickstay.repository.HotelRepository;
import com.quickstay.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    public RoomDto addRoomToHotel(Long hotelId, RoomDto roomDto) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

        Room room = new Room();
        room.setRoomType(roomDto.getRoomType());
        room.setPrice(roomDto.getPrice());
        room.setHotel(hotel);
        room.setAvailable(true);
        Room saved = roomRepository.save(room);
        roomDto.setId(saved.getId());
        roomDto.setAvailable(saved.isAvailable());
        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(hotel.getId());
        roomDto.setHotel(hotelDto);
        return roomDto;
    }
}
