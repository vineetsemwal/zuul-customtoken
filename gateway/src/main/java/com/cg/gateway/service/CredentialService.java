package com.cg.gateway.service;

import com.cg.gateway.entities.UserCredential;

public interface CredentialService {

    boolean checkCredentials(String id, String password);

    UserCredential findById(String id);

    UserCredential save(UserCredential user);

    boolean isAdmin(String id);

}
