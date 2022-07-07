package group.mesh.demo.domain.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PhoneDataResponseDto {

    private Long userId;
    private List<String> phones;
}
