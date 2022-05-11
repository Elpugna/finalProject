package com.applaudostudios.resourceserver.configuration.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
  KeycloakRealmRoleConverter() {
  }

  public Collection<GrantedAuthority> convert(final Jwt jwt) {
    ArrayList<GrantedAuthority> authorities = new ArrayList();
    Map<String, Object> realmRoles = (Map)jwt.getClaims().get("resource_access");
    String[] strings = realmRoles.get("resource-server").toString().split(":");
    String roles = strings[1].substring(1, strings[1].length() - 2);
    List<String> stringRoles = List.of(roles.split(","));
    Iterator var7 = stringRoles.iterator();

    while(var7.hasNext()) {
      String s = (String)var7.next();
      String var10003 = s.substring(1, s.length() - 1);
      authorities.add(new SimpleGrantedAuthority("ROLE_" + var10003));
    }

    String[] scopes = jwt.getClaims().get("scope").toString().split(" ");
    String[] var13 = scopes;
    int var9 = scopes.length;

    for(int var10 = 0; var10 < var9; ++var10) {
      String s = var13[var10];
      authorities.add(new SimpleGrantedAuthority("SCOPE_" + s));
    }

    return authorities;
  }
}
