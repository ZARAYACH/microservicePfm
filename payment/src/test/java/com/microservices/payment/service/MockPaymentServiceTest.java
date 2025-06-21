package com.microservices.payment.service;
import com.microservices.common.exception.PaymentException;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.mapper.PaymentMapper;
import com.microservices.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MockPaymentServiceTest {
    @Mock
    private PaymentRepository repository;

    @Mock
    private PaymentMapper mapper;

    @InjectMocks
    private PaymentService service;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        service = new PaymentService(repository, mapper);
//    }

//    @Test
//    void processPayment_shouldSucceed_withValidCard() throws PaymentException {
//        PaymentRequestDto dto = new PaymentRequestDto();
//        dto.setCardHolder("test name");
//        dto.setAmount(100.0)
//        dto.setCardNumber("4111222233334444");
//
//        Payment mockEntity = new Payment("1",dto.getCardHolder(), dto.getCardNumber(), dto.getAmount(),true,"Mock payment successful","xx-xx-xxxx");
//        PaymentResponseDto responseDto = new PaymentResponseDto("1","test name",100.0,true,"Mock payment successful","xx-xx-xxxx");
//
//        when(repository.save(any(Payment.class))).thenReturn(mockEntity);
//        when(mapper.toDto(any(Payment.class))).thenReturn(responseDto);
//
//        PaymentResponseDto result = service.processPayment(dto);
//
//        assertTrue(result.isSuccess());
//        assertEquals("Mock payment successful", result.getMessage());
//    }
//
//    @Test
//    void processPayment_shouldFail_withInvalidCard() {
//        PaymentRequestDto dto = new PaymentRequestDto();
//        dto.setCardNumber("9999888877776666");
//
//        PaymentException ex = assertThrows(PaymentException.class, () -> service.processPayment(dto));
//        assertEquals("Payment failed: invalid card prefix.", ex.getMessage());
//    }
//
//    @Test
//    void getById_shouldReturnResponseIfExists() {
//        Payment payment = new Payment("1","test name","4111222233334444", 100.0, true, "ok","xx-xx-xxxx");
//        when(repository.findById("1")).thenReturn(Optional.of(payment));
//        when(mapper.toDto(payment)).thenReturn(new PaymentResponseDto("1","test name",100.0,true,"ok","xx-xx-xxxx"));
//
//        Optional<PaymentResponseDto> result = service.getById("1");
//
//        assertTrue(result.isPresent());
//        assertEquals("ok", result.get().getMessage());
//    }
}
