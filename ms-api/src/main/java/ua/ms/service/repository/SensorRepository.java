package ua.ms.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.ms.entity.Sensor;


import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    <T> List<T> findBy(Pageable pagination, Class<T> type);
    <T> Optional<T> findById(long id, Class<T> type);
    <T> Optional<T> findByName(String name, Class<T> type);
    Sensor findFirstById(long id);

}