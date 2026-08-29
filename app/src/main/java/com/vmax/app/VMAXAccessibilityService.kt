<?xml version="1.0" encoding="utf-8"?>  
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"  
    xmlns:app="http://schemas.android.com/apk/res-auto"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:background="#f4f9fd">

    <LinearLayout  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:orientation="vertical"  
        android:background="#ffffff"  
        android:padding="30dp">

        <!-- Header -->  
        <LinearLayout  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:orientation="vertical"  
            android:background="#000000"  
            android:padding="16dp"  
            android:layout_marginBottom="20dp">

            <TextView  
                android:layout_width="wrap_content"  
                android:layout_height="wrap_content"  
                android:text="VMAX - Train Ticket Automation"  
                android:textColor="#ffffff"  
                android:textSize="24sp"  
                android:textStyle="bold" />

            <TextView  
                android:layout_width="wrap_content"  
                android:layout_height="wrap_content"  
                android:text="Use of Accessibility Service API"  
                android:textColor="#ffffff"  
                android:textSize="16sp"  
                android:layout_marginTop="8dp" />  
        </LinearLayout>

        <!-- Main Title -->  
        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="Use of Accessibility Service API"  
            android:textColor="#3f4554"  
            android:textSize="28sp"  
            android:textStyle="bold"  
            android:gravity="center"  
            android:layout_marginBottom="40dp" />

        <!-- Why Accessibility Service is needed -->  
        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="Why Accessibility Service is needed in VMAX?"  
            android:textColor="#474c52"  
            android:textSize="20sp"  
            android:textStyle="bold"  
            android:layout_marginBottom="16dp" />

        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="The VMAX app is used to book train tickets (especially in TATKAL quota) on the IRCTC website or IRCTC Rail Connect app. It saves time by filling all the details for you so that you can get a ticket as quickly as possible in the TATKAL quota's first-come, first-serve basis."  
            android:textColor="#484848"  
            android:textSize="15sp"  
            android:lineSpacingExtra="4dp"  
            android:layout_marginBottom="8dp" />

        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="So, in order to automate the IRCTC Rail Connect app and autofill the details, the accessibility service is needed in VMAX so that it can access and control the app securely."  
            android:textColor="#484848"  
            android:textSize="15sp"  
            android:lineSpacingExtra="4dp"  
            android:layout_marginBottom="32dp" />

        <!-- How is the accessibility service used -->  
        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="How is the accessibility service used for the automated ticket booking feature of VMAX?"  
            android:textColor="#474c52"  
            android:textSize="20sp"  
            android:textStyle="bold"  
            android:layout_marginBottom="16dp" />

        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="Enabling the accessibility service for VMAX helps it to control other apps (in our case, the IRCTC Rail Connect app). If you have an IRCTC account and want to book TATKAL quota tickets with a good chance of getting a confirmed ticket, you can do so using VMAX."  
            android:textColor="#484848"  
            android:textSize="15sp"  
            android:lineSpacingExtra="4dp"  
            android:layout_marginBottom="8dp" />

        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="There are 2 modes of booking: Using the IRCTC Website & using the IRCTC Rail Connect app. To book using the latter, we have made a video for a complete demonstration of how to use it and explained why it is needed. You can watch the video "  
            android:textColor="#484848"  
            android:textSize="15sp"  
            android:lineSpacingExtra="4dp"  
            android:layout_marginBottom="8dp" />

        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="here."  
            android:textColor="#0948B3"  
            android:textSize="15sp"  
            android:onClick="openYouTubeVideo"  
            android:clickable="true"  
            android:focusable="true"  
            android:textStyle="bold"  
            android:layout_marginBottom="32dp" />

        <!-- What data is collected -->  
        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="What data is collected by VMAX using the accessibility service and for what purpose?"  
            android:textColor="#474c52"  
            android:textSize="20sp"  
            android:textStyle="bold"  
            android:layout_marginBottom="16dp" />

        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="VMAX does not collect or store any of your personal or sensitive data accessed using the Accessibility Service. The service is strictly used to fill the data automatically in the IRCTC Rail Connect app in order to book the train ticket quickly and securely."  
            android:textColor="#484848"  
            android:textSize="15sp"  
            android:lineSpacingExtra="4dp"  
            android:layout_marginBottom="32dp" />

        <!-- How to enable accessibility service -->  
        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="How to enable accessibility service for VMAX?"  
            android:textColor="#474c52"  
            android:textSize="20sp"  
            android:textStyle="bold"  
            android:layout_marginBottom="16dp" />

        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="In the VMAX app, go to Dashboard → Ticket Booking → New Form. You can fill in all the booking details here, which will be auto-filled while booking the train ticket. In the RAILCONNECT tab, when you click on 'Book Now' to start the booking, it will display a prompt to enable the accessibility service for VMAX, with 'SETTINGS' and 'CANCEL' buttons. By clicking SETTINGS, it will take you to your phone's Accessibility Service settings, where you can find the VMAX app in the list of apps using the accessibility service and enable it."  
            android:textColor="#484848"  
            android:textSize="15sp"  
            android:lineSpacingExtra="4dp"  
            android:layout_marginBottom="8dp" />

        <TextView  
            android:layout_width="match_parent"  
            android:layout_height="wrap_content"  
            android:text="If you do not wish to enable the accessibility service, you would not be able to book tickets by autofilling the Rail Connect app. However, you can still use the WEBSITE option to book the ticket on the IRCTC website through automation."  
            android:textColor="#484848"  
            android:textSize="15sp"  
            android:lineSpacingExtra="4dp" />

    </LinearLayout>  
</ScrollView>  
