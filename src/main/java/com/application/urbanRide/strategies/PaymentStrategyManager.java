package com.application.urbanRide.strategies;

import com.application.urbanRide.entities.enums.PaymentMethod;
import com.application.urbanRide.strategies.impl.CashPaymentStrategy;
import com.application.urbanRide.strategies.impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {
    private final CashPaymentStrategy cashPaymentStrategy;
    private final WalletPaymentStrategy walletPaymentStrategy;

    public PaymentStrategy getPaymentStrategy(PaymentMethod paymentMethod)
    {
  return switch (paymentMethod)
  {
      case CASH -> cashPaymentStrategy;
      case WALLET -> walletPaymentStrategy;
  };

    }
}
