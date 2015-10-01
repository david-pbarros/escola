package br.com.dbcorp.escolaMinisterio;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;


public class TesteCrip {
	public static KeyPair keyPairGenerator (String algorithm, int keysize) throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
		kpg.initialize(keysize);
		KeyPair kp = kpg.generateKeyPair();
		return kp;
	}
	
	public static byte[] encrypt (byte[] inputBytes, PublicKey key, String xform) throws Exception {
		Cipher cipher = Cipher.getInstance(xform);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(inputBytes);
	}
	
	public static byte[] decrypt (byte[] inputBytes, PrivateKey key,  String xform) throws Exception{
		Cipher cipher = Cipher.getInstance(xform);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(inputBytes);
	}
	
	public static void main(String[] args) throws Exception {
		
		KeyPair kp = TesteCrip.keyPairGenerator("RSA", 512);
		PublicKey pubk = kp.getPublic();
		PrivateKey prvk = kp.getPrivate();
		
		String publicaSave = Base64.getEncoder().encodeToString(pubk.getEncoded());
		String privadaSave = Base64.getEncoder().encodeToString(prvk.getEncoded());
		
		System.out.println("Publica: " + publicaSave);
		System.out.println("Privada: " + privadaSave);
		
		byte[] prKeyGuardar = Base64.getDecoder().decode(privadaSave);
		
		//PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pbKeyGuardar));
		PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(prKeyGuardar));
		
		//encryption
		String xform = "RSA/ECB/PKCS1Padding";
		byte[] encBytes = TesteCrip.encrypt("maria tinha um carneirinho".getBytes(), pubk, xform);
		
		// Decrypt
		byte[] decBytes = TesteCrip.decrypt(encBytes, privateKey, xform);
		
		
		/*System.out.println("Emc " + new String(encBytes, "UTF-8") );*/
		System.out.println("Desc " + new String(decBytes, "UTF-8") );
		
		String senha = Base64.getEncoder().encodeToString("teste".getBytes());
		byte[] senhaD = Base64.getDecoder().decode(senha);
		
		System.out.println("SenhaC: " + senha);
		System.out.println("SenhaD: " +  new String(senhaD, "UTF-8"));
		
	}
}
