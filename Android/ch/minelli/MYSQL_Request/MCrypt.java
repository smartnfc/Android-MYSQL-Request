/**
 * Thanks to androidsnippets for this ancryption/decryption class
 *
 * @author http://www.androidsnippets.com/encrypt-decrypt-between-android-and-php
 */

package ch.minelli.MYSQL_Request;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

public class MCrypt
{

	private String          iv;
	private IvParameterSpec ivspec;
	private SecretKeySpec   keyspec;
	private Cipher          cipher;

	private String SecretKey;

	public MCrypt(String param_iv, String param_Secret_Key)
	{

		iv = param_iv;
		SecretKey = param_Secret_Key;

		ivspec = new IvParameterSpec(iv.getBytes());

		keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

		try
		{
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
		}
		catch (NoSuchAlgorithmException ignored)
		{ }
		catch (NoSuchPaddingException ignored)
		{ }
	}

	public byte[] encrypt(String text)
	{

		byte[] encrypted = null;

		try
		{
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

			encrypted = cipher.doFinal(padString(text).getBytes());
		}
		catch (Exception ignored)
		{ }

		return encrypted;
	}

	public byte[] decrypt(String code) throws Exception
	{
		if (code == null || code.length() == 0)
			throw new Exception("Empty string");

		byte[] decrypted = null;

		try
		{
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			decrypted = cipher.doFinal(hexToBytes(code));
		}
		catch (Exception e)
		{
			throw new Exception("[decrypt] " + e.getMessage());
		}
		return decrypted;
	}

	public static String bytesToHex(byte[] data)
	{
		if (data == null)
		{
			return null;
		}

		int len = data.length;
		String str = "";
		for (int i = 0; i < len; i++)
		{
			if ((data[i] & 0xFF) < 16)
				str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
			else
				str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
		}
		return str;
	}

	public static byte[] hexToBytes(String str)
	{
		if (str == null)
		{
			return null;
		}
		else if (str.length() < 2)
		{
			return null;
		}
		else
		{
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++)
			{
				buffer[i] = (byte)Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
			}
			return buffer;
		}
	}

	private static String padString(String source)
	{
		char paddingChar = ' ';
		int size = 16;
		int x = source.length() % size;
		int padLength = size - x;

		for (int i = 0; i < padLength; i++)
		{
			source += paddingChar;
		}

		return source;
	}
}
