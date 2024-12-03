package alatoo.edu.kg.store.repository;

import alatoo.edu.kg.store.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {
}
