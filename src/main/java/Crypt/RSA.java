package Crypt;

/**
 * Created by cephalgia on 20.03.17.
 */

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger fn;
    private BigInteger e;
    private BigInteger d;
    private final static SecureRandom random = new SecureRandom();

    public void init(int X) {
        p = BigInteger.probablePrime(X >> 1, random);
        q = BigInteger.probablePrime(X >> 1, random);
        fn = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        N = p.multiply(q);
        e = new BigInteger("65537");
        System.out.println("Public key = " + e);
        d = e.modInverse(fn);
        System.out.println("Private key = " + d);
    }

    BigInteger encrypt(BigInteger message) {
        return message.modPow(e, N);
    }

    BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(d, N);
    }

    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.init(1024);
        String messsge = "Lol kek cheburek";
        System.out.println("Message = " + messsge);
        BigInteger messageInBytes = new BigInteger(messsge.getBytes());
        System.out.println("Message in bytes = " + messageInBytes);
        BigInteger encryptMessage = rsa.encrypt(messageInBytes);
        System.out.println("Encrypted message = " + encryptMessage);
        System.out.println(encryptMessage);
        System.out.println();
        BigInteger decryptMessage = rsa.decrypt(encryptMessage);
        System.out.println("Decrypted message in bytes = " + decryptMessage);
        System.out.println("Decrypted message = " + new String(decryptMessage.toByteArray()));
    }
}