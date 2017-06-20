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

package com.github.jdtk0x5d.eve.jet.view.fx.tools.message.provider;

import com.github.jdtk0x5d.eve.jet.view.fx.tools.message.EnumMessageStringConverter;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
@Component
public class MessageHelperImpl implements MessageHelper {

  private static final Object[] NO_ARGS = new Object[]{};

  @Autowired
  private MessageSource messageSource;

  @Override
  public String getMessage(Enum object) {
    return messageSource.getMessage(
        object.getClass().getName() + "." + object.name(),
        NO_ARGS,
        Locale.getDefault());
  }

  @Override
  public <T extends Enum<T>> StringConverter<T> getConverter(T[] values) {
    return new EnumMessageStringConverter(this,values);
  }
}