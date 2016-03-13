package com.mttnow.push.api.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

public class CryptographTest {

  @Test
  public void shouldBeAbleToDecryptEncryptedText() throws Exception{
    TimeBasedGenerator textGenerator = Generators.timeBasedGenerator();
    for(int x=0; x<100; x++){
      String text = textGenerator.generate().toString();
      String encrypted = Cryptograph.encrypt(text);
      String decrypted = Cryptograph.decrypt(encrypted);
      assertEquals("Token: " + text + "\tEncrypted: " + encrypted + "\tDecrypted: " + decrypted, text, decrypted);
    }
  }
  
}
