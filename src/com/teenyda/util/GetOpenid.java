package com.teenyda.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.xfire.util.Base64;

import com.mysql.jdbc.Security;

import net.sf.json.JSONObject;

/**
 * 教程地址：https://blog.csdn.net/qq_36571139/article/details/78206843
 * 获取用户信息
 * @author Administrator
 *
 */
public class GetOpenid {
	
	/**
	 * 获取session_key
	 * openid不需要获取，encryptedData解密后包含openid
	 * @param appid
	 * @param secret
	 * @param js_code
	 * @return
	 */
	public String getOpenid(String appid,String secret,String js_code){
		String requesturl = "https://api.weixin.qq.com/sns/jscode2session?Appid="+appid+
					"&secret="+secret+"&js_code="+js_code+"&grant_type=authorization_code";
		HttpURLConnection conn = null;
		BufferedReader br = null;
		InputStream is = null;
		try {
			URL url = new URL(requesturl);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			is = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			JSONObject json = new JSONObject();
			JSONObject jsonObj = json.fromObject(sb.toString());
			String session_key = jsonObj.getString("session_key");
			return session_key;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public JSONObject getUserInfo(String encryptedData,String sessionkey,String iv){
		//被加密的数据
		byte[] dataByte = Base64.decode(encryptedData);
		//加密的密匙
		byte[] keyByte = Base64.decode(sessionkey);
		//偏移量
		byte[] ivByte = Base64.decode(iv);
		int base = 16;
		// 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
		if (keyByte.length % 16 != 0){
			int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
			byte[] temp = new byte[groups*base];
			Arrays.fill(temp, (byte)0);
			System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
			keyByte = temp;
		}
		//初始化
		java.security.Security.addProvider(new BouncyCastleProvider());
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters paramters = AlgorithmParameters.getInstance("AES");
			paramters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, paramters);//初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length >0 ){
				String result = new String(resultByte, "utf-8");
				return JSONObject.fromObject(result);
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidParameterSpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
}




















