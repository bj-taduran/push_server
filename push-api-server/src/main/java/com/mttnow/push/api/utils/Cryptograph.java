package com.mttnow.push.api.utils;

import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * Class for encrypting and/or decrypting tokens (texts).
 * 
 */

public class Cryptograph {
  
  // DO NOT CHANGE ANYTHING !!
  private static final String PBKDF2_WITH_HMAC_SHA1 = "PBKDF2WithHmacSHA1";
  private static final String UTF8 = "utf-8";
  private static final int ITERATIONS = 1024;
  private static final int KEY_LENGTH = 256;
  private static final String CODE = "mtt-push";
  
  /**
   * Encrypts a given text using AES algorithm
   * 
   * @param text for encryption
   * @return encrypted text
   * @throws Exception
   */
  
  public static String encrypt(String input) throws Exception {
    final byte[] output = cipher(input.trim().getBytes(UTF8), true);
    return Base64.encodeBase64URLSafeString(output);
  }
  
  /**
   * Decrypts a given code using AES algorithm
   * 
   * @param code to be decrypted
   * @return decripted text
   * @throws Exception
   */
  
  public static String decrypt(String input) throws Exception {
    final byte[] output = cipher(Base64.decodeBase64(input), false);
    return new String(output, UTF8).trim();
  }

  private static byte[] cipher(byte[] bytes, boolean encrypt) throws Exception {
    // Initialize cipher
    final SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA1);
    final KeySpec keySpec = new PBEKeySpec(CODE.toCharArray(), CODE.getBytes(UTF8), ITERATIONS, KEY_LENGTH);
    final SecretKey secretKey = factory.generateSecret(keySpec);

    final PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESEngine(), new PKCS7Padding());
    final KeyParameter key = new KeyParameter(secretKey.getEncoded());
    cipher.init(encrypt, key);
    
    // Process encryption/decryption
    final byte[] output = new byte[cipher.getOutputSize(bytes.length)];
    final int bytesWrittenOut = cipher.processBytes(bytes, 0, bytes.length, output, 0);

    cipher.doFinal(output, bytesWrittenOut);
    return output;
  }
  
}


