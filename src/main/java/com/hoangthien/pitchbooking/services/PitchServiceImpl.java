package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.PitchDTO;
import com.hoangthien.pitchbooking.entities.District;
import com.hoangthien.pitchbooking.entities.Pitch;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.entities.YardSurface;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.mapper.PitchMapper;
import com.hoangthien.pitchbooking.repositories.DistrictRepository;
import com.hoangthien.pitchbooking.repositories.PitchRepository;
import com.hoangthien.pitchbooking.repositories.UserRepository;
import com.hoangthien.pitchbooking.repositories.YardSurfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        Pitch pitch = pitchRepository
                .findById(pitchDTO.getId())
                .orElseThrow(() -> new PitchBookingException("Pitch không tìm thấy!"));

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
}
