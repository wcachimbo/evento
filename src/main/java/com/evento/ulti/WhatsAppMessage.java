package com.evento.ulti;

import com.evento.model.OrdenDTO;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class WhatsAppMessage {

    public String buildMessage(OrdenDTO orden, Long id) {

        StringBuilder msg = new StringBuilder();

        msg.append("Hola ").append(orden.getName()).append(" 👋\n\n");
        msg.append("Tu pedido *#").append(id).append("* fue confirmado ✅\n\n");
        msg.append("📦 *Productos:*\n");

        orden.getProducts().forEach(p ->
                msg.append("- ")
                        .append(p.getName())
                        .append(" x")
                        .append(p.getUnitValue())
                        .append("\n")
        );

        msg.append("\n🙏 Gracias por tu compra");

        return msg.toString();
    }

    public String buildWhatsAppLink(String phone, String message) {
        return "https://wa.me/" + phone + "?text=" +
                URLEncoder.encode(message, StandardCharsets.UTF_8);
    }
}
