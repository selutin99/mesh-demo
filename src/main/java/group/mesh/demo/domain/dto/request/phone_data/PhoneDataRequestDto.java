package group.mesh.demo.domain.dto.request.phone_data;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static group.mesh.demo.domain.dao.PhoneData.PHONE_REGEXP;

@Data
public class PhoneDataRequestDto {

    @NotBlank
    @Size(min = 11, max = 13)
    @Pattern(regexp = PHONE_REGEXP)
    private String phone;
}
