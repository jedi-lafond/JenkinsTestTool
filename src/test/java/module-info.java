/**
 * Test Module
 */
module JenkinsTestTool.test {
    requires JenkinsTestTool.main;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.mockito;
    requires javafx.controls;
    //Test package
    exports test;
}