package ua.ms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ms.entity.Sensor;
import ua.ms.entity.dto.SensorDto;
import ua.ms.service.repository.SensorRepository;
import ua.ms.util.exception.SensorAlreadyExistException;
import ua.ms.util.exception.SensorNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;

    @Transactional(readOnly = true)
    public <T> List<T> findAll(Pageable pagination, Class<T> type) {
        return sensorRepository.findBy(pagination, type);
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findOne(long id, Class<T> type) {
        return sensorRepository.findById(id, type);
    }

    @Transactional
    public Sensor update(long id, SensorDto sensorDto) {
        Optional<Sensor> byId = sensorRepository.findById(id);
        if (byId.isEmpty())
            throw new SensorNotFoundException("Sensor is not found");

        Sensor sensorToUpdate = byId.get();
        Sensor updated = updateSensorFields(sensorToUpdate, sensorDto);
        return sensorRepository.save(updated);
    }

    private Sensor updateSensorFields(Sensor sensor, SensorDto sensorDto) {
        sensor.setName(sensorDto.getName() != null ? sensorDto.getName() : sensor.getName());
        return sensor;
    }

    @Transactional
    public Sensor delete(long id){
        Optional<Sensor> byId = sensorRepository.findById(id);
        if(byId.isEmpty())
            throw new SensorNotFoundException("Sensor is not found");

        sensorRepository.delete(byId.get());
        return byId.get();
    }

    @Transactional
    public Sensor create(Sensor sensorToCreate){
        Optional<Sensor> sensor = sensorRepository.findByName(sensorToCreate.getName(), Sensor.class);
        if(sensor.isPresent())
            throw new SensorAlreadyExistException("Sensor wit this name is already added");

        return sensorRepository.save(sensorToCreate);
    }
}
