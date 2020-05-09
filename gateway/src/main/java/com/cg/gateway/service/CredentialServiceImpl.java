package com.cg.gateway.service;

import com.cg.gateway.dao.CredentialDao;
import com.cg.gateway.entities.UserCredential;
import com.cg.gateway.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CredentialServiceImpl implements CredentialService {

    private CredentialDao dao;

    public CredentialDao getDao() {
        return dao;
    }

    @Autowired
    public void setDao(CredentialDao dao) {
        this.dao = dao;
    }


    @Override
    public UserCredential save(UserCredential credential) {
        credential = dao.save(credential);
        return credential;
    }


    @Override
    public boolean checkCredentials(String id, String password) {
        if(id==null || id.isEmpty() || password==null || password.isEmpty()){
            return false;
        }
        UserCredential user = findById(id);
        if (user == null) {
            return false;
        }
        return user.getId().equals(id) && user.getPassword().equals(password);
    }

    @Override
    public UserCredential findById(String id) {
        Optional<UserCredential> optional = dao.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new UserNotFoundException("user credentials not found in store");
    }

    @Override
    public boolean isAdmin(String id) {
       UserCredential userCredential= findById(id);
       return "admin".equalsIgnoreCase(userCredential.getRole());
    }
}
