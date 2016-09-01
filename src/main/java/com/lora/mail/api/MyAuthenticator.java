package  com.lora.mail.api;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
	/**
	 * 用户名（登录邮箱）
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;

	/**
	 * 初始化邮箱和密码
	 * 
	 * @param username
	 *            邮箱
	 * @param password
	 *            密码
	 */
	public MyAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

	String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
