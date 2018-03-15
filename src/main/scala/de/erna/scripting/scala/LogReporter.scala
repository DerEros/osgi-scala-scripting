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
package de.erna.scripting.scala

import org.slf4j.Logger

import scala.reflect.internal.util.Position
import scala.tools.nsc.Settings

class LogReporter(logger: Logger, settings: Settings) extends BacklogReporter(settings) {

  override def display(pos: Position, msg: String, severity: Severity) {
    super.display(pos, msg, severity)
    
    severity match {
      case INFO => logger.info(s"$msg: $pos")
      case WARNING => logger.warn(s"$msg: $pos")
      case _ => logger.error(s"$msg: $pos")
    }
  }
  
}