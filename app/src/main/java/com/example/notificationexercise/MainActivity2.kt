package com.example.notificationexercise

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationexercise.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val CH_ID="channelID"
    private val CH_NAME="channelName"
    private val NOTIFICATION_ID=0
    private val ACTION_SEPET ="action sepet"
    private val ACTION_SEPET_REQUEST_CODE= 3
    private lateinit var  pendingIntent : PendingIntent
    private val ACTION_YANITLA ="action yanıtla"
    private val ACTION_YANITLA_REQUEST_CODE= 4


    //NOTİFİCATION KOD SATIRLARI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMain2Binding.inflate(layoutInflater)

        createNotificationChannel()
        createPendingIntent()
        handleIntent(intent)

        binding.notification.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                requestPermission()
            }else{
                buildNotification()
            }
        }
        setContentView(binding.root)

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {
            handleIntent(it)
        }
    }

    private fun createPendingIntent(){
        val intent=Intent(this,MainActivity2::class.java)
        pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
    }

    private fun createNotificationChannel(){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CH_ID,CH_NAME,NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "bu kanalın amacı"
                    enableLights(true)
                    enableVibration(true)
                    }
                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)

        }
    }

    private fun sepetActionIntent() : PendingIntent{
            val sepetintent=Intent(this,MainActivity2::class.java).apply {
                action = ACTION_SEPET
                putExtra("data",100)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

            }
        return PendingIntent.getActivity(this,ACTION_SEPET_REQUEST_CODE,sepetintent,PendingIntent.FLAG_IMMUTABLE)
    }
    private fun createReplyActionIntent() : PendingIntent{
        val yanıtlaIntent=Intent(this,MainActivity2::class.java).apply {
            action=ACTION_YANITLA

            flags=Intent.FLAG_ACTIVITY_SINGLE_TOP

        }
        return PendingIntent.getActivity(this,ACTION_YANITLA_REQUEST_CODE,yanıtlaIntent,PendingIntent.FLAG_IMMUTABLE)
    }

    private fun handleIntent(intent: Intent){
        when(intent.action){
            ACTION_SEPET ->{
                val gelenveri=intent.getIntExtra("data",0)
                binding.textView2.text="GELEN VERİ : $gelenveri"
            }
            ACTION_YANITLA ->{

                binding.personName.postDelayed({
                    binding.personName.requestFocus()
                    val inputManager= getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.showSoftInput(binding.personName,InputMethodManager.SHOW_IMPLICIT)
                },1000)

            }
        }

    }

    private fun buildNotification(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        val a1=binding.personName.text.toString()
        val notification = NotificationCompat.Builder(this,CH_ID)
            .setContentTitle("Vakıfbank Mobil")
            .setContentText("1221 ile biten hesabınızdan, 1001,00 TL FAST Anlık Ödeme Gerçekleşmiştir.")
            .setSubText("VakıfBank")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.profile))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // api 26 küçükte ise burda da tanımlıyoruz bildirim derecesini üstte tanımladık bu 26 küçükse diye yaptık
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Bildirimi genişlettiğinde bu yazı görünücekBildirimi genişlettiğinde bu yazı görünücek" +
                    "Bildirimi genişlettiğinde bu yazı görünücek Bildirimi genişlettiğinde bu yazı görünücek"))
            .setColor(resources.getColor(R.color.blue))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

            .addAction(R.drawable.ic_launcher_foreground,"köye dön şef" , sepetActionIntent())
            .addAction(R.drawable.ic_launcher_foreground,"köye dönme şef",createReplyActionIntent())

            .build()

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID,notification)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                buildNotification()
                Log.d("osman", "${permissions[0]}izin verildi")
            }
        }else{
            Log.d("osman","${permissions[0]}izin verilmedi")

        }
    }
}