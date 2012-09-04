/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.cserver.common.encryption.CipherUtil;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorException;

/**
 * EncryptionUtil
 * 
 * @author kwwang
 * 
 */
public class EncryptionUtil {

	public static Logger logger = Logger.getLogger(EncryptionUtil.class);

	public static String decryptByPtnSource(String unDecryptedString,
			int ptnSource) throws ExecutorException {
		String encryptedString = null;
		if (UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER != ptnSource)
			return unDecryptedString;
		try {
			if (StringUtils.isNotBlank(unDecryptedString))
				encryptedString = CipherUtil.decrypt(URLDecoder.decode(
						unDecryptedString, CipherUtil.STRING_ENCODING));
		} catch (Exception e) {
			String errorMsg = "decryption failed, originString is "
					+ unDecryptedString;
			logger.fatal(errorMsg);
			throw new ExecutorException(errorMsg, e);
		}

		return encryptedString;
	}
}
