/*
 * Copyright 2017 Tigran Dadaiants
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jdtk0x5d.eve.jet.view.fx.dialog;

import com.github.jdtk0x5d.eve.jet.config.spring.annotations.Message;
import com.github.jdtk0x5d.eve.jet.view.fx.config.annotations.FXDialog;
import com.github.jdtk0x5d.eve.jet.view.fx.config.annotations.Init;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import javax.annotation.PostConstruct;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
@FXDialog(value = "fxml/dialog-exception.fxml", expandable = true)
public class ExceptionDialog extends Alert {

  @FXML
  private TextArea textArea;

  @Message("dialog.exception.title")
  private String title;
  @Message("dialog.exception.header")
  private String headerText;

  public ExceptionDialog() {
    super(AlertType.ERROR);
  }

  @Init
  private void init(Throwable ex) {
    setTitle(title);
    setHeaderText(headerText);
    setContentText(ex.getLocalizedMessage());

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    pw.flush();
    pw.close();

    textArea.setText(sw.toString());
  }
}
