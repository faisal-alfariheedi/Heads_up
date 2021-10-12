package com.example.heads_up

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.Surface
import android.widget.*
import androidx.core.view.isVisible
import kotlinx.coroutines.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.URL

class game : AppCompatActivity() {
    
    lateinit var main: LinearLayout
    lateinit var ques: LinearLayout
    lateinit var tvTime: TextView
    lateinit var tvName: TextView
    lateinit var tab1: TextView
    lateinit var tab2: TextView
    lateinit var tab3: TextView
    lateinit var tvMain: TextView
    lateinit var btStart: Button
    lateinit var prog: ProgressBar
    lateinit var tvw: TextView
    var gameActive = false
    var cel = arrayListOf<celep.dat>()
    var celeb = 0
    var apif = APIClient().getClient()?.create(APIInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        init()
        btStart.setOnClickListener {
            if(cel.isEmpty()){
                getcel()
                newgame()
            }else{
                newgame()
            }
        }


    }

    private fun getcel() {
        if (apif != null) {
            apif!!.getdat()?.enqueue(object : Callback<List<celep.dat>> {
                override fun onResponse(call: Call<List<celep.dat>>, response: Response<List<celep.dat>>) {
                    for (i in response.body()!!) {
                        cel.add(i)

                    }
                    wait(false)
                    cel.shuffle()
                    newCelebrity(celeb)
                }

                override fun onFailure(call: Call<List<celep.dat>>, t: Throwable) {

                    wait(false)
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show();
                }
            })
        }
    }
    fun wait(a:Boolean){
        if(a){
            prog.isVisible = true
            tvw.isVisible = true
        }else{
            prog.isVisible=false
            tvw.isVisible=false
        }

    }


    fun init(){
        main = findViewById(R.id.main)
        ques = findViewById(R.id.quis)
        tvTime = findViewById(R.id.tvTime)
        tvName = findViewById(R.id.tvName)
        tab1 = findViewById(R.id.tvTaboo1)
        tab2 = findViewById(R.id.tvTaboo2)
        tab3 = findViewById(R.id.tvTaboo3)
        tvMain = findViewById(R.id.tvMain)
        btStart = findViewById(R.id.btStart)
        tvw=findViewById(R.id.wait)
        prog=findViewById(R.id.progressBar)
        apif = APIClient().getClient()?.create(APIInterface::class.java)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val rotation = windowManager.defaultDisplay.rotation
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            if (gameActive) {

                updateStatus(false)
            } else {
                updateStatus(false)
            }
        } else {
            if (gameActive) {
                newCelebrity(celeb)
                celeb++
                updateStatus(true)
                object : CountDownTimer(60000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        tvTime.text = "Time: ${millisUntilFinished / 1000}"
                    }

                    override fun onFinish() {
                        gameActive = false
                        tvTime.text = "Time: --"
                        tvMain.text = "Heads Up!"
                        btStart.isVisible = true
                        updateStatus(false)
                    }
                }.start()
            } else {

                updateStatus(false)
            }
        }
    }

    private fun newgame() {
        if (!gameActive) {
            gameActive = true
            tvMain.text = "Please Rotate Device"
            btStart.isVisible = false
            val rotation = windowManager.defaultDisplay.rotation
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                updateStatus(false)
            } else {
                newCelebrity(celeb)
                celeb++
                updateStatus(true)
                object : CountDownTimer(60000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        tvTime.text = "Time: ${millisUntilFinished / 1000}"
                    }

                    override fun onFinish() {
                        gameActive = false
                        tvTime.text = "Time: --"
                        tvMain.text = "Heads Up!"
                        btStart.isVisible = true
                        updateStatus(false)
                    }
                }.start()
            }

        }
    }

    private fun newCelebrity(id: Int) {
        if (id < cel.size) {
            tvName.text = cel[id].name
            tab1.text = cel[id].taboo1
            tab2.text = cel[id].taboo2
            tab3.text = cel[id].taboo3
        }
    }


    private fun updateStatus(showCelebrity: Boolean) {
        if (showCelebrity) {
            ques.isVisible = true
            main.isVisible = false
        } else {
            ques.isVisible = false
            main.isVisible = true
        }
    }

    /////////// data guardian from the rotation monster
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var cel = arrayListOf<celep.dat>()
        outState.putInt("celeb", celeb)
        outState.putBoolean("gameactive",gameActive)
        for (i in cel){
            outState.putString("name ${cel.indexOf(i)}",i.name)
            outState.putString("taboo1 ${cel.indexOf(i)}",i.taboo1)
            outState.putString("taboo2 ${cel.indexOf(i)}",i.taboo2)
            outState.putString("taboo3 ${cel.indexOf(i)}",i.taboo3)
        }
        outState.putInt("celsize",cel.size)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        celeb= savedInstanceState.getInt("celeb")
        gameActive=savedInstanceState.getBoolean("gameactive")
        var obj=celep.dat(0,"","","","")
        var n=0
        for (i in 0 until savedInstanceState.getInt("celsize")) {
            n = i
            obj.name = savedInstanceState.getString("name $n")
            obj.taboo1 = savedInstanceState.getString("taboo1 $n")
            obj.taboo2 = savedInstanceState.getString("taboo2 $n")
            obj.taboo3 = savedInstanceState.getString("taboo3 $n")
            cel.add(obj)
        }
        newCelebrity(celeb)

    }

    /////////////////////menu////////////////////////////


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        val item1 = menu!!.getItem(0)
        item1.setTitle("switch to edit db")


        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.m1 -> {
                intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}