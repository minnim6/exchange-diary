package com.exchange.diary.domain.diary;

import com.exchange.diary.domain.image.BackgroundImage;
import com.exchange.diary.domain.image.Image;
import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.sticker.Sticker;
import com.exchange.diary.domain.team.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @JoinColumn(name = "team_id")
    @ManyToOne
    private Team team;

    @JoinColumn(name = "member_number")
    @ManyToOne
    private Member member;

    @OneToOne
    private BackgroundImage backgroundImage;

    @OneToOne
    private Image image;

    private String diaryContent;

    @Column(insertable = true, updatable = false)
    @CreationTimestamp
    private LocalDate diaryDate;

    @OneToMany(mappedBy = "diary")
    private List<Sticker> stickerList = new ArrayList<>();
}
