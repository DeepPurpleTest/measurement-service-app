package ua.ms.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ua.ms.entity.Sensor;
import ua.ms.entity.dto.SensorDto;
import ua.ms.service.SensorService;
import ua.ms.service.repository.SensorRepository;
import ua.ms.util.exception.SensorDuplicateException;
import ua.ms.util.exception.SensorNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static ua.ms.TestConstants.SENSOR_DTO;
import static ua.ms.TestConstants.SENSOR_ENTITY;


@SpringBootTest
@Transactional
@ActiveProfiles("test-env")
class SensorServiceTest {
    @Autowired
    private SensorService sensorService;
    @MockBean
    private SensorRepository sensorRepository;

    @Test
    void findAllShouldReturnList() {
        when(sensorRepository.findBy(PageRequest.of(0, 5), Sensor.class))
                .thenReturn(new ArrayList<>());
        assertThat(sensorService.findAll(PageRequest.of(0, 5), Sensor.class))
                .isNotNull();
    }

    @Test
    void findOneShouldReturnEntity() {
        when(sensorRepository.findById(1L, Sensor.class))
                .thenReturn(Optional.of(SENSOR_ENTITY));

        assertThat(sensorService.findOne(1L, Sensor.class))
                .isEqualTo(Optional.of(SENSOR_ENTITY));
    }

    @Test
    void findOneShouldReturnEmptyOptionalIfNotFound() {
        when(sensorRepository.findById(1L, Sensor.class))
                .thenReturn(Optional.empty());

        assertThat(sensorService.findOne(1L, Sensor.class))
                .isEmpty();
    }

    @Test
    void findOneShouldReturnValidType() {
        when(sensorRepository.findById(1L, Sensor.class))
                .thenReturn(Optional.of(SENSOR_ENTITY));
        when(sensorRepository.findById(1L, SensorDto.class))
                .thenReturn(Optional.of(SENSOR_DTO));

        assertThat(sensorService.findOne(1L, Sensor.class))
                .isEqualTo(Optional.of(SENSOR_ENTITY));
        assertThat(sensorService.findOne(1L, SensorDto.class))
                .isEqualTo(Optional.of(SENSOR_DTO));
    }

    @Test
    void updateShouldReturnEntity() {
        when(sensorRepository.findById(SENSOR_ENTITY.getId()))
                .thenReturn(Optional.of(SENSOR_ENTITY));
        when(sensorRepository.save(SENSOR_ENTITY))
                .thenReturn(SENSOR_ENTITY);

        assertThat(sensorService.update(1L, SENSOR_DTO))
                .isEqualTo(SENSOR_ENTITY);
    }

    @Test
    void updateShouldThrowExceptionIfSensorIsNotFound() {
        when(sensorRepository.findById(SENSOR_ENTITY.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sensorService.update(1L, SENSOR_DTO))
                .isInstanceOf(SensorNotFoundException.class);
    }

    @Test
    void deleteShouldReturnEntity() {
        when(sensorRepository.findById(SENSOR_ENTITY.getId()))
                .thenReturn(Optional.of(SENSOR_ENTITY));
        doNothing().when(sensorRepository).delete(SENSOR_ENTITY);

        assertThat(sensorService.delete(1L))
                .isEqualTo(SENSOR_ENTITY);
    }

    @Test
    void deleteShouldThrowExceptionIfSensorIsNotFound() {
        when(sensorRepository.findById(SENSOR_ENTITY.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sensorService.delete(1L))
                .isInstanceOf(SensorNotFoundException.class);
    }

    @Test
    void createShouldReturnEntity() {
        when(sensorRepository.findByName(SENSOR_ENTITY.getName(), Sensor.class))
                .thenReturn(Optional.empty());
        when(sensorRepository.save(SENSOR_ENTITY))
                .thenReturn(SENSOR_ENTITY);

        assertThat(sensorService.create(SENSOR_ENTITY)).isEqualTo(SENSOR_ENTITY);
    }

    @Test
    void createShouldThrowExceptionIfSensorWithThisNameIsAlreadyAdded() {
        when(sensorRepository.findByName(SENSOR_ENTITY.getName(), Sensor.class))
                .thenReturn(Optional.of(SENSOR_ENTITY));

        assertThatThrownBy(() -> sensorService.create(SENSOR_ENTITY))
                .isInstanceOf(SensorDuplicateException.class);
    }

}
