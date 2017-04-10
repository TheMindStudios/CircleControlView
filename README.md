# CircleControlView

Ready for usage CircleControlView based on gesture detection

//GIF

## Features

  - [x] get value based on min, max values and rotation angle
  - [x] get count of full circles (2*PI)
  - [x] get rotation direction (clockwise/counter clockwise)

##Usage

  1. Import `circlecontrolview` module to your project

 //SCREENSHOT
 
  2. Add following line to your `settings.gradle` file

  ```groovy
    include ':YOUR_APP_PROJECT_NAME', ':circlecontrolview'
  ```
  3. Add following line to your `build.gradle` file

  ```groovy
    dependencies {
    ...
    compile project(':circlecontrolview')
}
  ```
  3. Initialize `CircleControlView` from code

  ```java
    final CircleControlView circleControlView = (CircleControlView) findViewById(R.id.radio_cv_fm);
    circleControlView.setOnValueChangedCallback(onValueChangedCallback);

    final Drawable pressedBackground = ContextCompat.getDrawable(this, R.drawable.bg_btn_radio_pressed);
    circleControlView.setPressedBackground(pressedBackground);

    final Properties properties = CircleControlView.newPropertiesBuilder()
            .minValue(700)
            .value(1000)
            .maxValue(1200)
            .numberOfCircles(2)
            .build();

    circleControlView.setProperties(properties);
        
        ...
        
    private CircleControlView.OnValueChangedCallback onValueChangedCallback = new CircleControlView.OnValueChangedCallback() {
        @Override
        public void onValueChanged(int value) {
            ...
            Process your value here
        }
    };
  ```
  
  3. Initialize `CircleControlView` from XML

  ```xml
    <com.themindstudios.circlecontrolview.widget.CircleControlView
        android:id="@+id/circleview"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:background="@drawable/PUT_YOUR_BG_RESOURCE_ID_HERE"
        circle:minValue="0"
        circle:currentValue="333"
        circle:maxValue="360"/>
  ```
  
  ```java
  final CircleControlView circleView = (CircleControlView) findViewById(R.id.circleview);
  circleView.setOnValueChangedCallback(onValueChangedCallback);
  
  ...
  
  private CircleControlView.OnValueChangedCallback onValueChangedCallback = new CircleControlView.OnValueChangedCallback() {

        @Override
        public void onValueChanged(int value) {
            ...
            Process your value here
        }
    };
  ```
  
##License

CorcleControlView is available under the MIT license. See the LICENSE file for more info.
