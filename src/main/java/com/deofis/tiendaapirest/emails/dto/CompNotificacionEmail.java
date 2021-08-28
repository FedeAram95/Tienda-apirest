package com.deofis.tiendaapirest.emails.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompNotificacionEmail implements NotificacionEmail {

    private String subject;
    private String recipient;
    private EmailBody body;
    private String redirectUrl;

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public String getRecipient() {
        return this.recipient;
    }

    @Override
    public Object getBody() {
        return this.body;
    }

    @Override
    public String getRedirectUrl() {
        return this.redirectUrl;
    }
}
