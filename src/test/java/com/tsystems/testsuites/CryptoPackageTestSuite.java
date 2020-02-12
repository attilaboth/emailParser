package com.tsystems.testsuites;

import com.tsystems.monitoring.crypto.CryptoEngineTest2;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
//@SelectPackages({"com.tsystems.monitoring.crypto"})
@SelectClasses({CryptoEngineTest2.class})
@DisplayName("CryptoEngineTest2")
public class CryptoPackageTestSuite {
}
