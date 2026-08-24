package com.shopsphere.payment;
import jakarta.validation.Valid; import jakarta.validation.constraints.DecimalMin; import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.math.BigDecimal; import java.time.OffsetDateTime;
@RestController @RequestMapping("/api/payments")
public class PaymentController {
 @GetMapping("/health") public String health(){return "payment-service-ok";}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public PaymentResponse pay(@Valid @RequestBody PaymentRequest r){return new PaymentResponse("PAY-"+r.orderId(),r.orderId(),r.amount(),"PAID",OffsetDateTime.now());}
 public record PaymentRequest(@NotNull Long orderId,@NotNull @DecimalMin("0.01") BigDecimal amount){}
 public record PaymentResponse(String paymentId,Long orderId,BigDecimal amount,String status,OffsetDateTime processedAt){}
}
