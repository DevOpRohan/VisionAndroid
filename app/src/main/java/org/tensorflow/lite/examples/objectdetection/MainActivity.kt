/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.objectdetection

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.objectdetection.databinding.ActivityMainBinding
//
///**
// * Main entry point into our app. This app follows the single-activity pattern, and all
// * functionality is implemented in the form of fragments.
// */
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var activityMainBinding: ActivityMainBinding
//
//    companion object{
//        var item:String=""
//
//    }
//
////    val objectViewmodel by viewModels<ObjectViewmodel>()
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(activityMainBinding.root)
//
//        item = intent?.getStringExtra("object").toString()
//
//        Toast.makeText(this,"Finding $item",Toast.LENGTH_LONG).show()
//
//        Handler().postDelayed({
//
//            ObjectViewmodel.objectResult="$item not found"
//
//            finish()
//        },20000)
//
//    }
//
//    override fun onBackPressed() {
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
//            // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
//            // (https://issuetracker.google.com/issues/139738913)
//            finishAfterTransition()
//        } else {
//            super.onBackPressed()
//        }
//    }
//}
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    companion object {
        var item: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        item = intent?.getStringExtra("object").toString()

        Toast.makeText(this, "Finding $item", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
            // (https://issuetracker.google.com/issues/139738913)
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }
}