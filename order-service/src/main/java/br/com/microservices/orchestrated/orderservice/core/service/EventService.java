package br.com.microservices.orchestrated.orderservice.core.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.dto.EventFilters;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {
  
  private final EventRepository repository;

  public void notifyEnding(Event event) {
    event.setOrderId(event.getOrderId());
    event.setCreatedAt(LocalDateTime.now());
    save(event);
    log.info("Order {} with saga notified! TransactionId: {}", event.getOrderId(), event.getTransactionId());
  }

  public List<Event> findAll() {
    return repository.findAllByOrderByCreatedAtDesc();
  }

  public Event findByFilters(EventFilters filters) {
    validateEmptyFilters(filters);
    if(!ObjectUtils.isEmpty(filters.getOrderId())) {
      return findByOrderId(filters.getOrderId());
    } else {
      return findByTransactionId(filters.getTransactionId());
    }
  }

  private Event findByOrderId(String orderId) {
    return repository
      .findTop1ByOrderIdOrderByCreatedAtDesc(orderId)
      .orElseThrow(() -> new ValidateException("Event not found by OrderId"));
  }

  private Event findByTransactionId(String transactionId) {
    return repository
      .findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId)
      .orElseThrow(() -> new ValidateException("Event not found by TransactionId"));
  }

  private void validateEmptyFilters(EventFilters filters) {
    if (ObjectUtils.isEmpty(filters.getOrderId()) && ObjectUtils.isEmpty(filters.getTransactionId())) {
      throw new ValidateException("OrderId or TransactionId must be informed");
    }
  }

  public Event save(Event event) {
    return repository.save(event);
  }
}
