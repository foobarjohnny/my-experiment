/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * TestFileUtils.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-25
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class})
public class TestFileUtils extends TestCase{
	private FileUtils fileUtils = new FileUtils();//for coverage rate
	/**
	 * dir.exists()  false
	 */
	public void testDelDir1(){
		//define variables
		File dir = PowerMock.createMock(File.class);
		//prepare and replay
		EasyMock.expect(dir.exists()).andReturn(false);
		EasyMock.expect(dir.getAbsolutePath()).andReturn("test path");
		PowerMock.replayAll();

		//invoke and verify
		FileUtils.delDir(dir);
		PowerMock.verifyAll();
	}
	/**
	 * dir.isDirectory()  false
	 */
	public void testDelDir2(){
		//define variables
		File dir = PowerMock.createMock(File.class);
		//prepare and replay
		EasyMock.expect(dir.exists()).andReturn(true);
		EasyMock.expect(dir.isDirectory()).andReturn(false);
		EasyMock.expect(dir.getAbsolutePath()).andReturn("test path");
		PowerMock.replayAll();

		//invoke and verify
		FileUtils.delDir(dir);
		PowerMock.verifyAll();
	}
	public void testDelDir_fail() {
		//define variables
		PowerMock.mockStaticPartial(FileUtils.class, "delFile");
		File dir = PowerMock.createMock(File.class);
		
		File[] files = new File[2];
		File file0 = PowerMock.createMock(File.class);
		File file1 = PowerMock.createMock(File.class);
		files[0] = file0;
		files[1] = file1;
		
		//prepare and replay
		EasyMock.expect(dir.exists()).andReturn(true);
		EasyMock.expect(dir.isDirectory()).andReturn(true);
		EasyMock.expect(dir.listFiles()).andReturn(files);
		FileUtils.delFile(file0);
		FileUtils.delFile(file1);
		EasyMock.expect(dir.delete()).andReturn(false);
		EasyMock.expect(dir.getAbsolutePath()).andReturn("test path");
		PowerMock.replayAll();

		//invoke and verify
		FileUtils.delDir(dir);
		PowerMock.verifyAll();

		//no Exception is OK.
	}
	public void testDelDir() {
		//define variables
		PowerMock.mockStaticPartial(FileUtils.class, "delFile");
		File dir = PowerMock.createMock(File.class);
		
		File[] files = new File[2];
		File file0 = PowerMock.createMock(File.class);
		File file1 = PowerMock.createMock(File.class);
		files[0] = file0;
		files[1] = file1;
		
		//prepare and replay
		EasyMock.expect(dir.exists()).andReturn(true);
		EasyMock.expect(dir.isDirectory()).andReturn(true);
		EasyMock.expect(dir.listFiles()).andReturn(files);
		FileUtils.delFile(file0);
		FileUtils.delFile(file1);
		EasyMock.expect(dir.delete()).andReturn(true);
		PowerMock.replayAll();

		//invoke and verify
		FileUtils.delDir(dir);
		PowerMock.verifyAll();

		//no Exception is OK.
	}
	
	public void testDelFile_fileNotExists(){
		//define variables
		File file = PowerMock.createMock(File.class);
		
		//prepare and replay
		EasyMock.expect(file.exists()).andReturn(false);
		EasyMock.expect(file.getAbsolutePath()).andReturn("test path");
		PowerMock.replayAll();

		//invoke and verify
		FileUtils.delFile(file);
		PowerMock.verifyAll();

		//no Exception is OK.
	}
	public void testDelFile_fileIsDirectory(){
		//define variables
		PowerMock.mockStaticPartial(FileUtils.class, "delDir");
		File file = PowerMock.createMock(File.class);
		
		//prepare and replay
		EasyMock.expect(file.exists()).andReturn(true);
		EasyMock.expect(file.isDirectory()).andReturn(true);
		FileUtils.delDir(file);
		PowerMock.replayAll();

		//invoke and verify
		FileUtils.delFile(file);
		PowerMock.verifyAll();

		//no Exception is OK.
	}
	public void testDelFile_fail(){
		//define variables
		PowerMock.mockStaticPartial(FileUtils.class, "delDir");
		File file = PowerMock.createMock(File.class);
		
		//prepare and replay
		EasyMock.expect(file.exists()).andReturn(true);
		EasyMock.expect(file.isDirectory()).andReturn(false);
		EasyMock.expect(file.delete()).andReturn(false);
		EasyMock.expect(file.getAbsolutePath()).andReturn("test path");
		PowerMock.replayAll();

		//invoke and verify
		FileUtils.delFile(file);
		PowerMock.verifyAll();

		//no Exception is OK.
	}
	
	public void testDelFile(){
		//define variables
		PowerMock.mockStaticPartial(FileUtils.class, "delDir");
		File file = PowerMock.createMock(File.class);
		
		//prepare and replay
		EasyMock.expect(file.exists()).andReturn(true);
		EasyMock.expect(file.isDirectory()).andReturn(false);
		EasyMock.expect(file.delete()).andReturn(true);
		PowerMock.replayAll();

		//invoke and verify
		FileUtils.delFile(file);
		PowerMock.verifyAll();

		//no Exception is OK.
	}
	
	public void testDelFiles_filesIsNull(){
		FileUtils.delFiles(null);
		//no Exception is OK.
	}
	
	public void testDelFiles(){
		//define variables
		PowerMock.mockStaticPartial(FileUtils.class, "delFile");
		File file = PowerMock.createMock(File.class);
		
		List<File> list = new ArrayList<File>();
		File file0 = PowerMock.createMock(File.class);
		File file1 = PowerMock.createMock(File.class);
		list.add(file0);
		list.add(file1);
		
		//prepare and replay
		FileUtils.delFile(file0);
		FileUtils.delFile(file1);
		PowerMock.replayAll();

		//invoke and verify
		FileUtils.delFiles(list);
		PowerMock.verifyAll();

		//no Exception is OK.
	}

}
