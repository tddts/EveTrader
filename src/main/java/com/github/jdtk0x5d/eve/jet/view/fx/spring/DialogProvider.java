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

import com.github.jdtk0x5d.eve.jet.config.spring.beans.ResourceBundleProvider;
import com.github.jdtk0x5d.eve.jet.view.fx.annotations.FXDialog;
import com.github.jdtk0x5d.eve.jet.view.fx.annotations.InitDialog;
import com.github.jdtk0x5d.eve.jet.view.fx.exception.DialogException;
import com.github.jdtk0x5d.eve.jet.view.fx.view.View;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.misc.MethodUtil;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code DialogProvider} creates {@link Dialog} objects by processing classes with special annotations.
 * {@code DialogProvider} is capable of injecting dialog with FXML nodes similar to JavaFX controllers,
 * and also capable processing Dialog as a Spring bean (including injection of dependencies).
 * <p>
 * To create a Dialog via {@code DialogProvider} you should mark corresponding Dialog implementation with
 * {@link FXDialog} annotation and describe path to FXML file with dialog content.
 * FXMl file should have {@code fx:controller} property set to dialog class.
 * <p>
 * To initialize dialog crate a method with required parameters and mark by {@link InitDialog} annotation.
 * This initialization method will be invoked every time this dialog is called.
 * <p>
 * Such dialog would also support {@link PostConstruct} annotation as a Spring-processed bean.
 *
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
public class DialogProvider {

  private static final Object[] EMPTY_ARGS = new Object[]{};

  private Map<Class<?>, Dialog<?>> dialogCache = new HashMap<>();
  private Map<Class<?>, List<Method>> dialogInitCache = new HashMap<>();

  @Autowired
  private FxWirer fxWirer;
  @Autowired
  private ResourceBundleProvider resourceBundleProvider;

  /**
   * Create dialog of given type.
   *
   * @param type dialog class
   * @param <T>  dialog generic type
   * @return dialog of given type.
   */
  @SuppressWarnings("unchecked")
  public <T extends Dialog<?>> T getDialog(Class<T> type) {
    return getDialog(type, EMPTY_ARGS);
  }

  /**
   * Create dialog of given type using given arguments.
   *
   * @param type dialog class
   * @param <T>  dialog generic type
   * @param args dialog arguments
   * @return dialog of given type.
   */
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
    try {

      if (!type.isAnnotationPresent(FXDialog.class)) {
        throw new DialogException("Dialog class should have a @FXDialog annotation!");
      }
      FXDialog dialogAnnotation = type.getDeclaredAnnotation(FXDialog.class);

      View<T> view = loadDialogView(dialogAnnotation);
      T dialog = view.getController();
      setDialogContent(dialog, view.getRoot(), dialogAnnotation);
      fxWirer.initBean(dialog);
      processInitMethod(type, dialog, args);
      dialogCache.put(type, dialog);
      return dialog;

    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new DialogException("Could not create dialog!", e);
    }
  }

  private void processInitMethod(Class<?> type, Dialog<?> dialog, Object[] args) throws InvocationTargetException, IllegalAccessException {

    List<Method> initMethods = dialogInitCache.get(type);

    // Add dialog init methods to cache
    if (initMethods == null) {
      initMethods = new ArrayList<>();
      for (Method method : type.getDeclaredMethods()) {
        if (method.isAnnotationPresent(InitDialog.class)) initMethods.add(method);
      }
      dialogInitCache.put(type, initMethods);
    }

    // Invoke init methods
    for (Method method : initMethods) {
      method.setAccessible(true);
      if (method.getParameterCount() == 0) {
        MethodUtil.invoke(method, dialog, EMPTY_ARGS);
      }
      else {
        MethodUtil.invoke(method, dialog, args);
      }
    }
  }

  private void setDialogContent(Dialog<?> dialog, Node root, FXDialog dialogAnnotation) {
    boolean expandable = dialogAnnotation.expandable();
    if (expandable) {
      dialog.getDialogPane().setExpandableContent(root);
    }
    else {
      dialog.getDialogPane().setContent(root);
    }
  }

  private <T> View<T> loadDialogView(FXDialog dialogAnnotation) {

    String filePath = dialogAnnotation.value();

    if (filePath.isEmpty()) {
      throw new DialogException("@FXDialog should contain path to FXML file!");
    }

    return new View<>(filePath, resourceBundleProvider.getResourceBundle());
  }
}
