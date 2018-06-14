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
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.xfire.util.Base64;

import com.teenyda.bean.SensitiveData;

import net.sf.json.JSONObject;

public class GetUserInfo {
	private static final String Appid = "wx2f7904a87e445037";
	private static final String secret = "4d04ab6b3e59fe18a712388a85942845";

	/**
	 * ��¼΢�ŷ����� ֻ��js_code����
	 * 
	 * @return
	 * 
	 */
	// TODO loginInWXservice
	public static Map<String, String> loginInWXservice(InputStream is) {
		SensitiveData sensitiveData = GetUserInfo.getSensitiveData(is);
		if (sensitiveData.getJs_code() != null) {
			System.out.println("app��js_code" + sensitiveData.getJs_code());
			String path = "https://api.weixin.qq.com/sns/jscode2session?Appid=" + Appid + "&secret=" + secret
					+ "&js_code=" + sensitiveData.getJs_code() + "&grant_type=authorization_code";
			// https://api.weixin.qq.com/sns/jscode2session?Appid=wx2f7904a87e445037&secret=4d04ab6b3e59fe18a712388a85942845&js_code=071gk68n0WWN2s1pZS6n0diS7n0gk68w&grant_type=authorization_code"
			// ��΢�ŷ�������ȡsessionKey
			URL url;
			InputStream inputStream = null;
			System.out.println("LoginInWXService");
			System.out.println("sensitiveData" + sensitiveData);
			try {
				url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				int responseCode = conn.getResponseCode();
				if (responseCode == 200) {
					System.out.println("����΢�ŷ���ɹ�");
					inputStream = conn.getInputStream();
					Map<String, String> infos = getSessionKeyByWXService(inputStream);
					// map����sessioinKey��openId
					return infos;
				} else {
					System.out.println("����΢�ŷ���ʧ��");
					String responseMessage = conn.getResponseMessage();
					System.out.println("responseMessage" + responseMessage);
					return null;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

			}
		} else {
			throw new RuntimeException("��ȡ�û�js_codeʧ��");
		}
		return null;

	}

	/**
	 * ��ȡ��������
	 * 
	 * @param is
	 * @return
	 */
	// TODO getSensitiveData
	public static SensitiveData getSensitiveData(InputStream is) {
		JSONObject fromObject = inputStreamToJson(is);
		System.out.println(fromObject);
		// ��JSONObjectתΪSensitiveData����
		SensitiveData sensitiveData = (SensitiveData) fromObject.toBean(fromObject, SensitiveData.class);
		return sensitiveData;
	}

	// TODO getSessionKey
	/**
	 * ͨ��΢�ŵ���������ȡsessionKey��openid
	 * 
	 * @param is
	 * @return
	 */
	public static Map<String, String> getSessionKeyByWXService(InputStream is) {
		JSONObject fromObject = inputStreamToJson(is);
		System.out.println("ͨ��΢�ŵ���������ȡsessionKey��openid");
		String sessionKey = fromObject.getString("session_key");
		String openid = fromObject.getString("openid");
		System.out.println("΢�ŷ���session_key:" + sessionKey);
		System.out.println("΢�ŷ���openid:" + openid);
		Map<String, String> map = new HashMap<String, String>();
		map.put("sessionKey", sessionKey);
		map.put("openid", openid);
		return map;
	}

	// TODO getDecryptData
	/**
	 * ������������
	 * 
	 * @param encryptedData
	 * @param sessionkey
	 * @param iv
	 * @return
	 */
	public static JSONObject getDecryptData(String encryptedData, String sessionkey, String iv) {
		// �����ܵ�����
		byte[] dataByte = Base64.decode(encryptedData);
		// ���ܵ��ܳ�
		byte[] keyByte = Base64.decode(sessionkey);
		// ƫ����
		byte[] ivByte = Base64.decode(iv);
		int base = 16;
		// �����Կ����16λ����ô�Ͳ���. ���if �е����ݺ���Ҫ
		if (keyByte.length % 16 != 0) {
			int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
			byte[] temp = new byte[groups * base];
			Arrays.fill(temp, (byte) 0);
			System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
			keyByte = temp;
		}
		// ��ʼ��
		java.security.Security.addProvider(new BouncyCastleProvider());
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters paramters = AlgorithmParameters.getInstance("AES");
			paramters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, paramters);// ��ʼ��
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
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
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ�ֻ���sessionKey �� ����Ƿ����
	 * 
	 * @return
	 */
	public static String getAppSessionKey(InputStream is) {
		JSONObject fromObject = inputStreamToJson(is);
		boolean hasSession = fromObject.has("sessionkey");
		if (hasSession) {
			String sessionKey = fromObject.getString("sessionkey");
			return sessionKey;
		}
		return null;
	}

	/**
	 * ��ȡͣ����Ϣ
	 * 
	 * @param is
	 * @return lookid sessionkey js_code
	 */
	public static Map<String, String> getParkingInfo(InputStream is) {
		Map<String, String> map = new HashMap<String, String>();
		JSONObject js = inputStreamToJson(is);
		if (js.has("lookid")) {
			map.put("lookid", js.getString("lookid"));
		}
		if (js.has("sessionKey")) {
			map.put("sessionkey", js.getString("sessionKey"));
		}

		return map;
	}

	/**
	 * ��ȡ����ͣ����Ϣ
	 * 
	 * @param is
	 * @return orderId sessionkey staytime spend
	 */
	public static Map<String, String> getEndParkingInfo(InputStream is) {
		Map<String, String> map = new HashMap<String, String>();
		JSONObject js = inputStreamToJson(is);
		if (js.has("orderId") && js.has("sessionkey") && js.has("spend") && js.has("lookid")) {
			map.put("orderId", js.getString("orderId"));
			map.put("sessionkey", js.getString("sessionkey"));
			map.put("spend", js.getString("spend"));
			map.put("lookid", js.getString("lookid"));
			return map;
		}
		return null;
	}

	public static JSONObject inputStreamToJson(InputStream is) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String json = sb.toString();
			System.out.println(json);
			JSONObject js = new JSONObject();
			JSONObject fromObject = js.fromObject(json);
			return fromObject;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
