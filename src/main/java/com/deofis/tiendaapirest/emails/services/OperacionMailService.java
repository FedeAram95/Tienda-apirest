package com.deofis.tiendaapirest.emails.services;

import com.deofis.tiendaapirest.autenticacion.exceptions.MailSenderException;
import com.deofis.tiendaapirest.emails.dto.EmailBody;
import com.deofis.tiendaapirest.emails.dto.NotificacionEmail;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
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
 * Implementación orientada al envío de emails para {@link com.deofis.tiendaapirest.operaciones.entities.Operacion}es.
 * Implementa {@link MailService} para concretar el envío.
 */
@Service
@Qualifier("operacionMailService")
@AllArgsConstructor
@Slf4j
public class OperacionMailService implements MailService {

    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;

    private final String mailFrom;

    @Override
    @Async
    public void sendEmail(NotificacionEmail notificacionEmail) {
        // Casteamos el body para adecuarse a EmailBody compuesto.
        EmailBody body = (EmailBody) notificacionEmail.getBody();

        String title = body.getTitle();
        String message = body.getMessage();
        Operacion content = (Operacion) body.getContent();
        String redirectUrl = notificacionEmail.getRedirectUrl();

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(mailFrom);
            messageHelper.setTo(notificacionEmail.getRecipient());
            messageHelper.setSubject(notificacionEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(title, message, redirectUrl, content), true);
        };

        try {
            javaMailSender.send(messagePreparator);
            log.info("Email sent!");
        } catch (MailException e) {
            throw new MailSenderException("Excepción al enviar email: " + e.getMessage());
        }
    }
}
