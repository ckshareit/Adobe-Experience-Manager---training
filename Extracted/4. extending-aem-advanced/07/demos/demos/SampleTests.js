/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
hobs.param("enPage", "/content/trainingproject/en.html")
new hobs.TestSuite("TrainingProject Tests", {path:"/apps/trainingproject/tests/hobbestests/SampleTests.js", register: true})
    .addTestCase(new hobs.TestCase("Hello World component on english page")
        .navigateTo("%enPage%")
        .asserts.location("%enPage%", true)
        .asserts.visible(".helloworld", true)
    )
 
    .addTestCase(new hobs.TestCase("Hello World component on french page")
        .navigateTo("/content/trainingproject/fr.html")
        .asserts.location("/content/trainingproject/fr.html", true)
        .asserts.visible(".helloworld", true)
    )
    .addTestCase(new hobs.TestCase("Text and Image Component on English Page")
    .navigateTo("%enPage%")
    .asserts.location("%enPage%", true)
    .asserts.exists("div.textimage.parbase", true)
    );
