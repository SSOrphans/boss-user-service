package org.ssor.boss.user;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({
                  "org.ssor.boss.user.controller",
                  "org.ssor.boss.user.dto",
                  "org.ssor.boss.user.entity",
                  "org.ssor.boss.user.repository",
                  "org.ssor.boss.user.security",
                  "org.ssor.boss.user.service",
                  "org.ssor.boss.core.configuration",
                  "org.ssor.boss.core.entity",
                  "org.ssor.boss.core.repository"
                })
public class BossUserServiceTestSuite
{
}
