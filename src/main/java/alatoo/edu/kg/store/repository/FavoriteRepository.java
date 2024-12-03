package alatoo.edu.kg.store.repository;

import alatoo.edu.kg.store.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
