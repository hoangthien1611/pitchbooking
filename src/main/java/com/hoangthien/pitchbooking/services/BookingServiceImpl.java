package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.dto.BookingCheck;
import com.hoangthien.pitchbooking.dto.BookingDTO;
import com.hoangthien.pitchbooking.dto.TimeFrame;
import com.hoangthien.pitchbooking.entities.Booking;
import com.hoangthien.pitchbooking.entities.ChildPitch;
import com.hoangthien.pitchbooking.entities.SpecificPitchesCost;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.mapper.BookingMapper;
import com.hoangthien.pitchbooking.repositories.BookingRepository;
import com.hoangthien.pitchbooking.repositories.ChildPitchRepository;
import com.hoangthien.pitchbooking.repositories.SpecificPitchesCostRepository;
import com.hoangthien.pitchbooking.repositories.UserRepository;
import com.hoangthien.pitchbooking.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildPitchRepository childPitchRepository;

    @Autowired
    private SpecificPitchesCostRepository specificPitchesCostRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Override
    public BookingDTO save(BookingDTO bookingDTO) {
        User userBooking = userRepository
                .findById(bookingDTO.getUserId())
                .orElseThrow(() -> new PitchBookingException("User not found!"));

        ChildPitch childPitch = childPitchRepository
                .findById(bookingDTO.getChildPitchId())
                .orElseThrow(() -> new PitchBookingException("Child pitch not found!"));

        if (bookingDTO.getId() == 0) {
            bookingDTO.setId(null);
            bookingDTO.setTimeCreated(LocalDateTime.now());

            Booking booking = bookingMapper.bookingDTOToBooking(bookingDTO);
            booking.setDateBooking(TimeUtils.getLocalDateFromDateString(bookingDTO.getDateBookingString()));
            booking.setAccepted(true);
            booking.setUserBooking(userBooking);
            booking.setChildPitch(childPitch);

            return bookingMapper.bookingToBookingDTO(bookingRepository.save(booking));
        }

        Booking existedBooking = bookingRepository
                .findById(bookingDTO.getId())
                .orElseThrow(() -> new PitchBookingException("Booking not found!"));

        existedBooking.setOrderName(bookingDTO.getOrderName());
        existedBooking.setOrderPhone(bookingDTO.getOrderPhone());
        existedBooking.setContent(bookingDTO.getContent());
        existedBooking.setCost(bookingDTO.getCost());
        existedBooking.setAccepted(true);
        existedBooking.setUserBooking(userBooking);
        existedBooking.setChildPitch(childPitch);
        existedBooking.setDateBooking(TimeUtils.getLocalDateFromDateString(bookingDTO.getDateBookingString()));

        return bookingMapper.bookingToBookingDTO(bookingRepository.save(existedBooking));
    }

    @Override
    public void delete(Long id) {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new PitchBookingException("Booking not found!"));
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingCheck> getBookingCheckList(Long pitchesCostId, LocalDate date) {

        List<BookingCheck> bookingChecks = new ArrayList<>();

        SpecificPitchesCost specificPitchesCost = specificPitchesCostRepository
                .findById(pitchesCostId)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy PitchesCost"));

        List<Booking> bookings = bookingRepository.findAllByPitchesCostIdAndDateBooking(pitchesCostId, date);

        int fromTime = TimeUtils.getTimeIntFromString(specificPitchesCost.getFromTime());
        int toTime = TimeUtils.getTimeIntFromString(specificPitchesCost.getToTime());
        List<TimeFrame> timeFrames = TimeUtils.generateTimeFrameList(fromTime, toTime);

        return timeFrames.stream()
                .map(timeFrame -> {
                    BookingCheck bookingCheck = new BookingCheck();
                    bookingCheck.setTimeFrame(timeFrame);
                    bookingCheck.setDateBooking(date);

                    List<Booking> bookedList = bookings.stream()
                            .filter(booking -> booking.getFromTime().equals(timeFrame.getFromTime())
                                    && booking.getToTime().equals(timeFrame.getToTime()))
                            .collect(Collectors.toList());
                    bookingCheck.setAvailable(bookedList.size() < specificPitchesCost.getGroupSpecificPitches().getChildPitches().size());
                    return bookingCheck;
                })
                .collect(Collectors.toList());
    }
}
