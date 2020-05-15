package com.example.first_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        println("this")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var p =findViewById<ProgressBar>(R.id.progressBar1)
        IndButton.setOnClickListener {
            if(p.getVisibility()!= View.VISIBLE )
            {
                p.setVisibility(View.VISIBLE) // if not set it to visible
                //IndButton.setVisibility(View.VISIBLE); // use 1 or 2 as parameters.. arg0 is the view(your button) from the onclick listener
            }
            else
            {
                p.setVisibility(View.INVISIBLE)
            }

            var i =Intent(this,  India::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
        }

    }

    override fun onResume() {
        super.onResume()
        var p =findViewById<ProgressBar>(R.id.progressBar1)
        p.setVisibility(View.INVISIBLE)
    }
}


