package com.tsystems.testsuites;

import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({"com.tsystems.parser.email.model"})
@DisplayName("ModelPakageTestSuite")
public class ModelPakageTestSuite {
}
