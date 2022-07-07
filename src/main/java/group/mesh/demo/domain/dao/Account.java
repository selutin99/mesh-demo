package group.mesh.demo.domain.dao;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "account")
@ToString(exclude = "user")
public class Account {

    @Id
    private Long id;

    @Positive
    private BigDecimal balance;

    @Positive
    private BigDecimal initialBalance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
