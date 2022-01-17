package com.exchange.diary.domain.sticker;

import com.exchange.diary.domain.image.StickerImage;
import com.exchange.diary.domain.diary.Diary;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stickerId;

    @JoinColumn(name = "diary_id")
    @ManyToOne
    private Diary diary;

    @OneToOne
    private StickerImage stickerImage;

    private int stickerNumX;

    private int stickerNumY;
}
