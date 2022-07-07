package group.mesh.demo.domain.converter;

import group.mesh.demo.domain.dao.EmailData;
import group.mesh.demo.domain.dto.response.EmailDataResponseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmailDataToEmailDataDto implements Converter<List<EmailData>, EmailDataResponseDto> {

    @Override
    public EmailDataResponseDto convert(List<EmailData> emailData) {
        EmailDataResponseDto dto = new EmailDataResponseDto();
        dto.setEmails(emailData.stream().map(EmailData::getEmail).collect(Collectors.toList()));
        dto.setUserId(emailData.stream().findFirst().get().getUser().getId());
        return dto;
    }
}
