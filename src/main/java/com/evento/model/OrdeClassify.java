package com.evento.model;

import java.util.List;

public record OrdeClassify(

        List<OrdenQuerys> today,
        List<OrdenQuerys> tomorrow,
        List<OrdenQuerys> orden
) {
}
