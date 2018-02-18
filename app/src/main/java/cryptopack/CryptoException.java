package cryptopack;

/**
 * http://www.codejava.net/coding/file-encryption-and-decryption-simple-example
 */
public class CryptoException extends Exception{
    public CryptoException(){}

    public CryptoException(String message, Throwable throwable){
        super(message,throwable);
    }
}
