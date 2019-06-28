package com.sc.web.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.util.ResourceUtils;

import com.github.wxpay.sdk.WXPayConfig;

public class WXPayConfigImpl implements WXPayConfig {

	private String appID;

	private String appSecret;

	private String mchID;

	private String mchKey;

	private byte[] certData;

	public WXPayConfigImpl(String appID, String appSecret, String mchID, String mchKey, String certPath) {
		this.appID = appID;
		this.appSecret = appSecret;
		this.mchID = mchID;
		this.mchKey = mchKey;
		try {
			File file = ResourceUtils.getFile(certPath);
			InputStream certStream = new FileInputStream(file);
			this.certData = new byte[(int) file.length()];
			certStream.read(this.certData);
			certStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getAppID() {
		return appID;
	}

	@Override
	public String getMchID() {
		return mchID;
	}

	@Override
	public String getKey() {
		return mchKey;
	}

	@Override
	public InputStream getCertStream() {
		// TODO Auto-generated method stub
		ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	@Override
	public int getHttpConnectTimeoutMs() {
		// TODO Auto-generated method stub
		return 60000;
	}

	@Override
	public int getHttpReadTimeoutMs() {
		// TODO Auto-generated method stub
		return 60000;
	}

	public String getAppSecret() {
		return appSecret;
	}

}
