/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.erna.scripting.scala.bundlefs

import org.osgi.framework.Bundle

import scala.tools.nsc.io.AbstractFile

/**
 * Implementation of {@link AbstractFile} on top of a {@link org.osgi.framework.Bundle}
 */
object BundleFS {

  /**
   * Create a new {@link AbstractFile} instance representing an
   * {@link org.osgi.framework.Bundle}
   * @param bundle
   */
  def create(bundle: Bundle): AbstractFile = {
    require(bundle != null, "bundle must not be null")

    new DirEntry(bundle, bundle.getResource("/"), null)
  }

}
