package com.microservices.payment.integration;
import com.microservices.common.dtos.payment.CreatePaymentDto;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.repository.PaymentRepository;
import com.microservices.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
public class PaymentIntegrationTest {


}
