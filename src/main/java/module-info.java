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

  exports com.restfb.example;
}