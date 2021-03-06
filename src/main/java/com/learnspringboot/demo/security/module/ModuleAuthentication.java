package com.learnspringboot.demo.security.module;

import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.security.UserPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModuleAuthentication extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 550L;
    private final Object principal;
    private String module;
    private String action;

    public ModuleAuthentication(Object principal, String module, String action) {
        super((Collection)null);
        this.principal = principal;
        this.module = module;
        this.action = action;
        this.setAuthenticated(authenticate());
    }

    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean authenticated){
        super.setAuthenticated(authenticated);
    }

    private boolean authenticate(){
        UserPrincipal userPrincipal = (UserPrincipal) principal;

        if(action.equalsIgnoreCase("GET"))
            return !(module.contains("/api/users") && userPrincipal == null);

        if(userPrincipal == null) return false;

        // check user is admin
        if(userPrincipal.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) return true;

        if(!userPrincipal.getRole().getName().equalsIgnoreCase("ROLE_USER")) return false;

        // except some module
        Map<String, Object> exceptModule = new HashMap<>();
        exceptModule.put("api/users/change-password", null);

        if(exceptModule.containsKey(module)) return true;

        Set<Permission> permissions = (Set<Permission>) userPrincipal.getPermission();
        for(Permission permission: permissions){
            if(permission.getModule().equalsIgnoreCase(module) && permission.getAction().equalsIgnoreCase(action)){
                return true;
            }
        }
        return false;
    }
}