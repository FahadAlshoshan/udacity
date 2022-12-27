package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final Logger logger = LoggerFactory.getLogger(CredentialService.class);
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void createCredential(Credential credential) {
        String key = generateKey();
        if (key != null) {
            String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
            Credential encryptedCredential = new Credential(
                    credential.getUrl(), key, credential.getUsername(), encryptedPassword, credential.getUserId());
            credentialMapper.insert(encryptedCredential);
        }
    }
    public void updateCredential(Credential credential){
        Credential credentialFromDB = credentialMapper.getCredentialById(credential.getId());
        String newEncryptedPassword = encryptionService.encryptValue(credential.getPassword(),credentialFromDB.getKey());
        credentialFromDB.setPassword(newEncryptedPassword);
        credentialFromDB.setUrl(credential.getUrl());
        credentialFromDB.setUsername(credential.getUsername());
        credentialMapper.update(credentialFromDB);
    }
    public void removeCredential(int id){
        credentialMapper.delete(id);
    }

    public List<Credential> getCredentials(int userId) {
        return credentialMapper.getAllUsersCredentials(userId);
    }


    private String generateKey() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            return Base64.getEncoder().encodeToString(key);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return null;
    }
}
