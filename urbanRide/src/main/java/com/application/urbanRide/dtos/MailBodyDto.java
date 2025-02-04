package com.application.urbanRide.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailBodyDto {
    private String subject;
    private String body;
}
