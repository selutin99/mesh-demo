package group.mesh.demo.domain.converter;

import group.mesh.demo.domain.dao.PhoneData;
import group.mesh.demo.domain.dto.response.PhoneDataResponseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhoneDataToPhoneDataDto implements Converter<List<PhoneData>, PhoneDataResponseDto> {

    @Override
    public PhoneDataResponseDto convert(List<PhoneData> phoneData) {
        PhoneDataResponseDto dto = new PhoneDataResponseDto();
        dto.setPhones(phoneData.stream().map(PhoneData::getPhone).collect(Collectors.toList()));
        dto.setUserId(phoneData.stream().findFirst().get().getUser().getId());
        return dto;
    }
}
