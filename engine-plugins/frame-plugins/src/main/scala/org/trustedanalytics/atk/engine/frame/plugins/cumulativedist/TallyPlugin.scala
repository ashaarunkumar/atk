/**
 *  Copyright (c) 2015 Intel Corporation 
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.trustedanalytics.atk.engine.frame.plugins.cumulativedist

import org.trustedanalytics.atk.UnitReturn
import org.trustedanalytics.atk.domain.frame.TallyArgs
import org.trustedanalytics.atk.domain.schema.{ Column, Schema, DataTypes }
import org.trustedanalytics.atk.engine.plugin.{ ApiMaturityTag, ArgDoc, Invocation, PluginDoc }
import org.trustedanalytics.atk.engine.frame.SparkFrame
import org.trustedanalytics.atk.engine.plugin.SparkCommandPlugin
import org.apache.spark.frame.FrameRdd

// Implicits needed for JSON conversion
import spray.json._
import org.trustedanalytics.atk.domain.DomainJsonProtocol._

/**
 * Computes a cumulative count
 *
 */
@PluginDoc(oneLine = "Count number of times a value is seen.",
  extended = """A cumulative count is computed by sequentially stepping through the rows,
observing the column values and keeping track of the number of times the specified
*count_value* has been seen.""")
class TallyPlugin extends SparkCommandPlugin[TallyArgs, UnitReturn] {

  /**
   * The name of the command, e.g. graphs/ml/loopy_belief_propagation
   *
   * The format of the name determines how the plugin gets "installed" in the client layer
   * e.g Python client via code generation.
   */
  override def name: String = "frame/tally"

  override def apiMaturityTag = Some(ApiMaturityTag.Beta)

  /**
   * Computes a cumulative count
   *
   * @param invocation information about the user and the circumstances at the time of the call,
   *                   as well as a function that can be called to produce a SparkContext that
   *                   can be used during this invocation.
   * @param arguments user supplied arguments to running this plugin
   * @return a value of type declared as the Return type.
   */
  override def execute(arguments: TallyArgs)(implicit invocation: Invocation): UnitReturn = {
    val frame: SparkFrame = arguments.frame

    // run the operation
    val cumulativeDistRdd = CumulativeDistFunctions.cumulativeCount(frame.rdd, arguments.sampleCol, arguments.countVal)
    val updatedSchema = frame.schema.addColumnFixName(Column(arguments.sampleCol + "_tally", DataTypes.float64))
    frame.save(new FrameRdd(updatedSchema, cumulativeDistRdd))
  }
}
