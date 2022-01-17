package domain.member;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Member {

    @Id
    private Long memberNumber;
    private String memberId;
    private String memberPassword;
    private String memberNickname;
    private Date memberJoinDate;
}
