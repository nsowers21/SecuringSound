package com.example.securingsound

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.log10

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {

    //Permission Variables
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    //Check that permission opf microphone is granted
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }//end of onRequestPermissionResult

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing all the components
        val screen = findViewById<ConstraintLayout>( R.id.Screen)
        val startButton = findViewById<Button>(R.id.Start)
        val endButton = findViewById<Button>(R.id.stop)
        val warning = findViewById<TextView>(R.id.Warning)
        var mediaRecorder: MediaRecorder? = null
        var maxAmp: Double? = null
        var maxDb: Double? = null

        //Sets the mediaRecorderVariables
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile("dev/null")

        //if clicked start recording
        startButton.setOnClickListener{
            screen.setBackgroundColor(resources.getColor(R.color.green))
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            maxAmp = (mediaRecorder?.maxAmplitude)?.toDouble()
            maxDb = 20 * log10(maxAmp?.div(0.1)!!)
            if(maxDb!! >= 120){
                warning.text = ("This is an unsafe area")
                screen.setBackgroundColor(resources.getColor(R.color.red))
            }
            endButton.setOnClickListener{
                mediaRecorder?.stop()
            }//end of endButton
        }//end of Start Button
    }//end of onCreate
}//end of MainActivity


