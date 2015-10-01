package br.com.dbcorp.escolaMinisterio;

import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

public class TestePHPCrip {

	private static String privada = "LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQ0KTUlJQ1hRSUJBQUtCZ1FDM3BtVU1VUS80NG83eHZMMkhSaFZQLzJxVkEvTkRCRGZGdENrbFJldU1iTGNRa1k1Ug0KVWpTTkloUFl2UWk3dXd0bnY1R1ZnVForUGt5TnlSZ092dS9MaWErK24yeFJLMDhma05xdkxNR2trZFg0VWo5Qw0KRXlTaGw0QUZFdkJ5WkNOMWI5TnZwZVZXMnl2Zjl5SXV5dW1SNXZKOGxMbXVPSXZQZmpHTkkvUkJQd0lEQVFBQg0KQW9HQWYzaUdjTnNmUEFCOWVYc3BEbWp0eUI0Z0s1aVhVKy9zaWxTM3JvQnVzNFNPT0hqZmtNQi9hMnE0M2Rxdg0KNGlZOUQyRWZ1dWI2SFB3L0JMY005TWRCQnhiZDZQZ2ZUd2NCY1c4aElPOWxBVW5STjlHWlNaVEFpUTBlUTNEcg0KTUd6WmpKL2o1U1dKVWZubDdiaiswVE9EN1MxY3FnNzEwYlFab3FSWWlOMjZMTmtDUVFEcmZSaHQyeHFVKzd2NQ0KcHNmRlJ0MEFZY0lmRmJpTmJZSFF5d1Y0bGNPUXRKTldmcUVxRHJhN1VxWmdEd3JHZHNub05YYlR0N2I2M0N4Ng0KN3hwMzBsYnJBa0VBeDZWbnZoTEV1YUdwcHhlaFBCUXdFS0JyYkNBbUxQdHd0MXBReklLN3lyL3JmcXE5Nnp5Yg0Kd0J3c3MvczlGTmpNQVlxNExBOGk5cG1xNUVSQ1JieFIvUUpCQU11K0dlckNUUWRsbmNkc0V4K09KaHYwZUwzbw0KVHhxZUNsa1pyb3djRjI0VnJmeUI1dks2ZEVNeVNSeUhKeTE3RFVuSktCd1pzVWp1UWRYREZjVmh5UzBDUUdObA0KNmFuTGhHQjdxWkRFaGdUNGRCbkRGTmluaFBvK1VaY29BelJmSG9wS1ZVQWlXQjRuZGRBRzl3YkEzbDlqdE9aTA0KbjNob0xOc2tGTjVEVWMrUWZDMENRUUNuL2RHd091clJqdkVHV0Y3dE1qR2lRQ3Iwa3BOeUJjZlVzZ3pFc2ZaNA0KbmNWTFEwekxYcWhvNzBPTFFKMmpBbTVEMTV1R0kzbU1vekVPeGJEbzEzbFQNCi0tLS0tRU5EIFJTQSBQUklWQVRFIEtFWS0tLS0t";
	private static String key ="LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0NCk1JR2ZNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0R05BRENCaVFLQmdRQzNwbVVNVVEvNDRvN3h2TDJIUmhWUC8ycVYNCkEvTkRCRGZGdENrbFJldU1iTGNRa1k1UlVqU05JaFBZdlFpN3V3dG52NUdWZ1RaK1BreU55UmdPdnUvTGlhKysNCm4yeFJLMDhma05xdkxNR2trZFg0VWo5Q0V5U2hsNEFGRXZCeVpDTjFiOU52cGVWVzJ5dmY5eUl1eXVtUjV2SjgNCmxMbXVPSXZQZmpHTkkvUkJQd0lEQVFBQg0KLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0t";
	
	public static PublicKey publicaFromString() throws Exception {
		if (Security.getProvider("BC") == null) {
		    Security.addProvider(new BouncyCastleProvider());
		}
		
		byte[] temp2 = Base64.getDecoder().decode(key);
		
		String temp = new String(temp2, "UTF-8");
		
		 PEMReader pemReader= new PEMReader(new StringReader(temp));
		 PublicKey pk = (PublicKey) pemReader.readObject();
		 
		 pemReader.close();
		 
		 return pk;
	}
	
	public static PrivateKey PrivadaFromString() throws Exception {
		if (Security.getProvider("BC") == null) {
		    Security.addProvider(new BouncyCastleProvider());
		}
		
		byte[] temp2 = Base64.getDecoder().decode(privada);
		
		String temp = new String(temp2, "UTF-8");
		
		PEMReader pemReader = new PEMReader(new StringReader(temp));
		KeyPair keyPair = (KeyPair) pemReader.readObject();
		
		pemReader.close();
		
		return keyPair.getPrivate();
	}
	
	public static void main(String[] args) throws Exception {
		if (Security.getProvider("BC") == null) {
		    Security.addProvider(new BouncyCastleProvider());
		}
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
		cipher.init(Cipher.ENCRYPT_MODE, publicaFromString());
		
		byte[] ciphered = cipher.doFinal("hello world".getBytes());
		
		Cipher cipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
		cipher2.init(Cipher.DECRYPT_MODE, PrivadaFromString());
		
		byte[] temp = cipher2.doFinal(ciphered);
		
		System.out.println(Base64.getEncoder().encodeToString((ciphered)));
		
		System.out.println(new String(temp, "UTF-8"));
	}
}