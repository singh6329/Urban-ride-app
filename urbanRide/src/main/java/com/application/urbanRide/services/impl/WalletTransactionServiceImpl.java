package com.application.urbanRide.services.impl;

import com.application.urbanRide.entities.WalletTransactions;
import com.application.urbanRide.repositories.WalletTransactionsRepository;
import com.application.urbanRide.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionsRepository walletTransactionsRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createNewWalletTransaction(WalletTransactions walletTransaction) {
        walletTransactionsRepository.save(walletTransaction);
    }
}
