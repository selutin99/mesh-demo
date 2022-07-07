package group.mesh.demo.service;

import group.mesh.demo.domain.converter.PhoneDataToPhoneDataDto;
import group.mesh.demo.domain.dao.PhoneData;
import group.mesh.demo.domain.dto.response.PhoneDataResponseDto;
import group.mesh.demo.domain.repository.PhoneDataRepository;
import group.mesh.demo.exception.business.DataValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneDataService {

    private final PhoneDataToPhoneDataDto phoneDataDtoConverter;

    private final UserService userService;

    private final PhoneDataRepository phoneDataRepository;

    @Transactional
    @CacheEvict("phone_users")
    public PhoneDataResponseDto addUserPhoneData(String phone) {
        PhoneData existingPhone = phoneDataRepository.findByPhone(phone);
        if (existingPhone != null) {
            throw new DataValidationException("Phone already in use");
        }
        var phoneData = new PhoneData();
        var currentUser = userService.getUserFromSession();
        phoneData.setId(phoneDataRepository.generateNewId());
        phoneData.setPhone(phone);
        phoneData.setUser(currentUser);
        phoneDataRepository.save(phoneData);
        log.info("Successfully add new phone {} to user with id {}", phone, currentUser.getId());
        return phoneDataDtoConverter.convert(phoneDataRepository.findAllByUser(currentUser));
    }

    @Transactional
    @CacheEvict("phone_users")
    public PhoneDataResponseDto editPhoneData(String oldPhone, String newPhone) {
        var currentUser = userService.getUserFromSession();
        removePhoneData(oldPhone);
        addUserPhoneData(newPhone);
        log.info("Change phone {} to {} for user with id {}", oldPhone, newPhone, currentUser.getId());
        return phoneDataDtoConverter.convert(phoneDataRepository.findAllByUser(userService.getUserFromSession()));
    }

    @Transactional
    @CacheEvict("phone_users")
    public PhoneDataResponseDto removePhoneData(String phone) {
        var currentUser = userService.getUserFromSession();
        List<PhoneData> userPhoneData = phoneDataRepository.findAllByUser(currentUser);
        List<String> userPhones = userPhoneData.stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList());
        if (!userPhones.contains(phone)) {
            throw new DataValidationException("Phone not exist");
        }
        phoneDataRepository.delete(phoneDataRepository.findByPhone(phone));
        log.info("Delete phone {} for user with id {}", phone, currentUser.getId());
        return phoneDataDtoConverter.convert(phoneDataRepository.findAllByUser(currentUser));
    }
}
