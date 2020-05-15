package com.example.first_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import kotlinx.android.synthetic.main.india.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.SSLContext
import kotlin.collections.ArrayList


var sou= listOf<String>("")
var dai= listOf<String>("")

var daily_cases= IntArray(6)
val entries: MutableList<BarEntry> = ArrayList()
val labels = ArrayList<String>()

var today=0

var bmp: Bitmap? = null

var today_date=" "


var t_=1
var t_1=1
var t_2=1

class India : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        try {
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext
            sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.india)

        var swipe=findViewById<SwipeRefreshLayout>(R.id.swi)
        swipe.setOnRefreshListener({
            val intent = Intent(this,India::class.java)
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
            //swipe.setRefreshing(false)
        })

        var dat=Calendar.getInstance().getTime()
        var date_format=dat.toString().substring(4,10)
        today_date=date_format

        state_data().execute()
        daily_data().execute()
        map().execute()

        while(t_==1){}

        var c=0
        var rows=""
        for (i in sou)
        {
            if(i.contains("/20"))
            {
                var state=i.split(",")
                if(c==0)
                {
                    textView36.text= state[1]
                    textView29.text= state[2]
                    textView22.text= state[3]
                    textView37.text= "new :"+state[7]
                    today=Integer.parseInt(state[7])
                    textView30.text= "new :"+state[8]
                    textView23.text= "new :"+state[9]
                }
                else
                {
                    rows="R"+(c).toString()
                    var res_id=resources.getIdentifier(rows,"id",packageName)
                    val tv: TextView = findViewById(res_id)
                    tv.text=state[0]+"\n Total Cases :"+state[1]+"   [+"+state[7]+"]" +"\n Recovered :"+state[2]+"    [+"+state[8]+"]"+ "\n Deaths :"+state[3]+"    [+"+state[9]+"]"
                }
                c=c+1
            }

        }


            while (t_1 == 1) {
            }

            val barChart=findViewById<BarChart>(R.id.barchart)
            barChart.clear()

            val bardataset = BarDataSet(entries, "Cells")

            val data = BarData(labels, bardataset)

            barChart.data = data // set the data and list of labels into chart
            bardataset.setColor(Color.rgb(255, 255, 255))
            barChart.setDescription("")
            barChart.axisLeft.setDrawLabels(false)
            barChart.axisRight.setDrawLabels(false)
            //barChart.xAxis.setDrawLabels(false)
            barchart.legend.setEnabled(false)
            barChart.xAxis.setDrawGridLines(false)
            barChart.axisLeft.setDrawGridLines(false)
            barChart.axisRight.setDrawGridLines(false)
            barChart.setTouchEnabled(false)
            barChart.setScaleEnabled(false)
            barChart.isDoubleTapToZoomEnabled
            barChart.setPinchZoom(false)
            barChart.setDrawBorders(false)
            barChart.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
            barChart.xAxis.setTextColor(Color.WHITE)
            barChart.xAxis.setTextSize(15f)
            barChart.xAxis.setLabelRotationAngle(-90f)
            barChart.setBorderWidth(0f)
            bardataset.setValueTextSize(12f)
            bardataset.setValueTextColor(Color.WHITE)
            bardataset.setBarSpacePercent(50f)

            while (t_2 == 1) {
            }
            var image: ImageView = findViewById(R.id.map1)
            image.setImageBitmap(bmp)

    }
}





class state_data() : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        val url="https://api.covid19india.org/csv/latest/state_wise.csv"
        val u=URL(url)
        var k=BufferedReader(InputStreamReader(u.openStream()))
        var line=k.readLines()
        sou=line
        t_=0
        return null
    }
}



class daily_data() : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {

        val url="https://www.worldometers.info/coronavirus/country/india/"
        val u=URL(url)
        var k=BufferedReader(InputStreamReader(u.openStream()))
        dai=k.readLines()

        var count=0
        var i_=0
        for (l in dai){
            if(l.contains("data-date=")||(l.contains("news_date")&&l.contains("newsdate")))
            {
                count++
            }
        }

        entries.clear()
        labels.clear()

        labels.add(0, today_date)

        var q=1
        for (l in dai){
            if(l.contains("data-date=")||(l.contains("news_date")&&l.contains("newsdate")))
            {
                var st_ind=0
                var en_ind=0
                if(l.contains("news_date")&&l.contains("newsdate")){
                    st_ind=l.indexOf("news_date")+15
                    en_ind=l.indexOf("GMT")-2
                    if(l.substring(st_ind,en_ind).replace(" ".toRegex(),"")== today_date.replace(" ".toRegex(),"")){continue}
                    labels.add(q,l.substring(st_ind,en_ind))
                    q++
                }
                else if(l.contains("&nbsp")){
                    st_ind=l.indexOf("data-date")+23
                    en_ind=l.indexOf("&nbsp")
                    if(l.substring(st_ind,en_ind).replace(" ".toRegex(),"")== today_date.replace(" ".toRegex(),"")){continue}
                    labels.add(q,l.substring(st_ind,en_ind))
                    q++
                }
                else{
                    st_ind=l.indexOf("data-date")+23
                    en_ind=l.indexOf("chevron")-17
                    if(l.substring(st_ind,en_ind).replace(" ".toRegex(),"")== today_date.replace(" ".toRegex(),"")){continue}
                    labels.add(q,l.substring(st_ind,en_ind))
                    q++
                }
            }
        }

        Collections.reverse(labels)
        count= labels.size


        for (l in dai) {
            if (l.contains("new cases") && l.contains("new deaths")) {
                var index = l.indexOf("<strong>")
                var end_index = l.indexOf("new cases")
                var f = Integer.parseInt(l.substring(index + 8, end_index - 1).replace(",", "")).toFloat()
                if(i_!=-1)
                    entries.add(BarEntry(f,count-i_-2))
                i_ = i_ + 1
            }
        }


        entries.add(BarEntry(today.toFloat(),count-1))

        t_1=0
        return null
    }
}


class map() : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        val url="https://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/COVID-19_Outbreak_Cases_in_India.svg/674px-COVID-19_Outbreak_Cases_in_India.svg.png"
        val u=URL(url)
        bmp=BitmapFactory.decodeStream(u.openConnection().getInputStream())
        t_2=0
        return null
    }
}





