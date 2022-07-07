package group.mesh.demo.domain.dto.request;

import lombok.Data;

@Data
public class UserCredentialsDto {

    private String login;
    private String password;
}
