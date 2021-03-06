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

package org.trustedanalytics.atk.scoring.models

import org.apache.spark.mllib.ScoringModelTestUtils
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.regression.LinearRegressionModel
import org.scalatest.WordSpec

class LinearRegressionModelTest extends WordSpec {
  val weights = new DenseVector(Array(2, 3))
  val intercept = 4
  val linearRegressionModel = new LinearRegressionModel(weights, intercept)
  var linearRegressionScoreModel = new LinearRegressionScoreModel(linearRegressionModel)
  val numRows = 5 // number of rows of data to test with

  "LinearRegressionModel" should {
    "throw an exception when attempting to score null data" in {
      ScoringModelTestUtils.nullDataTest(linearRegressionScoreModel)
    }

    "throw an exception when scoring data with too few columns" in {
      ScoringModelTestUtils.tooFewDataColumnsTest(linearRegressionScoreModel, weights.size, numRows)
    }

    "throw an exception when scoring data with too many columns" in {
      ScoringModelTestUtils.tooManyDataColumnsTest(linearRegressionScoreModel, weights.size, numRows)
    }

    "throw an exception when scoring data with non-numerical records" in {
      ScoringModelTestUtils.invalidDataTest(linearRegressionScoreModel, weights.size)
    }

    "successfully score a model when float data is provided" in {
      ScoringModelTestUtils.successfulModelScoringFloatTest(linearRegressionScoreModel, weights.size, numRows)
    }

    "successfully score a model when integer data is provided" in {
      ScoringModelTestUtils.successfulModelScoringFloatTest(linearRegressionScoreModel, weights.size, numRows)
    }

  }
}
