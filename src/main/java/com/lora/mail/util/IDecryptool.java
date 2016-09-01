package com.lora.mail.util;

public interface IDecryptool {
	/**
	 * 加密字符串
	 * 
	 * @param text
	 *            明文
	 * @return 密文
	 */
	public String encrypt(String text);
	
	/**
	 * 解密字符串
	 * 
	 * @param text
	 *            密文
	 * @return 明文
	 */
	public String decrypt(String text);

}
