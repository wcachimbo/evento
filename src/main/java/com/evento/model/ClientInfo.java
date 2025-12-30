package com.evento.model;

import lombok.Data;

@Data
public class ClientInfo {
    private Long idClient;
    private Long company;
    private String nameClient;
    private String phone;
    private String address;
    private String alias;
    private String description;
}
