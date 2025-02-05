package com.application.urbanRide.services;

import com.application.urbanRide.entities.Ride;
import com.application.urbanRide.entities.User;
import com.application.urbanRide.entities.Wallet;
import com.application.urbanRide.entities.enums.TransactionMethod;

public interface WalletService {
    Wallet createNewWallet(User user);
    void withdrawAllMoneyFromWallet();
    Wallet findWalletById(Long walletId);
    Wallet addFundsToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);
    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);
    Wallet findByUser(User user);
}
