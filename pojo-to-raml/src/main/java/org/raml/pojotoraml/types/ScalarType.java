/*
 * Copyright 2013-2017 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.pojotoraml.types;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Map;

public enum ScalarType implements RamlType {
  NUMBER("number", ImmutableList.<Type>of(float.class, Float.class, double.class, Double.class)),
  BOOLEAN(
      "boolean", ImmutableList.<Type>of(boolean.class, Boolean.class)),
  STRING("string",
      ImmutableList.<Type>of(String.class)),
  DATE_ONLY("date-only", ImmutableList.<Type>of()),
  TIME_ONLY(
      "time-only", ImmutableList.<Type>of()),
  DATETIME_ONLY("datetime-only", ImmutableList
      .<Type>of()),
  DATETIME("datetime", ImmutableList.<Type>of()),
  // All "integer" types are mapped to raml's "integer". Not sure if that is correct.
  INTEGER("integer", ImmutableList.<Type>of(int.class, Integer.class, byte.class, Byte.class,
                                            short.class, Short.class, long.class, Long.class, BigInteger.class)),
  NIL("nil", ImmutableList
      .<Type>of());

  private static final Map<Type, RamlType> JAVA_TO_RAML_TYPES;

  static {
    ImmutableMap.Builder<Type, RamlType> builder = ImmutableMap.builder();

    for (ScalarType ramlType : ScalarType.values()) {
      for (Type type : ramlType.correspondingJavaTypes) {
        builder.put(type, ramlType);
      }
    }

    JAVA_TO_RAML_TYPES = builder.build();
  }

  private final String ramlSyntax;
  private final Iterable<Type> correspondingJavaTypes;

  ScalarType(String ramlSyntax, Iterable<Type> correspondingJavaTypes) {
    this.ramlSyntax = ramlSyntax;
    this.correspondingJavaTypes = correspondingJavaTypes;
  }

  @Override
  public String getRamlSyntax() {
    return ramlSyntax;
  }

/*
  @Override
  public boolean isScalar() {
    return true;
  }

  @Override
  public List<Class<?>> consequentTypes() {
    return Collections.emptyList();
  }
*/


  public static Optional<RamlType> fromType(Type type) {
    return Optional.fromNullable(JAVA_TO_RAML_TYPES.get(type));
  }


}
