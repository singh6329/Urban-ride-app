package com.application.urbanRide.services.impl;

import com.application.urbanRide.dtos.MailBodyDto;
import com.application.urbanRide.entities.RideRequest;
import com.application.urbanRide.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final SimpleMailMessage simpleMailMessage;

    @Value("${spring.mail.username}")
    String sender;

    @Override
    public MailBodyDto prepareAcceptRideMail(RideRequest rideRequest) {

        String body="Hey,\n" +
                "\n" +
                "A new ride request has come through, and it’s waiting for your acceptance! If you’re ready to hit the road, just click the link below with the ride request number to accept the ride:\n" +
                "\n" +
                "http:localhost:8080/acceptRide/"+rideRequest.getId()+"\n" +
                "\n" +
                "Ride request details:\n" +
                "\n" +
                "Ride Request Number:"+rideRequest.getId()+"\n" +
                "Pickup Location: "+rideRequest.getPickUpLocation()+"\n" +
                "Drop-off Location:"+ rideRequest.getDropOffLocation()+"\n" +
                "Requested Time: "+ rideRequest.getRequestedTime() +"\n" +
                "Click the link, accept the ride, and you’ll be on your way!\n" +
                "\n" +
                "Looking forward to seeing you on the road. Safe travels!";
        String subject = "New Ride Request Available – Accept Now!";
        return MailBodyDto.builder().body(body).subject(subject).build();
    }

    @Override
    public void sendEmail(String recipient, String subject, String body) {
    try {
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);
        javaMailSender.send(simpleMailMessage);
        log.info("Mail sent Successfully!");

    }catch(Exception e)
    {
        log.info(e.getLocalizedMessage());
        throw new RuntimeException(e.getLocalizedMessage());
    }
    }

    @Override
    public void sendEmails(String[] recipient, String subject, String body) {
        try{

            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(sender);
            simpleMailMessage.setBcc(recipient);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);
            log.info("Mail sent successfully!");

        }catch(Exception e)
        {
            log.info(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }


}
