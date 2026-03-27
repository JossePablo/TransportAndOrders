package com.empresa.api.model.dto;

import jakarta.validation.constraints.NotBlank;
/**
 * Clase request.
 * origin, Lugar de origen.
 * destination Lugar de destino.
 */

public record OrderRequestDTO(
	    @NotBlank(message = "El origen es obligatorio")
	    String origin,

	    @NotBlank(message = "El destino es obligatorio")
	    String destination
	) {}
