package com.exchange.diary.domain.image;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class BackgroundImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backgroundImageId;

    private String backgroundImageTitle;
}
