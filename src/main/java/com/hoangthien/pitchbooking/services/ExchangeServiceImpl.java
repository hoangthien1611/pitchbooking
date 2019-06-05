package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.dto.ExchangeDTO;
import com.hoangthien.pitchbooking.entities.*;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.mapper.ExchangeMapper;
import com.hoangthien.pitchbooking.repositories.*;
import com.hoangthien.pitchbooking.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private ExchangeMapper exchangeMapper;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean save(ExchangeDTO exchangeDTO) {
        LocalDate dateExchange = TimeUtils.getLocalDateFromDateString(exchangeDTO.getDate());
        LocalDateTime now = LocalDateTime.now();

        if (now.toLocalDate().isAfter(dateExchange) ||
                (now.toLocalDate().isEqual(dateExchange) && now.getHour() >= TimeUtils.getTimeIntFromString(exchangeDTO.getTime()))) {
            throw new PitchBookingException("Không được tạo trận đấu cho thời gian đã diễn ra");
        }

        LocalDateTime timeExchange = LocalDateTime.of(dateExchange, TimeUtils.getLocalTimeFromTimeString(exchangeDTO.getTime()));

        Team team = teamRepository.findById(exchangeDTO.getTeamId())
                .orElseThrow(() -> new PitchBookingException("Đội bóng không hợp lệ"));

        District district = districtRepository.findById(exchangeDTO.getDistrictId())
                .orElseThrow(() -> new PitchBookingException("Quận / huyện không hợp lệ"));

        Pitch pitch = null;
        if (exchangeDTO.getHasPitch() == 1) {
            pitch = pitchRepository.findById(exchangeDTO.getPitchId())
                    .orElseThrow(() -> new PitchBookingException("Sân không hợp lệ"));
        }

        Level level = levelRepository.findById(exchangeDTO.getLevelId())
                .orElseThrow(() -> new PitchBookingException("Trình độ không hợp lệ"));

        User user = userRepository.findByUserName(exchangeDTO.getUsername())
                .orElseThrow(() -> new PitchBookingException("User not found"));

        Exchange exchange = exchangeMapper.exchangeDTOToExchange(exchangeDTO);
        exchange.setTimeExchange(timeExchange);
        exchange.setDistrict(district);
        exchange.setLevel(level);
        exchange.setPitch(pitch);
        exchange.setTeam(team);
        exchange.setUserCreated(user);
        exchange.setStatus(0);

        exchangeRepository.save(exchange);
        return true;
    }

    @Override
    public Page<Exchange> getAllPageable(String path, List<Integer> hasPitch, List<Long> levelIds, String search, int page) {
        LocalDateTime now = LocalDateTime.now();

        if (Defines.DISTRICT_PATH_ALL.equalsIgnoreCase(path)) {
            return exchangeRepository.findAllByHasPitchInAndLevelIdInAndSearchAndTimeExchangeAfter(hasPitch, levelIds, search.toLowerCase(), now, PageRequest.of(page, Defines.NUMBER_OF_ROWS_PER_PAGE));
        }

        return exchangeRepository.findAllByDistrictPathAndHasPitchInAndLevelIdInAndSearchAndTimeExchangeAfter(path, hasPitch, levelIds, search.toLowerCase(), now, PageRequest.of(page, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Exchange> getAllPageable(String path, List<Integer> hasPitch, List<Long> levelIds, int page) {
        LocalDateTime now = LocalDateTime.now();

        if (Defines.DISTRICT_PATH_ALL.equalsIgnoreCase(path)) {
            return exchangeRepository.findAllByHasPitchInAndLevelIdInAndTimeExchangeAfter(hasPitch, levelIds, now, PageRequest.of(page, Defines.NUMBER_OF_ROWS_PER_PAGE));
        }

        return exchangeRepository.findAllByDistrictPathAndHasPitchInAndLevelIdInAndTimeExchangeAfter(path, hasPitch, levelIds, now, PageRequest.of(page, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public List<Exchange> getAllByUserAndAvailable(String userName) {
        LocalDateTime now = LocalDateTime.now();
        return exchangeRepository.findAllByUserCreatedUserNameAndTimeExchangeAfterAndHasPitchEqualsAndStatusEquals(userName, now, 1, 0);
    }

    @Override
    public boolean delete(Long exchangeId) {
        exchangeRepository.findById(exchangeId)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy exchange cần xóa"));

        exchangeRepository.deleteById(exchangeId);
        return true;
    }
}
