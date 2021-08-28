package com.deofis.tiendaapirest.catalogo.consultas.services;

import com.deofis.tiendaapirest.catalogo.consultas.dto.ConsultaPayload;
import com.deofis.tiendaapirest.catalogo.productos.services.ProductoService;
import com.deofis.tiendaapirest.emails.dto.SimpleNotificacionEmail;
import com.deofis.tiendaapirest.emails.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ConsultaServiceImpl implements ConsultaService {

    private final MailService mailService;
    private final ProductoService productoService;

    @Autowired
    public ConsultaServiceImpl(@Qualifier("simpleMailService") MailService mailService,
                               ProductoService productoService) {
        this.mailService = mailService;
        this.productoService = productoService;
    }

    @Override
    public void enviarConsulta(ConsultaPayload consultaPayload) {
        Long productId = consultaPayload.getProductoId();
        String productName = this.productoService.obtenerProducto(productId).getNombre();

        String subject = "Consulta por producto";
        String bodyTitle = "Nueva consulta para producto " + productName + " con id: " + productId;
        String bodyMessage = consultaPayload.getMensaje().concat(".\n Email:  ".concat(consultaPayload.getEmail()));
        if (consultaPayload.getTelefono() != null) bodyMessage = bodyMessage.concat("\n Teléfono: "
                .concat(consultaPayload.getTelefono()));

        this.enviarEmails(subject, bodyTitle, bodyMessage);
    }

    /**
     * Envía email a la cuenta de consultas.
     * @param subject String subject.
     * @param title String titulo email.
     * @param body String cuerpo con el mensaje del email.
     */
    private void enviarEmails(String subject, String title, String body) {
        SimpleNotificacionEmail notificacionEmail = SimpleNotificacionEmail.builder()
                .recipient("consultas@deofis.com")
                .subject(subject)
                .title(title)
                .body(body).build();

        this.mailService.sendEmail(notificacionEmail);
    }
}
