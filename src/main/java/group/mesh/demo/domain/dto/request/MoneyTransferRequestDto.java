package group.mesh.demo.domain.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class MoneyTransferRequestDto {

    @NotNull
    @NotBlank
    private Long to;

    @NotNull
    @NotBlank
    @Positive
    private BigDecimal value;
}
