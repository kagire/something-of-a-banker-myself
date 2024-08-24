package com.kagire.data.entity;

import com.kagire.data.misc.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Client {

    private String id;
    private String name;
    private ClientType type;
}
