package alatoo.edu.kg.store.repository;

import alatoo.edu.kg.store.entity.PostHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostHistoryRepository extends JpaRepository<PostHistory, Long> {

    List<PostHistory> findByPostIdOrderByVersionDesc(Long postId);
    Optional<PostHistory> findByPostIdAndVersion(Long postId, Integer version);
}
