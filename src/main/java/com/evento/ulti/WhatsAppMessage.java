package com.evento.ulti;

import com.evento.model.OrdenDTO;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class WhatsAppMessage {

    public String buildMessage(OrdenDTO orden, Long id, String status) {

        StringBuilder msg = new StringBuilder();

        msg.append("Hola ").append(orden.getName()).append(" 👋\n\n");
        msg.append("Tu pedido *#").append(id).append("* fue Creado con estado: ").append(status).append(" \n\n");
        if (orden.getSubTotal().compareTo(orden.getTotal()) == 0) {
            msg.append(" 💵 *Pago total de :*\n").append(orden.getSubTotal());
        } else {
            msg.append(" 💵 *Abonaste:*\n").append(orden.getSubTotal());
        }
        msg.append("📦 *Productos:*\n");

        orden.getProducts().forEach(p ->
                msg.append("- ")
                        .append(p.getName())
                        .append(" x")
                        .append(p.getUnitValue())
                        .append("\n")
        );

        msg.append("\n🙏 Gracias por tu compra");

        if("P".equalsIgnoreCase(status)) {
            msg.append("\n🚨🚨🚨🚨🚨 *AVISO IMPORTANTE* 🚨🚨🚨🚨🚨\n");
            msg.append("Recuerde que los pedidos sin abono **no se reservan**. ");
            msg.append("Realice su pago lo antes posible para asegurar y separar sus artículos. ");
        }
        return msg.toString();

    }

    public String buildWhatsAppLink(String phone, String message) {
        return "https://wa.me/" + phone + "?text=" +
                URLEncoder.encode(message, StandardCharsets.UTF_8);
    }
}
