package group.mesh.demo.domain.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class EmailDataResponseDto {

    private Long userId;
    private List<String> emails;
}
