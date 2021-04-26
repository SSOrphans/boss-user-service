package org.ssor.boss.core;

import org.junit.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({
  "org.ssor.boss.core.entity",
  "org.ssor.boss.core.repository",
  "org.ssor.boss.core.service",
  "org.ssor.boss.core.transfer"
})
public class BossCoreTestSuite
{
  @Test
  public void forceTestSuiteToRun()
  { }
}
