# DiaphragmView
Made in [![Appus Studio] (https://github.com/vladimir-yerokhin-appus/DiaphragmView/blob/master/readme_files/appus_logo.png)] (http://appus.pro)

Customizable custom view which looks and works like photocamera diaphragm. 
Can be used, for example, in photoeditos or like animated UI elemet.

* [Customization](#customization)
* [Demo](#demo)
* [Getting Started](#getting-started)
* [Info](#info)

# Customization

* diaphragm_background_color - petals color
* diaphragm_border_color - border color
* diaphragm_border_width - (int value)
* diaphragm_default_opening_value - the value to which the diaphragm will be revealed initially (float value from 0f to 1f)
* diaphragm_size - diaphragm diameter (value in dp)
* diaphragm_petals_count - quantity of diaphragm petals (int value from 3 to ...)


# Demo

![](https://github.com/vladimir-yerokhin-appus/DiaphragmView/blob/master/readme_files/diaphragmViewGif.gif)

# Getting Started

##Setup:

   Just add dependence to main build.gradle:

        dependencies {
                compile 'pro.appus:diaphragmview:1.0.0-alpha'
        }
   
##Usage example:

   Just add view into your layout's .xml.
   
        <pro.appus.diaphragmsample.view.DiaphragmView
        	android:layout_width="wrap_content"
        	android:layout_height="wrap_content"

        	app:diaphragm_background_color="@color/colorAccent"
        	app:diaphragm_border_color="@android:color/white"
        	app:diaphragm_default_opening_value="0.5"
        	app:diaphragm_size="200dp"
        	app:diaphragm_border_width="3"
        	app:diaphragm_petals_count="9"  />

   And also, the DiaphragmView can be controlled from the code:

   * setOpeningValue(float openingValue) - sets diaphragm's opening value, for example, by SeekBar
   * getOpeningValue() - gets current diaphragm's opening value
   * setDiaphragmPetalsCount(int diaphragmPetalsCount) - sets diaphragm's petals number
   * reset() - returns the DiaphragmView to its original state

# Info

## Developed By

[Alexey Kubas](https://github.com/alexey-kubas-appus), Appus Studio

[Vladimir Yerokhin](https://github.com/vladimir-yerokhin-appus), Appus Studio

## License

```
Copyright 2015 Appus Studio.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
