package com.example.heads_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class addcel : AppCompatActivity() {
    lateinit var prog: ProgressBar
    lateinit var tvw: TextView
    lateinit var inname: EditText
    lateinit var tab1: EditText
    lateinit var tab2: EditText
    lateinit var tab3: EditText
    lateinit var addbut: Button
    lateinit var back: Button
    var apif = APIClient().getClient()?.create(APIInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcel)
        init()
        addbut.setOnClickListener{
            adddb()
        }
        back.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun init() {
        inname=findViewById(R.id.inname)
        tab1=findViewById(R.id.intaboo1)
        tab2=findViewById(R.id.intaboo2)
        tab3=findViewById(R.id.intaboo3)
        addbut=findViewById(R.id.addcel)
        back=findViewById(R.id.back)
        tvw=findViewById(R.id.wait)
        prog=findViewById(R.id.progressBar)
        inname.setText(intent.getStringExtra("na"))
        tab1.setText(intent.getStringExtra("ta1"))
        tab2.setText(intent.getStringExtra("ta2"))
        tab3.setText(intent.getStringExtra("ta3"))
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
    fun adddb() {
        CoroutineScope(Dispatchers.IO).launch {
            var f = celep.dat(
                intent.getIntExtra("id", 0),
                inname.text.toString(),
                tab1.text.toString(),
                tab2.text.toString(),
                tab3.text.toString()
            )
            if (apif != null) {
                apif!!.adddat(f)?.enqueue(object : Callback<celep.dat> {
                    override fun onResponse(call: Call<celep.dat>, response: Response<celep.dat>) {
                        wait(false)
                        inname.setText("")
                        tab1.setText("")
                        tab2.setText("")
                        tab3.setText("")
                        Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure(call: Call<celep.dat>, t: Throwable) {

                            wait(false)
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT)
                                .show();
                        }
                    })
            }
        }
    }

}