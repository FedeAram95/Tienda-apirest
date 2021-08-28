package com.deofis.tiendaapirest.emails.services;

import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    String build(String title, String message, String url) {
        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("message", message);
        context.setVariable("url", url);

        return templateEngine.process("mailTemplate", context);
    }

    String build(String title, String message, String url, Operacion content) {
        if (content == null) return "error";
        // Calcular mes a partir de int.
        String month = new SimpleDateFormat("MMMM").format(content.getFechaOperacion());
        month = month.substring(0, 1).toUpperCase() + month.substring(1);

        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("message", message);
        context.setVariable("url", url);
        context.setVariable("operacion", content);
        context.setVariable("medioPago", content.getMedioPago().getNombre());
        context.setVariable("month", month);

        return templateEngine.process("mailTemplate", context);
    }

}
