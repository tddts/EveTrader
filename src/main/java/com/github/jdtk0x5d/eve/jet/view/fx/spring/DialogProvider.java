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

package com.github.jdtk0x5d.eve.jet.view.fx.spring;

import com.github.jdtk0x5d.eve.jet.config.spring.beans.ResourceBundleContainer;
import com.github.jdtk0x5d.eve.jet.view.fx.config.annotations.FXDialog;
import com.github.jdtk0x5d.eve.jet.view.fx.config.annotations.Init;
import com.github.jdtk0x5d.eve.jet.view.fx.exception.DialogException;
import com.github.jdtk0x5d.eve.jet.view.fx.view.View;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
public class DialogProvider {

  private static final String FXML_FIELD_LOOKUP_PREFIX = "#";
  private static final Object[] EMPTY_ARGS = new Object[]{};

  private Map<Class<?>, Dialog<?>> dialogCache = new HashMap<>();

  @Autowired
  private FxWirer fxWirer;
  @Autowired
  private ResourceBundleContainer resourceBundleContainer;

  @SuppressWarnings("unchecked")
  public <T extends Dialog<?>> T getDialog(Class<T> type) {
    return getDialog(type, EMPTY_ARGS);
  }

  @SuppressWarnings("unchecked")
  public <T extends Dialog<?>> T getDialog(Class<T> type, Object... args) {
    // Try to use cached dialog
    Dialog<?> cachedDialog = getCachedDialog(type, args);
    // Create new dialog
    return cachedDialog == null ? createDialog(type, args) : (T) cachedDialog;
  }

  private Dialog<?> getCachedDialog(Class<?> type, Object... args) {
    Dialog<?> cachedDialog = dialogCache.get(type);
    if (cachedDialog == null) return null;

    try {
      processInitMethod(type, cachedDialog, args);
      return cachedDialog;
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new DialogException("Could not initialize dialog!", e);
    }
  }

  @SuppressWarnings("unchecked")
  private <T extends Dialog<?>> T createDialog(Class<T> type, Object[] args) {
    Constructor<?>[] constructors = type.getConstructors();
    if (constructors.length == 0) {
      throw new DialogException("Dialog should have a public constructor!");
    }
    if (constructors.length > 1) {
      throw new DialogException("Dialog should have only one constructor!");
    }
    if (constructors[0].getParameterCount() > 0) {
      throw new DialogException("Dialog constructor should have no parameters!");
    }
    try {

      T dialog = (T) constructors[0].newInstance();
      wireDialog(type, dialog);
      processInitMethod(type, dialog, args);
      dialogCache.put(type, dialog);
      return dialog;

    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new DialogException("Could not create dialog!", e);
    }
  }

  private void wireDialog(Class<?> type, Dialog<?> dialog) throws IllegalAccessException, InvocationTargetException {
    wireFXML(type, dialog);
    fxWirer.initBean(dialog);
  }

  private void processInitMethod(Class<?> type, Dialog<?> dialog, Object[] args) throws InvocationTargetException, IllegalAccessException {
    for (Method method : type.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Init.class)) {
        method.setAccessible(true);
        method.invoke(dialog, args);
      }
    }
  }

  private void wireFXML(Class<?> type, Dialog<?> dialog) throws IllegalAccessException {

    if (!type.isAnnotationPresent(FXDialog.class)) {
      throw new DialogException("Dialog class should have a @FXDialog annotation!");
    }
    FXDialog dialogAnnotation = type.getDeclaredAnnotation(FXDialog.class);

    View<?> view = loadDialogView(dialogAnnotation);
    Node root = view.getRoot();
    setDialogContent(dialog, root, dialogAnnotation);

    for (Field field : type.getDeclaredFields()) {
      if (field.isAnnotationPresent(FXML.class)) {
        field.setAccessible(true);
        field.set(dialog, root.lookup(FXML_FIELD_LOOKUP_PREFIX + field.getName()));
      }
    }
  }

  private void setDialogContent(Dialog<?> dialog, Node root, FXDialog dialogAnnotation) {
    boolean expandable = dialogAnnotation.expandable();
    if(expandable){
      dialog.getDialogPane().setExpandableContent(root);
    }else {
      dialog.getDialogPane().setContent(root);
    }
  }


  private View<?> loadDialogView(FXDialog dialogAnnotation) {

    String filePath = dialogAnnotation.value();

    if (filePath.isEmpty()) {
      throw new DialogException("@FXDialog should contain path to FXML file!");
    }

    return new View<>(filePath, resourceBundleContainer.getResourceBundle());
  }
}
