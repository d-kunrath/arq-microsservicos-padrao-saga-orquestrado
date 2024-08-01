package br.com.microservices.orchestrated.inventoryservice.core.dto;

import java.time.LocalDateTime;

import br.com.microservices.orchestrated.inventoryservice.core.enums.ESagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {

  private String source;
  private ESagaStatus status;
  private String message;
  private LocalDateTime createdAt;
}
