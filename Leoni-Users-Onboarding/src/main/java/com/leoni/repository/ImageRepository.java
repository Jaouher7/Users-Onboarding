package com.leoni.repository;

import com.leoni.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository <Image, Long> {
}
