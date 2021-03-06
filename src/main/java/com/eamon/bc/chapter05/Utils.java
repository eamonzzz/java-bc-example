package com.eamon.bc.chapter05;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jcajce.spec.ScryptKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;

/**
 * @author: eamon
 * @date: 2019-01-30 17:53
 * @description:
 */
public class Utils {

    /**
     * Calculate a derived key using PBKDF2 based on SHA-256 using
     * the BC JCE provider.
     *
     * @param password       the password input.
     * @param salt           the salt parameter.
     * @param iterationCount the iteration count parameter.
     * @return the derived key.
     */
    public static byte[] jcePKCS5Scheme2(char[] password, byte[] salt, int iterationCount) throws GeneralSecurityException {
        SecretKeyFactory fact = SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", new BouncyCastleProvider());
        return fact.generateSecret(new PBEKeySpec(password, salt, iterationCount, 256)).getEncoded();
    }

    /**
     * Calculate a derived key using PBKDF2 based on SHA-256 using
     * the BC low-level API.
     *
     * @param password       the password input.
     * @param salt           the salt parameter.
     * @param iterationCount the iteration count parameter.
     * @return the derived key.
     */
    public static byte[] bcPKCS5Scheme2(char[] password, byte[] salt, int iterationCount) {
        PBEParametersGenerator generator = new PKCS5S2ParametersGenerator(new SHA256Digest());
        generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password), salt,
                iterationCount);
        return ((KeyParameter) generator.generateDerivedParameters(256)).getKey();
    }

    /**
     * Calculate a derived key using SCRYPT using the BC JCE provider. *
     *
     * @param password             the password input.
     * @param salt                 the salt parameter.
     * @param costParameter        the cost parameter.
     * @param blocksize            the blocksize parameter.
     * @param parallelizationParam the parallelization parameter.
     * @return the derived key.
     */
    public static byte[] jceSCRYPT(char[] password, byte[] salt, int costParameter, int blocksize,
                                   int parallelizationParam)
            throws GeneralSecurityException {
        SecretKeyFactory fact = SecretKeyFactory.getInstance("SCRYPT", "BC");
        return fact.generateSecret(new ScryptKeySpec(password, salt, costParameter, blocksize, parallelizationParam,
                256)).getEncoded();
    }

    /**
     * Calculate a derived key using SCRYPT using the BC low-level API. *
     *
     * @param password             the password input.
     * @param salt                 the salt parameter.
     * @param costParameter        the cost parameter.
     * @param blocksize            the blocksize parameter.
     * @param parallelizationParam the parallelization parameter.
     * @return the derived key.
     */
    public static byte[] bcSCRYPT(char[] password, byte[] salt, int costParameter, int blocksize,
                                  int parallelizationParam) {
        return SCrypt.generate(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password), salt, costParameter,
                blocksize, parallelizationParam, 256 / 8);
    }
}
