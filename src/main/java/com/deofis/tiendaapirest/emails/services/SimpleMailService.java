package com.deofis.tiendaapirest.emails.services;

import com.deofis.tiendaapirest.autenticacion.exceptions.MailSenderException;
import com.deofis.tiendaapirest.emails.dto.NotificacionEmail;
import com.deofis.tiendaapirest.emails.dto.SimpleNotificacionEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Implementación simple de {@link MailService}, la notificación está compuesta por un subject (asunto),
 * un recipient (destinatario), un title (título) y un body (cuerpo con mensaje).
 */
@Service
@Qualifier("simpleMailService")
@AllArgsConstructor
@Slf4j
public class SimpleMailService implements MailService {

    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;

    private final String mailFrom;

    @Override
    @Async
    public void sendEmail(NotificacionEmail genericNotificacionEmail) {
        SimpleNotificacionEmail notificacionEmail = (SimpleNotificacionEmail) genericNotificacionEmail;
        String title = notificacionEmail.getTitle();
        String message = (String) notificacionEmail.getBody();
        String redirectUrl = notificacionEmail.getRedirectUrl();

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(mailFrom);
            messageHelper.setTo(notificacionEmail.getRecipient());
            messageHelper.setSubject(notificacionEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(title, message, redirectUrl), true);
        };

        try {
            javaMailSender.send(messagePreparator);
            log.info("Email sent!");
        } catch (MailException e) {
            throw new MailSenderException("Excepción al enviar email: " + e.getMessage());
        }
    }
}
