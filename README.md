# DelayedDismissView
A view that shows a visual progress count down

![alt tag](http://i.imgur.com/1wIMkc6.png)

## Installation

Clone the repository in your desktop

##Usage 
### XML

Add view to your layout

```XML
   <com.ablanco.library.DelayedDismissView
        android:id="@+id/delayed"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
```


### Customization in XML

 ```XML
    <declare-styleable name="DelayedDismissView">
        <!--Icon to show as dismiss view-->
        <attr name="ddvDrawable" format="reference"/>
        <!--Delay time to trigger action-->
        <attr name="ddvDelay" format="integer"/>
        <!--Color of the progress count down. Uses colorAccent as default-->
        <attr name="ddvProgressColor" format="color"/>
        <!--Background color of the view. Uses colorPrimaryDark as default-->
        <attr name="ddvBackGroundColor" format="color"/>
        <!--Tint color of the dismiss icon view. Uses colorPrimary as default-->
        <attr name="ddvDrawableTintColor" format="color"/>
    </declare-styleable>
```

### Customization in code

 ```java
        DelayedDismissView delayedDismissView = (DelayedDismissView) findViewById(R.id.delayed);
        delayedDismissView.setIconDrawable(R.drawable.ic_dismiss);
        delayedDismissView.setIconBitmap(mIconBitmap);
        delayedDismissView.setDelayTime(2500);
        delayedDismissView.setProgressColor(mProgressColor);
        delayedDismissView.setBackgroundColor(mBackgroundColor);
        delayedDismissView.setIconTintColor(mIconTintColor);
```

##Usage

Count down will start when you call dismiss

```java
   delayedDismissView.dismiss(new DelayedDismissView.DismissListener() {
            @Override
            public void onDismissed() {
                //count down completed, do stuff
            }

            @Override
            public void onCanceled() {
                //user canceled count down, hide view or do what you want
            }
        });
```

## License
```
    MIT License
```
       
