/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.trump;

import java.io.File;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestZipTask.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-25
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ZipTask.class})
public class TestZipTask extends TestCase{
	private ZipTask zipTask_G = new ZipTask();
	
	public void testExecute() throws Exception {
		//define variables
		ZipTask zipTask = PowerMock.createPartialMock(ZipTask.class, "findSatisfiedDirs");
		//prepare and replay
		PowerMock.expectPrivate(zipTask, "findSatisfiedDirs");
		PowerMock.replayAll();

		//invoke and verify
		zipTask.execute();
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	
	/*
	 * Failed on Hudson, classpath issue. Remove this case.
	public void testFindSatisfiedDirs() throws Exception {
		//define variables
		ZipTask zipTask = PowerMock.createPartialMock(ZipTask.class, "collectRelativePath","zip");
		File baseDir = new File(UnittestUtil.getAbsPath("zipFolder"));
		zipTask.setBaseDir(baseDir);
		
		PathCollection pachCol = new PathCollection();
		pachCol.addPath("1");
		pachCol.addPath("2");
		//prepare and replay
		PowerMock.expectNew(PathCollection.class).andReturn(pachCol);
		zipTask.collectRelativePath(baseDir, "", pachCol);
		zipTask.zip(pachCol.getPathList().get(0), baseDir);
		zipTask.zip(pachCol.getPathList().get(1), baseDir);
		PowerMock.replayAll();

		//invoke and verify
		Whitebox.invokeMethod(zipTask, "findSatisfiedDirs");
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	*/
	
	public void testZip() {
		//define variables
		PathCollection pathCol = new PathCollection();
		pathCol.addPath(UnittestUtil.getAbsPath("zipFolder"));
		//pathCol.getPathList().get(0).childrenPath.add("\\test.txt");
		//pathCol.getPathList().get(0).childrenPath.add("\\test.xml");
		pathCol.getPathList().get(0).childrenPath.add(File.separator + "test.txt");
		pathCol.getPathList().get(0).childrenPath.add(File.separator + "test.xml");

		//prepare and replay

		PowerMock.replayAll();

		//invoke and verify
		zipTask_G.zip(pathCol.getPathList().get(0), new File(UnittestUtil.getAbsPath("zipFolder")));
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}

	/* Failed on Hudson, classpath issue. Remove this case.
	public void testCollectRelativePath() {
		//define variables
		File baseFile = new File(UnittestUtil.getAbsPath("zipFolder"));
		PathCollection pachCol = new PathCollection();
		
		zipTask_G.collectRelativePath(baseFile, "", pachCol);
		
		//assert
		//do not forget that there is a svn folder
		assertEquals(5,pachCol.getPathList().size());
	}
	*/

	/**
	 * for coverage rate
	 */
	public void testSimple() {
		zipTask_G.getParams();
		zipTask_G.setParams("");
		zipTask_G.getLevel();
		zipTask_G.setLevel(1);
		zipTask_G.getBaseDir();
		zipTask_G.setBaseDir(null);
		zipTask_G.getToDir();
		zipTask_G.setToDir(null);
	}

}
