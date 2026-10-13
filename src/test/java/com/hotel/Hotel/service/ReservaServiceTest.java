package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.*;
import com.hotel.Hotel.dto.response.ReservaResponse;
import com.hotel.Hotel.exception.RecursoNoEncontradoException;
import com.hotel.Hotel.mapper.ReservaMapper;
import com.hotel.Hotel.repository.ReservaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaService reservaService;

    @Nested
    @DisplayName("Operaciones de Confirmación")
    class ConfirmacionTests {

        @Test
        @DisplayName("Debe confirmar exitosamente cuando la reserva existe y está PENDIENTE")
        void debeConfirmarReservaExitosamente() {
            // Arrange
            UUID reservaId = UUID.randomUUID();
            Cliente cliente = new Cliente("Beatriz Morales", "beatriz@empresa.com");
            Habitacion habitacion = new HabitacionEstandar("201", 2, 200.0, 1);

            RangoFechas fechas = new RangoFechas(
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));
            Reserva reservaExistente = new Reserva(cliente, habitacion, fechas);

            ReservaResponse responseEsperado = new ReservaResponse(
                    reservaId,
                    "Beatriz Morales",
                    "201",
                    fechas.fechaInicio(),
                    fechas.fechaFin(),
                    400.0,
                    "CONFIRMADA");

            // Stubs
            when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
            when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reservaMapper.toResponse(any(Reserva.class))).thenReturn(responseEsperado);

            // Act
            ReservaResponse resultado = reservaService.confirmar(reservaId);

            // Assert
            assertNotNull(resultado);
            assertEquals("CONFIRMADA", resultado.estado());
            assertEquals(EstadoReserva.CONFIRMADA, reservaExistente.getEstado());

            // Verificación de comportamiento (Mocks)
            verify(reservaRepository, times(1)).findById(reservaId);
            verify(reservaRepository, times(1)).save(reservaExistente);
            verify(reservaMapper, times(1)).toResponse(reservaExistente);
        }

        @Test
        @DisplayName("Debe cancelar exitosamente cuando la reserva existe")
        void debeCancelarReservaExitosamente() {
            // Arrange
            UUID reservaId = UUID.randomUUID();
            Cliente cliente = new Cliente("Beatriz Morales", "beatriz@empresa.com");
            Habitacion habitacion = new HabitacionEstandar("201", 2, 200.0, 1);

            RangoFechas fechas = new RangoFechas(
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));
            Reserva reservaExistente = new Reserva(cliente, habitacion, fechas);

            ReservaResponse responseEsperado = new ReservaResponse(
                    reservaId,
                    "Beatriz Morales",
                    "201",
                    fechas.fechaInicio(),
                    fechas.fechaFin(),
                    400.0,
                    "CANCELADA");

            // Stubs
            when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
            when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reservaMapper.toResponse(any(Reserva.class))).thenReturn(responseEsperado);

            // Act
            ReservaResponse resultado = reservaService.cancelar(reservaId);

            // Assert
            assertNotNull(resultado);
            assertEquals("CANCELADA", resultado.estado());
            assertEquals(EstadoReserva.CANCELADA, reservaExistente.getEstado());

            // Verificación de comportamiento (Mocks)
            verify(reservaRepository, times(1)).findById(reservaId);
            verify(reservaRepository, times(1)).save(reservaExistente);
            verify(reservaMapper, times(1)).toResponse(reservaExistente);
        }

        @Test
        @DisplayName("Debe obtener una reserva exitosamente cuando existe")
        void debeObtenerReservaExitosamente() {
            // Arrange
            UUID reservaId = UUID.randomUUID();
            Cliente cliente = new Cliente("Beatriz Morales", "beatriz@empresa.com");
            Habitacion habitacion = new HabitacionEstandar("201", 2, 200.0, 1);

            RangoFechas fechas = new RangoFechas(
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));
            Reserva reservaExistente = new Reserva(cliente, habitacion, fechas);

            ReservaResponse responseEsperado = new ReservaResponse(
                    reservaExistente.getId(),
                    "Beatriz Morales",
                    "201",
                    fechas.fechaInicio(),
                    fechas.fechaFin(),
                    400.0,
                    "PENDIENTE");

            // Stubs
            when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
            when(reservaMapper.toResponse(reservaExistente)).thenReturn(responseEsperado);

            // Act
            ReservaResponse resultado = reservaService.obtenerPorId(reservaId);

            // Assert
            assertNotNull(resultado);
            assertEquals("PENDIENTE", resultado.estado());
            assertEquals(reservaExistente.getId(), resultado.id());

            // Verificación de comportamiento (Mocks)
            verify(reservaRepository, times(1)).findById(reservaId);
            verify(reservaMapper, times(1)).toResponse(reservaExistente);
        }

        @Test
        @DisplayName("Debe listar todas las reservas exitosamente")
        void debeListarTodasLasReservasExitosamente() {
            // Arrange
            Cliente cliente = new Cliente("Beatriz Morales", "beatriz@empresa.com");
            Habitacion habitacion = new HabitacionEstandar("201", 2, 200.0, 1);

            RangoFechas fechas = new RangoFechas(
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));
            Reserva reservaExistente = new Reserva(cliente, habitacion, fechas);

            ReservaResponse responseEsperado = new ReservaResponse(
                    reservaExistente.getId(),
                    "Beatriz Morales",
                    "201",
                    fechas.fechaInicio(),
                    fechas.fechaFin(),
                    400.0,
                    "PENDIENTE");

            List<Reserva> reservasEsperadas = List.of(reservaExistente);
            List<ReservaResponse> respuestasEsperadas = List.of(responseEsperado);

            // Stubs
            when(reservaRepository.findAll()).thenReturn(reservasEsperadas);
            when(reservaMapper.toResponseList(reservasEsperadas)).thenReturn(respuestasEsperadas);

            // Act
            List<ReservaResponse> resultado = reservaService.listarTodas();

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals("PENDIENTE", resultado.get(0).estado());

            // Verificación de comportamiento (Mocks)
            verify(reservaRepository, times(1)).findAll();
            verify(reservaMapper, times(1)).toResponseList(reservasEsperadas);
        }

    }
}