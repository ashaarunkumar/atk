/*
// Copyright (c) 2015 Intel Corporation 
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
*/
package org.apache.spark.mllib.classification

import org.apache.spark.mllib.linalg.Vectors
import org.trustedanalytics.atk.scoring.interfaces.Model

class NaiveBayesScoringModel(naiveBayesModel: NaiveBayesModel) extends NaiveBayesModel(naiveBayesModel.labels, naiveBayesModel.pi, naiveBayesModel.theta) with Model {

  override def score(data: Seq[Array[String]]): Seq[Any] = {
    var score = Seq[Any]()
    data.foreach { row =>
      {
        val x: Array[Double] = new Array[Double](row.length)
        row.zipWithIndex.foreach {
          case (value: Any, index: Int) => x(index) = value.toDouble
        }
        score = score :+ predict(Vectors.dense(x))
      }
    }
    score
  }

}