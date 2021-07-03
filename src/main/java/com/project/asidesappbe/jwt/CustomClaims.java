package com.project.asidesappbe.jwt;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Map;

public interface CustomClaims extends Claims {
    String AUTHORITIES = "aut";

    List<Map<String, String>> getAuthoritiesAsList();
    Claims setAuthoritiesAsList(List<Map<String, String>> authorities);

    <T> T get(String var1, Class<T> var2);
}
