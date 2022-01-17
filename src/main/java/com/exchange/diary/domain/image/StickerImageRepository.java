package com.exchange.diary.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerImageRepository extends JpaRepository<StickerImage,Long> {
}
