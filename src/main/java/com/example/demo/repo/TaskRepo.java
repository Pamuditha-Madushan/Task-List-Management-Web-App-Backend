package com.example.demo.repo;

import com.example.demo.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface TaskRepo extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM tasks WHERE id=?1", nativeQuery = true)
    public Optional<Task> findById(Long id);

    @Query(value = "SELECT * FROM tasks t WHERE t.title = :title AND t.description = :description AND t.status = :status", nativeQuery = true)
    Optional<Task> findByTitleAndDescriptionAndStatus(@Param("title") String title, @Param("description") String description, @Param("status") String status);

    @Query(value = "SELECT t FROM tasks t", nativeQuery = true)
    List<Task> findAllTasksWithPagination(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM tasks", nativeQuery = true)
    public long countAllTasks();

}
