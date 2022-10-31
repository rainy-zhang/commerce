package org.rainy.commerce.service;

import com.alibaba.nacos.common.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RSATest {

    @Test
    public void generateKeyBytes() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        log.info("private key: [{}]", new String(Base64.encodeBase64(privateKey.getEncoded())));
        log.info("public key: [{}]", new String(Base64.encodeBase64(publicKey.getEncoded())));
    }

}
