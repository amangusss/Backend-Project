package alatoo.edu.kg.store.repository;

import alatoo.edu.kg.store.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
