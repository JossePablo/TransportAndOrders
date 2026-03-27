package com.empresa.api.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.api.exception.ResourceNotFoundException;
import com.empresa.api.model.dto.OrderRequestDTO;
import com.empresa.api.model.entity.Driver;
import com.empresa.api.model.entity.Order;
import com.empresa.api.model.entity.OrderStatus;
import com.empresa.api.repository.DriverRepository;
import com.empresa.api.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Genera el constructor para la inyección de dependencias (Lombok)

public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	private final DriverRepository driverRepository;

	public Order updateStatus(UUID orderId, OrderStatus newStatus) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

		// Lógica de validación de flujo
		if (!isValidTransition(order.getStatus(), newStatus)) {
			throw new IllegalStateException(
					"Transición de estado no permitida de " + order.getStatus() + " a " + newStatus);
		}

		order.setStatus(newStatus);
		return orderRepository.save(order);
	}

	private boolean isValidTransition(OrderStatus current, OrderStatus next) {
		return switch (current) {
		case CREATED -> next == OrderStatus.IN_TRANSIT || next == OrderStatus.CANCELLED;
		case IN_TRANSIT -> next == OrderStatus.DELIVERED || next == OrderStatus.CANCELLED;
		case DELIVERED, CANCELLED -> false; // Estados finales, no se mueven
		};
	}

	// 1. Crear una nueva orden
	@Transactional
	public Order createOrder(OrderRequestDTO dto) {
		Order order = new Order();
		order.setOrigin(dto.origin());
		order.setDestination(dto.destination());
		order.setStatus(OrderStatus.CREATED); // Estado inicial obligatorio
		return orderRepository.save(order);
	}

	// 2. Consultar orden por ID
	@Transactional(readOnly = true)
	public Order findById(UUID id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("La orden con ID " + id + " no existe"));
	}

	// 3. Asignar un conductor a una orden
	@Transactional
	public Order assignDriver(UUID orderId, UUID driverId, MultipartFile pdf, MultipartFile image) {
		Order order = findById(orderId);

		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new EntityNotFoundException("Conductor no encontrado"));

		if (!driver.isActive()) {
			throw new IllegalStateException("No se puede asignar: El conductor no está activo");
		}

		if (order.getStatus() != OrderStatus.CREATED) {
			throw new IllegalStateException("Solo se pueden asignar conductores a órdenes en estado CREATED");
		}

		// Validación de archivos (PDF e Imagen)
		validateFiles(pdf, image);

		// Lógica de guardado
		order.setDriver(driver);
		order.setStatus(OrderStatus.IN_TRANSIT); // Cambio automático de flujo

		return orderRepository.save(order);
	}

	private void validateFiles(MultipartFile pdf, MultipartFile image) {
		if (pdf == null || !pdf.getContentType().equals("application/pdf")) {
			throw new IllegalArgumentException("El archivo de asignación debe ser un PDF");
		}
		String imgType = image.getContentType();
		if (image == null || !(imgType.equals("image/png") || imgType.equals("image/jpeg"))) {
			throw new IllegalArgumentException("La imagen debe ser .png o .jpg");
		}

	}
}
