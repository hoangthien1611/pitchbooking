package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.dto.ChildPitchDTO;
import com.hoangthien.pitchbooking.dto.PitchDTO;
import com.hoangthien.pitchbooking.dto.TimeFrame;
import com.hoangthien.pitchbooking.dto.TimeFrameBooking;
import com.hoangthien.pitchbooking.entities.*;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.mapper.PitchMapper;
import com.hoangthien.pitchbooking.repositories.*;
import com.hoangthien.pitchbooking.utils.PitchBookingUtils;
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

        User owner = userRepository
                .findByUserName(pitchDTO.getOwnerUserName())
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy chủ sân!"));

        Pitch pitch = pitchMapper.pitchDTOToPitch(pitchDTO);
        pitch.setDistrict(district);
        pitch.setYardSurface(yardSurface);
        pitch.setOwner(owner);

        return pitchRepository.save(pitch);
    }

    @Transactional
    @Override
    public List<Pitch> getPitchesByOwner(String userName) {
        return pitchRepository.findAllByOwnerUserName(userName);
    }

    @Override
    public Pitch getPitchById(long id) {
        return pitchRepository.findById(id)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy sân!"));
    }

    @Transactional
    @Override
    public List<PitchDTO> getAllByDistrict(Long districtId) {
        return pitchRepository.findAllByDistrictId(districtId)
                .stream()
                .map(pitchMapper::pitchToPitchDTO)
                .collect(Collectors.toList());
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
    public boolean deletePitch(Long pitchId) {
        pitchRepository.findById(pitchId)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy sân cần xóa"));

        pitchRepository.deleteById(pitchId);
        return true;
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
                                                && PitchBookingUtils.getGroupDaysIdFromLocalDate(dateBooking) == spCost.getGroupDays().getId())
                                        .findFirst();
                                if (optional.isPresent()) {
                                    childPitchDTO.setCost(optional.get().getCost());
                                }

                                Optional<Booking> optionalBooking = childPitch.getBookings()
                                        .stream()
                                        .filter(bk ->
                                                bk.getFromTime().equals(timeFrame.getFromTime())
                                                        && bk.getToTime().equals(timeFrame.getToTime())
                                                        && bk.getDateBooking().isEqual(dateBooking))
                                        .findFirst();

                                if (optionalBooking.isPresent()) {
                                    childPitchDTO.setBooking(optionalBooking.get());
                                    childPitchDTO.setBookingAccepted(optionalBooking.get().isAccepted());
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
    public Page<Pitch> getAllPageable(String path, List<Integer> costs, List<Long> typeIds, List<Long> surfaceIds, int offset) {
        return pitchRepository.findAllByDistrictPathAndPitchTypeIdInAndSurfaceIdInAndCostIn(path, typeIds, surfaceIds, costs, PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Pitch> getAllPageable(String path, List<Integer> costs, List<Long> typeIds, List<Long> surfaceIds, String search, int offset) {
        return pitchRepository.findAllByDistrictPathAndPitchTypeIdInAndSurfaceIdInAndCostInAndSearch(path, typeIds, surfaceIds, costs, search.toLowerCase(), PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Pitch> getAllPageable(String path, List<Long> typeIds, List<Long> surfaceIds, int offset) {
        return pitchRepository.findAllByDistrictPathAndPitchTypeIdInAndSurfaceIdIn(path, typeIds, surfaceIds, PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Pitch> getAllPageable(String path, List<Long> typeIds, List<Long> surfaceIds, String search, int offset) {
        return pitchRepository.findAllByDistrictPathAndPitchTypeIdInAndSurfaceIdInAndSearch(path, typeIds, surfaceIds, search.toLowerCase(), PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Pitch> getAllPageable(List<Integer> costs, List<Long> typeIds, List<Long> surfaceIds, int offset) {
        return pitchRepository.findAllByPitchTypeIdInAndSurfaceIdInAndCostIn(typeIds, surfaceIds, costs, PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Pitch> getAllPageable(List<Integer> costs, List<Long> typeIds, List<Long> surfaceIds, String search, int offset) {
        return pitchRepository.findAllByPitchTypeIdInAndSurfaceIdInAndCostInAndSearch(typeIds, surfaceIds, costs, search.toLowerCase(), PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public List<Pitch> get3PitchesSameDistrict(Long pitchId, Long districtId) {
        Page<Pitch> pitches = pitchRepository.findAllByDistrictIdAndIdNot(districtId, pitchId, PageRequest.of(0, 3));
        return pitches.getContent();
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
}
