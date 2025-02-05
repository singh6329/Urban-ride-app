package com.application.urbanRide.services;

import com.application.urbanRide.dtos.MailBodyDto;
import com.application.urbanRide.entities.RideRequest;

public interface EmailService {
    MailBodyDto prepareAcceptRideMail(RideRequest rideRequest);
    void sendEmail(String recipient,String subject,String body);
    void sendEmails(String[] recipient,String subject,String body);
}
