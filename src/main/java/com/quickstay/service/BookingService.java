package com.quickstay.service;


import com.quickstay.dto.BookingDto;
import com.quickstay.dto.HotelDto;
import com.quickstay.dto.PaymentDto;
import com.quickstay.dto.RoomDto;
import com.quickstay.model.*;
import com.quickstay.repository.BookingRepository;
import com.quickstay.repository.PaymentRepository;
import com.quickstay.repository.RoomRepository;
import com.quickstay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<RoomDto> getAvailableRooms() {
        List<Room> rooms = roomRepository.findByIsAvailable(true);
        List<RoomDto> roomDtos = new ArrayList<>();
        for (Room room : rooms) {
            RoomDto roomDto = new RoomDto();
            roomDto.setId(room.getId());
            roomDto.setRoomType(room.getRoomType());
            roomDto.setPrice(room.getPrice());
            roomDto.setAvailable(room.isAvailable());
            HotelDto hotelDto = new HotelDto();
            hotelDto.setId(room.getHotel().getId());
            hotelDto.setName(room.getHotel().getName());
            hotelDto.setLocation(room.getHotel().getLocation());
            hotelDto.setContactNumber(room.getHotel().getContactNumber());
            hotelDto.setAddress(room.getHotel().getAddress());
            roomDto.setHotel(hotelDto);
            roomDtos.add(roomDto);
        }
        return roomDtos;
    }

    public List<RoomDto> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        List<Room> rooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate);
        List<RoomDto> roomDtos = new ArrayList<>();
        for (Room room : rooms) {
            RoomDto roomDto = new RoomDto();
            roomDto.setId(room.getId());
            roomDto.setRoomType(room.getRoomType());
            roomDto.setPrice(room.getPrice());
            roomDto.setAvailable(room.isAvailable());
            HotelDto hotelDto = new HotelDto();
            hotelDto.setId(room.getHotel().getId());
            hotelDto.setName(room.getHotel().getName());
            hotelDto.setLocation(room.getHotel().getLocation());
            hotelDto.setContactNumber(room.getHotel().getContactNumber());
            hotelDto.setAddress(room.getHotel().getAddress());
            roomDto.setHotel(hotelDto);
            roomDtos.add(roomDto);
        }
        return roomDtos;
    }

    public BookingDto createBooking(BookingDto bookingDto) {
        Room room = roomRepository.findById(bookingDto.getRoomId()).orElseThrow(() -> new IllegalArgumentException("Room not found"));
        User user = userRepository.findById(bookingDto.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (room.isAvailable()) {
            Booking booking = new Booking();
            booking.setRoom(room);
            booking.setUser(user);
            booking.setCheckInDate(LocalDate.parse(bookingDto.getCheckInDate()));
            booking.setCheckOutDate(LocalDate.parse(bookingDto.getCheckOutDate()));
            booking.setBookingStatus(BookingStatus.PENDING);
            room.setAvailable(false);
            roomRepository.save(room);
            Booking saved = bookingRepository.save(booking);
            bookingDto.setBookingId(saved.getId());
            bookingDto.setPrice(room.getPrice());

            // Calculate the payment amount (example: room price * number of nights)
            double paymentAmount = room.getPrice() *
                    (booking.getCheckOutDate().getDayOfYear() - booking.getCheckInDate().getDayOfYear());

            // Prepare PaymentDto to create payment
            PaymentDto payment = new PaymentDto();
            payment.setBookingId(booking.getId());
            payment.setAmount(paymentAmount);
            payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
            payment.setTransactionId("TX-" + System.currentTimeMillis());
            payment.setPaymentStatus(PaymentStatus.PENDING);  // Default status as PENDING

            // Create the payment after booking
            PaymentDto savedPayment = paymentService.createPayment(payment);

            // Add payment to booking (optional, if you want to associate the payment to the booking)
            bookingDto.setPaymentDto(savedPayment);
            bookingDto.setPaymentDto(payment);

            return bookingDto;
        } else {
            throw new IllegalStateException("Room is not available.");
        }
    }

    public List<BookingDto> getBookingsForUser(String username) {
        List<Booking> bookings = bookingRepository.findByUserUsername(username);
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setBookingId(booking.getId());
            bookingDto.setCheckInDate(booking.getCheckInDate().toString());
            bookingDto.setCheckOutDate(booking.getCheckOutDate().toString());
            bookingDto.setRoomId(booking.getRoom().getId());
            bookingDto.setUsername(booking.getUser().getUsername());
            bookingDto.setPrice(booking.getRoom().getPrice());
            bookingDto.setBookingStatus(booking.getBookingStatus());
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setPaymentId(booking.getPayment().getId());
            paymentDto.setBookingId(booking.getId());
            paymentDto.setAmount(booking.getPayment().getAmount());
            paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
            paymentDto.setTransactionId(booking.getPayment().getTransactionId());
            paymentDto.setPaymentStatus(booking.getPayment().getPaymentStatus());
            bookingDto.setPaymentDto(paymentDto);
            bookingDtos.add(bookingDto);
        }
        return bookingDtos;
    }
}
