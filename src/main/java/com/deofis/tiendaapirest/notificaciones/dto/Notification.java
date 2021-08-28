package com.deofis.tiendaapirest.notificaciones.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {
    private String title;
    private String content;
    private String category;
    private String actionUrl;
    private String user;
}
