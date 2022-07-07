package group.mesh.demo.service;

import group.mesh.demo.domain.converter.EmailDataToEmailDataDto;
import group.mesh.demo.domain.dao.EmailData;
import group.mesh.demo.domain.dto.response.EmailDataResponseDto;
import group.mesh.demo.domain.repository.EmailDataRepository;
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
public class EmailDataService {

    private final EmailDataToEmailDataDto emailDataDtoConverter;

    private final UserService userService;

    private final EmailDataRepository emailDataRepository;

    @Transactional
    @CacheEvict("email_users")
    public EmailDataResponseDto addUserEmailData(String email) {
        EmailData existingEmail = emailDataRepository.findByEmail(email);
        if (existingEmail != null) {
            throw new DataValidationException("Email already exist");
        }
        var emailData = new EmailData();
        var currentUser = userService.getUserFromSession();
        emailData.setId(emailDataRepository.generateNewId());
        emailData.setEmail(email);
        emailData.setUser(currentUser);
        emailDataRepository.save(emailData);
        log.info("Successfully add new email {} to user with id {}", email, currentUser.getId());
        return emailDataDtoConverter.convert(emailDataRepository.findAllByUser(currentUser));
    }

    @Transactional
    @CacheEvict("email_users")
    public EmailDataResponseDto editEmailData(String oldEmail, String newEmail) {
        var currentUser = userService.getUserFromSession();
        removeEmailData(oldEmail);
        addUserEmailData(newEmail);
        log.info("Change email {} to {} for user with id {}", oldEmail, newEmail, currentUser.getId());
        return emailDataDtoConverter.convert(emailDataRepository.findAllByUser(currentUser));
    }

    @Transactional
    @CacheEvict("email_users")
    public EmailDataResponseDto removeEmailData(String email) {
        var currentUser = userService.getUserFromSession();
        List<EmailData> userEmailData = emailDataRepository.findAllByUser(currentUser);
        List<String> userEmails = userEmailData.stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList());
        if (!userEmails.contains(email)) {
            throw new DataValidationException("Email not exist");
        }
        emailDataRepository.delete(emailDataRepository.findByEmail(email));
        log.info("Delete email {} for user with id {}", email, currentUser.getId());
        return emailDataDtoConverter.convert(emailDataRepository.findAllByUser(currentUser));
    }
}
