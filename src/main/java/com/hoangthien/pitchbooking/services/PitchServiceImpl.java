package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.dto.ChildPitchDTO;
import com.hoangthien.pitchbooking.dto.PitchDTO;
import com.hoangthien.pitchbooking.dto.TimeFrame;
import com.hoangthien.pitchbooking.dto.TimeFrameBooking;
import com.hoangthien.pitchbooking.entities.*;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.mapper.PitchMapper;
import com.hoangthien.pitchbooking.repositories.*;
import com.hoangthien.pitchbooking.utils.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PitchServiceImpl implements PitchService {

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private YardSurfaceRepository yardSurfaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildPitchRepository childPitchRepository;

    @Autowired
    private PitchMapper pitchMapper;

    @Override
    public Pitch saveNewPitch(PitchDTO pitchDTO) {
        YardSurface yardSurface = yardSurfaceRepository
                .findById(pitchDTO.getYardSurfaceId())
                .orElseThrow(() -> new PitchBookingException("Loại Mặt sân không tìm thấy!"));

        District district = districtRepository
                .findById(pitchDTO.getDistrictId())
                .orElseThrow(() -> new PitchBookingException("Quận / huyện không tìm thấy!"));

        // Update later
        User owner = userRepository
                .findById(1L)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy chủ sân!"));

        Pitch pitch = pitchMapper.pitchDTOToPitch(pitchDTO);
        pitch.setDistrict(district);
        pitch.setYardSurface(yardSurface);
        pitch.setOwner(owner);

        return pitchRepository.save(pitch);
    }

    @Transactional
    @Override
    public List<Pitch> getPitchesByOwnerId(long ownerId) {
        return pitchRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public Pitch getPitchById(long id) {
        return pitchRepository.findById(id)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy sân!"));
    }

    @Override
    public Pitch updatePitch(PitchDTO pitchDTO) {
        Pitch pitch = pitchRepository
                .findById(pitchDTO.getId())
                .orElseThrow(() -> new PitchBookingException("Pitch không tìm thấy!"));

        YardSurface yardSurface = yardSurfaceRepository
                .findById(pitchDTO.getYardSurfaceId())
                .orElseThrow(() -> new PitchBookingException("Loại Mặt sân mới không tìm thấy!"));

        District district = districtRepository
                .findById(pitchDTO.getDistrictId())
                .orElseThrow(() -> new PitchBookingException("Quận / huyện không tìm thấy!"));

        // Update later
        User owner = userRepository
                .findById(1L)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy chủ sân!"));

        pitch.setName(pitchDTO.getName());
        pitch.setIntroduction(pitchDTO.getIntroduction());
        pitch.setDetailDescription(pitchDTO.getDetailDescription());
        pitch.setYardSurface(yardSurface);
        pitch.setDistrict(district);
        pitch.setAddress(pitchDTO.getAddress());
        pitch.setLatitude(pitchDTO.getLatitude());
        pitch.setLongitude(pitchDTO.getLongitude());
        pitch.setPhoneNumber(pitchDTO.getPhoneNumber());
        pitch.setEmail(pitchDTO.getEmail());
        pitch.setFacebook(pitchDTO.getFacebook());
        if (pitchDTO.getAvatar() != null) {
            pitch.setAvatar(pitchDTO.getAvatar());
        }

        return pitchRepository.save(pitch);
    }

    @Override
    public List<TimeFrameBooking> getTimeFrameBookingsByDate(Long pitchId, LocalDate dateBooking) {
        List<ChildPitch> childPitches = childPitchRepository.findAllByPitchId(pitchId);
        List<TimeFrame> timeFrames = TimeUtils.generateTimeFrameList(Defines.TIME_START, Defines.TIME_END);
        List<TimeFrameBooking> timeFrameBookings = timeFrames.stream()
                .map(timeFrame -> {
                    TimeFrameBooking timeFrameBooking = new TimeFrameBooking();
                    timeFrameBooking.setTimeFrame(timeFrame);

                    List<ChildPitchDTO> childPitchDTOS = childPitches.stream()
                            .map(childPitch -> {
                                ChildPitchDTO childPitchDTO = new ChildPitchDTO();
                                childPitchDTO.setId(childPitch.getId());
                                childPitchDTO.setName(childPitch.getName());

                                Optional<SpecificPitchesCost> optional = childPitch.getGroupSpecificPitches()
                                        .getSpecificPitchesCosts()
                                        .stream()
                                        .filter(spCost ->
                                                isTimeFrameInTimeRange(timeFrame, spCost.getFromTime(), spCost.getToTime())
                                                && getGroupDaysIdFromLocalDate(dateBooking) == spCost.getGroupDays().getId())
                                        .findFirst();
                                if (optional.isPresent()) {
                                    childPitchDTO.setCost(optional.get().getCost());
                                }

                                Optional<Booking> optionalBooking = childPitch.getBookings()
                                        .stream()
                                        .filter(bk ->
                                                bk.getFromTime().equals(timeFrame.getFromTime())
                                                        && bk.getToTime().equals(timeFrame.getToTime())
                                                        && bk.isAccepted()
                                                        && bk.getDateBooking().isEqual(dateBooking))
                                        .findFirst();

                                if (optionalBooking.isPresent()) {
                                    childPitchDTO.setBooking(optionalBooking.get());
                                }

                                return childPitchDTO;
                            })
                            .collect(Collectors.toList());

                    timeFrameBooking.setChildPitches(childPitchDTOS);
                    return timeFrameBooking;
                })
                .collect(Collectors.toList());
        return timeFrameBookings;
    }

    @Override
    public Page<Pitch> getAllPageable(int offset) {
        return pitchRepository.findAll(PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Transactional
    @Override
    public Page<Pitch> getAllByDistrictPathPageable(String path, int offset) {
        return pitchRepository.findAllByDistrictPath(path, PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    private boolean isTimeFrameInTimeRange(TimeFrame timeFrame, String timeStart, String timeEnd) {
        try {
            int timeStartInt = Integer.parseInt(timeStart.split(":")[0]);
            int timeEndInt = Integer.parseInt(timeEnd.split(":")[0]);
            int fromTimeInt = Integer.parseInt(timeFrame.getFromTime().split(":")[0]);
            int toTimeInt = Integer.parseInt(timeFrame.getToTime().split(":")[0]);

            return fromTimeInt >= timeStartInt && toTimeInt <= timeEndInt;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getGroupDaysIdFromLocalDate(LocalDate localDate) {
        String dayOfWeek = localDate.getDayOfWeek().name();
        int result = 0;

        switch (dayOfWeek) {
            case "MONDAY":
            case "TUESDAY":
            case "WEDNESDAY":
            case "THURSDAY":
            case "FRIDAY":
                result = 1;
                break;
            case "SATURDAY":
                result = 2;
                break;
            case "SUNDAY":
                result = 3;
                break;
        }
        return result;
    }
}
