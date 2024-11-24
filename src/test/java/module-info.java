/**
 * Test Module
 */
module JenkinsTestTool.test {
    requires JenkinsTestTool.main;
    requires org.junit.jupiter;
    requires org.mockito;

    requires javafx.controls;

    exports test;
}