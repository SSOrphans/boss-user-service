/**
 * 
 */
package org.ssor.boss.user.testsuite;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

/**
 * @author Christian Angeles
 *
 */
@RunWith(JUnitPlatform.class)
@SelectPackages({ "org.ssor.boss.user.retrieveInfo.controller", "org.ssor.boss.user.retrieveInfo.dto",
		"org.ssor.boss.user.retrieveInfo.entity", "org.ssor.boss.user.retrieveInfo.dao",
		"org.ssor.boss.user.retrieveInfo.service" })
public class RetrieveUserInfoTestSuite {

}
