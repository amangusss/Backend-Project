package alatoo.edu.kg.store.repository;

import alatoo.edu.kg.store.entity.Post;
import alatoo.edu.kg.store.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :login OR u.email = :login")
    Optional<User> findByLogin(String login);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @EntityGraph(attributePaths = {"favoritePosts", "favoritePosts.author"})
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favoritePosts WHERE u.id = :id")
    Optional<User> findByIdWithFavoritePosts(@Param("id") Long id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
