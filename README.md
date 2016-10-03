# WaterMillProgressView
Water Mill Progress View

<img src="http://thedeveloperworldisyours.com/wp-content/uploads/videoBlue.gif"  width="300px" />
<img src="http://thedeveloperworldisyours.com/wp-content/uploads/dialogWaterMill.gif"  width="300px" />


# How to use
````xml
    <com.thedeveloperworldisyours.cabezas.WaterMillView
        android:layout_centerInParent="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:mill_color="#039BE5"
        app:wave_color="#01579B"
        app:text_color="#E1F5FE"
        app:background_color="#B3E5FC"
        app:loading_text="@string/loading"
        app:water_level="40"/>
````
or you can just use(default mill_color=Color.BLACK, wave_color="#ff33b5e5", text_color=Color.WHITE, background_color="#E3F2FD", loading_text="loading...", water_level="50")
````xml
    <com.thedeveloperworldisyours.cabezas.WaterMillView
        android:layout_centerInParent="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />
````

or you can use how a Dialog, in this case you can changes the string, in res/values/string.xml you should add
````xml
<resources>
    <string name="loading">Cargando...</string>
</resources>
````

the colors are (default mill_color=Color.BLACK, wave_color="#ff33b5e5", text_color=Color.WHITE, background_color="#E3F2FD", water_level="50")

```java  
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.activity_main_my_button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog();
                }
            });
        }
    }

    public void openDialog() {
        WaterMillDialog waterMillDialog = new WaterMillDialog();
        waterMillDialog.show(getSupportFragmentManager(), "");
    }
}
```
    

# Usage
    Compile with Android Studio and gradle


Feel free to contribute, and contact me for any issues.

Developed By
------------
* Javier González Cabezas - <javiergonzalezcabezas@gmail.com>

<a href="https://es.linkedin.com/in/javier-gonz%C3%A1lez-cabezas-8b4b2231">
  <img alt="Add me to Linkedin" src="https://github.com/JorgeCastilloPrz/EasyMVP/blob/master/art/linkedin.png" />
</a>

License
-------

    Copyright 2016 Javier González Cabezas

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
