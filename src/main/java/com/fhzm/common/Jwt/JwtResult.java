package com.fhzm.common.Jwt;

import io.jsonwebtoken.Claims;
import lombok.Data;

@Data
public class JwtResult {

    private Claims claims;

    private boolean isSuccess;

    private String code; //0 成功  1.超时 2.报错



}
