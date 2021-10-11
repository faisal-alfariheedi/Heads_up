package com.example.heads_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var incel: EditText
    lateinit var rv: RecyclerView
    var dat = arrayListOf<celep.dat>()
    lateinit var prog: ProgressBar
    lateinit var tvw: TextView
    lateinit var sub: Button
    lateinit var add:Button
    var apif = APIClient().getClient()?.create(APIInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        CoroutineScope(Dispatchers.IO).launch{
        if (apif != null) {
            apif!!.getdat()?.enqueue(object : Callback<List<celep.dat>> {
                override fun onResponse(call: Call<List<celep.dat>>, response: Response<List<celep.dat>>) {
                    for (i in response.body()!!) {
                        dat.add(i)

                    }
                    rv.adapter?.notifyDataSetChanged()
                    wait(false)

                }

                override fun onFailure(call: Call<List<celep.dat>>, t: Throwable) {

                    wait(false)
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show();
                }
            })
        }}
        sub.setOnClickListener {
            subswitch()
        }
        add.setOnClickListener{
            val intent = Intent(this, addcel::class.java)
            startActivity(intent)
        }


    }
    fun init() {
        incel = findViewById(R.id.incel)
        add=findViewById(R.id.addcel)
        rv = findViewById(R.id.rvma)
        rv.adapter = RVAdapter(dat,this)
        rv.layoutManager = LinearLayoutManager(this)
        tvw=findViewById(R.id.wait)
        prog=findViewById(R.id.progressBar)
        sub=findViewById(R.id.sub)
        wait(true)
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
    fun subswitch(){
        intent = Intent(applicationContext, editdelet::class.java)
        for (i in dat){
            if(i.name!!.lowercase()==incel.text!!.toString().lowercase()){
                intent.putExtra("na",i.name)
                intent.putExtra("ta1",i.taboo1)
                intent.putExtra("ta2",i.taboo2)
                intent.putExtra("ta3",i.taboo3)
                intent.putExtra("id",i.pk)
                break
            }
            if (dat.size-1==dat.indexOf(i)) Toast.makeText(this,"this name doesn`t exist in database",Toast.LENGTH_LONG).show()
        }

        startActivity(intent)
    }
}