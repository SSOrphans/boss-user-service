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
@SelectPackages({ "org.ssor.boss.user.controller", "org.ssor.boss.user.service", "org.ssor.boss.user.exception" })
public class UpdateUserTestSuite {

}
