package group.mesh.demo.domain.dao;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "phone_data")
@ToString(exclude = "user")
public class PhoneData {

    public static final String PHONE_REGEXP = "^([7]{1}[0-9]{10})([0-9]{0,2})$";

    @Id
    private Long id;

    @NotNull
    @Size(min = 11, max = 13)
    @Pattern(regexp = PHONE_REGEXP)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
