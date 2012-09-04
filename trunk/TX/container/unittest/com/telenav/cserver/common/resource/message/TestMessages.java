/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

/**
 * MessagesTest
 * 
 * @author kwwang
 * 
 */
public class TestMessages {
	public static enum LoginEnum {
		ACCOUNT_NOT_FOUND, NEW_ACCOUNT, COMMON_EXIPRE, TRIAL_EXPIRE, PIN_MISMATCH, UNKNOWN_STATUS
	};

	@Test
	public void getByEnum() {
		Map map = new HashMap();
		map.put("account_not_found", "account_not_found");
		Messages msgs = new Messages(map);

		Assert.assertEquals("account_not_found",
				msgs.getBy(LoginEnum.ACCOUNT_NOT_FOUND));
	}

	@Test
	public void getByEnumWithPrefix() {
		Map map = new HashMap();
		map.put("login.account_not_found", "account_not_found");
		Messages msgs = new Messages(map);

		Assert.assertEquals("account_not_found",
				msgs.getByEnumWithPrefix(LoginEnum.ACCOUNT_NOT_FOUND, "login"));
	}

	@Test
	public void getBatchStringWithPrefix() {
		Map map = new HashMap();
		map.put("login.account_not_found1", "account_not_found1");
		map.put("login.account_not_found2", "account_not_found2");
		Messages msgs = new Messages(map);

		Assert.assertEquals(2, msgs.getBatchStringWithPrefix("login").keySet()
				.size());
	}

	@Test
	public void isListKey() {
		Map map = new HashMap();
		Messages msgs = new Messages(map);

		Assert.assertTrue(msgs.isListKey("login.account_not_found.1"));
		Assert.assertFalse(msgs.isListKey("login.account_not_found.1x"));
		Assert.assertFalse(msgs.isListKey("login.account_not_found."));
		Assert.assertFalse(msgs.isListKey("login.account_not_found.mrc"));
		Assert.assertFalse(msgs.isListKey(""));
		Assert.assertFalse(msgs.isListKey(null));
	}

	@Test
	public void removeSuffixedNumFrom() {
		Map map = new HashMap();
		Messages msgs = new Messages(map);

		Assert.assertEquals("login.account_not_found",
				msgs.removeSuffixedNumFrom("login.account_not_found.1"));
		Assert.assertEquals("",
				msgs.removeSuffixedNumFrom("login.account_not_found."));
		Assert.assertEquals("",
				msgs.removeSuffixedNumFrom("login.account_not_found"));
	}

	@Test
	public void getBatchStringWithPrefixAndKeyIsTheSame() {

		// test non-override version
		Map map = new HashMap();
		map.put("login.account_not_found.option.1", "account_not_found1");
		map.put("login.account_not_found.option.2", "account_not_found2");
		Messages msgs = new Messages(map);

		Map returnMap = msgs
				.getBatchStringWithPrefix("login.account_not_found");
		List list = (List) returnMap.get("option");

		Assert.assertTrue(list instanceof List);
		Assert.assertTrue(2 == list.size());

		// test overrite version, if the prefix of listkey and prefix of
		// non-listkey is the same, then list will come first
		map.put("login.account_not_found.option", "non-list-value");
		map.put("login.account_not_found.option.3", "account_not_found3");
		returnMap = msgs.getBatchStringWithPrefix("login.account_not_found");
		list = (List) returnMap.get("option");
		Assert.assertTrue(list instanceof List);
		Assert.assertTrue(3 == list.size());
	}

	@Test
	public void newPersonWithMessageGetBatchStringWithPrefix() throws Exception {
		Map map = new HashMap();
		map.put("login.names.1", "account_not_found1");
		map.put("login.names.2", "account_not_found2");
		map.put("login.age", 3);
		Messages msgs = new Messages(map);

		Person p = new Person();

		BeanUtils.populate(p, msgs.getBatchStringWithPrefix("login"));

		Assert.assertEquals(3, p.getAge());
		Assert.assertEquals(2, p.getNames().size());
	}

	public void testSimple() {
		Map map = new HashMap();
		Messages msgs = new Messages(map);

		msgs.put("login.account_not_found1", "account_not_found1");
		msgs.get("");
		msgs.toString();

	}

}
