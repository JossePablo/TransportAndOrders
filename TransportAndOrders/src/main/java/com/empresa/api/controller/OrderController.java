package com.empresa.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.empresa.api.model.dto.OrderRequestDTO;
import com.empresa.api.model.entity.Order;
import com.empresa.api.model.entity.OrderStatus;
import com.empresa.api.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Management", description = "Endpoints para la gestión de órdenes de transporte")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	/**
	 * Crea una nueva orden de transporte y la asigna a un conductor específico.
	 * @param orderDto Datos básicos de la orden (origen/destino).
	 * @param driverId Identificador único del conductor.
	 * @param pdf Archivo PDF de la guía.
	 * @param image Imagen del paquete.
	 * @return La orden creada con su estado inicial.
	 * @throws ResourceNotFoundException si el conductor no existe.
	 */
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Crear y asignar una orden con archivos")
	public ResponseEntity<Order> createAndAssign(@RequestPart("order") @Valid OrderRequestDTO dto,
			@RequestPart("pdf") MultipartFile pdf, @RequestPart("image") MultipartFile image,
			@RequestParam UUID driverId) {

		// Aquí llamarías a tu lógica de servicio
		Order savedOrder = orderService.createOrder(dto);
		Order assignedOrder = orderService.assignDriver(savedOrder.getId(), driverId, pdf, image);

		return new ResponseEntity<>(assignedOrder, HttpStatus.CREATED);
	}
	
	/**
	 * Busca la información de una orden en especifico a ytravez del id.
	 * @PathVariable id de la orden que se desea buscar.
	 * @throws ResourceNotFoundException si la orden no existe.
	 */

	@GetMapping("/{id}")
	public ResponseEntity<Order> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(orderService.findById(id));
	}
	
	/**
	 * actualiza el estatus de una orden a travez del id.
	 * @PathVariable id de la orden que se desea actualizar.
	 * @RequestParam OrderStatus status el nuevo estatus a enviar.
	 * @throws ResourceNotFoundException si la orden no existe.
	 */

	@PatchMapping("/{id}/status")
	public ResponseEntity<Order> updateStatus(@PathVariable UUID id, @RequestParam OrderStatus status) {
		return ResponseEntity.ok(orderService.updateStatus(id, status));
	}
}
