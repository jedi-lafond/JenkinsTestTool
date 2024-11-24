/**
 * Test Module
 */
module JenkinsTestTool.test {
    requires JenkinsTestTool.main;
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires javafx.controls;
    requires javafx.base;

    exports test;
}