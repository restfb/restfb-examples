module com.restfb.example {
  // Java FX modules
  requires javafx.web;
  requires javafx.controls;
  requires javafx.fxml;
  // logging modules
  requires java.logging;
  requires jul.to.slf4j;
  // other modules
  requires restfb;
  requires java.desktop;
  requires logback.classic;

  exports com.restfb.example;
}