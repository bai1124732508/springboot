package com.fhzm.entity.generator;

import lombok.Data;

import java.io.Serializable;
@Data
public class IpVo  implements Serializable{
    private Integer code;
    private data data;

    @Data
    public class data implements Serializable {
        private String ip;
        private String region;
        private String city;
        private String country;



    }

}
