# DelayedActionView
A view that shows a visual progress count down before triggering the action

![alt tag](http://i.imgur.com/LAIrCa2.png)

## Installation

Clone the repository in your desktop

##Usage 
### XML

Add view to your layout

```XML
   <com.ablanco.library.DelayedActionView
        android:id="@+id/delayed"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
```


### Customization in XML

 ```XML
    <declare-styleable name="DelayedActionView">
        <!--Icon to show as start view-->
        <attr name="davDrawable" format="reference"/>
        <!--Delay time to trigger action-->
        <attr name="davDelay" format="integer"/>
        <!--Color of the progress count down. Uses colorAccent as default-->
        <attr name="davProgressColor" format="color"/>
        <!--Background color of the view. Uses colorPrimaryDark as default-->
        <attr name="davBackGroundColor" format="color"/>
        <!--Tint color of the action icon view. Uses colorPrimary as default-->
        <attr name="davDrawableTintColor" format="color"/>
    </declare-styleable>
```

### Customization in code

 ```java
        DelayedActionView delayedActionView = () findViewById(R.id.delayed);
        delayedActionView.setIconDrawable(R.drawable.ic_dismiss);
        delayedActionView.setIconBitmap(mIconBitmap);
        delayedActionView.setDelayTime(2500);
        delayedActionView.setProgressColor(mProgressColor);
        delayedActionView.setBackgroundColor(mBackgroundColor);
        delayedActionView.setIconTintColor(mIconTintColor);
```

##Usage

Count down will start when you call start

```java
   delayedActionView.start(new DelayedActionView.ActionListener() {
            @Override
            public void onAction() {
                //count down completed, do your stuff
            }

            @Override
            public void onCanceled() {
                //user canceld count down, hide view or do what you want
            }
        });
```

## License
```
    MIT License
```
       
