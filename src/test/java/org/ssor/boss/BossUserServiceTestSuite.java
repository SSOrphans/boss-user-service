package org.ssor.boss;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({
                  "org.ssor.boss.controller",
                  "org.ssor.boss.dto",
                  "org.ssor.boss.entity",
                  "org.ssor.boss.repository",
                  "org.ssor.boss.service"
                })
public class BossUserServiceTestSuite
{
}
