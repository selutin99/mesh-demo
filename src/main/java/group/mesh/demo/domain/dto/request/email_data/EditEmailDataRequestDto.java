package group.mesh.demo.domain.dto.request.email_data;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EditEmailDataRequestDto {

    @Email
    @NotBlank
    private String oldEmail;

    @Email
    @NotBlank
    private String newEmail;
}
