package com.application.urbanRide.services.impl;

import com.application.urbanRide.custExceptions.ResourceNotFoundException;
import com.application.urbanRide.entities.Ride;
import com.application.urbanRide.entities.User;
import com.application.urbanRide.entities.Wallet;
import com.application.urbanRide.entities.WalletTransactions;
import com.application.urbanRide.entities.enums.TransactionMethod;
import com.application.urbanRide.entities.enums.TransactionType;
import com.application.urbanRide.repositories.WalletRepository;
import com.application.urbanRide.services.WalletService;
import com.application.urbanRide.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId).orElseThrow(()->
                new ResourceNotFoundException("Wallet not found with id:"+walletId));
    }

    @Override
    @Transactional
    public Wallet addFundsToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance()+amount);
        WalletTransactions walletTransactions = WalletTransactions.builder().transactionId(transactionId)
                   .transactionType(TransactionType.CREDIT).transactionMethod(transactionMethod)
                .ride(ride).amount(amount).build();
        walletTransactionService.createNewWalletTransaction(walletTransactions);
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet= findByUser(user);
        wallet.setBalance(wallet.getBalance()-amount);
        WalletTransactions walletTransactions = WalletTransactions.builder().transactionId(transactionId)
                .transactionType(TransactionType.DEBIT).transactionMethod(transactionMethod)
                .ride(ride).amount(amount).build();
        walletTransactionService.createNewWalletTransaction(walletTransactions);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user).orElseThrow(()->
                new RuntimeException("Wallet with user id doesn't exists: "+user.getId()));
    }
}
