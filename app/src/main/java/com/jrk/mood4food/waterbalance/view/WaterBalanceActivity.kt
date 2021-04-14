package com.jrk.mood4food.waterbalance.view

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.john.waveview.WaveView
import com.jrk.mood4food.App
import com.jrk.mood4food.NavBarActivity
import com.jrk.mood4food.R
import com.jrk.mood4food.model.ModelModule
import com.jrk.mood4food.waterbalance.controller.WaterBalanceController
import com.jrk.mood4food.waterbalance.model.WaterBalanceObserver


class WaterBalanceActivity : NavBarActivity(), WaterBalanceView, WaterBalanceObserver {
    private val model = ModelModule.dataAccessLayer
    private val controller = WaterBalanceController(model)
    private var waterAdd: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_water_balance)
        super.onCreate(savedInstanceState)
        controller.bind(this)

        findViewById<ImageView>(R.id.addwater).setOnClickListener{
            val builder1 = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val view = inflater.inflate(R.layout.wateradd_dialog,null)
            builder1.setView(view)
            builder1.setMessage("Wie viel hast du getrunken")
            builder1.setCancelable(true)
            val seekBar = view.findViewById<SeekBar>(R.id.seekBar)
            seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    view.findViewById<TextView>(R.id.water_text).text = (p1 * 0.25).toString() + " Liter"
                    waterAdd = (p1 * 0.25).toFloat()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
            builder1.setPositiveButton(
                    "Add") { dialog, _ -> controller.onWaterAdd(waterAdd);dialog.cancel() }
            builder1.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            val alert11 = builder1.create()
            alert11.show()

        }

    }
    override fun setWaterBalance(x:Float){
        findViewById<WaveView>(R.id.waveView).setProgress(x.toInt())
        findViewById<TextView>(R.id.waterlevelPercentage).text = x.toInt().toString() + " %"
    }
    override fun onStart() {
        super.onStart()
        model.register(this)
        setWaterBalance(model.getWaterRepository().getCurrentWaterBalance())

    }


    override fun onStop() {
        super.onStop()
        model.unregister(this)
    }

    override fun getWaterAdd(): Float {
        return waterAdd
    }

    override fun waterStoredIn() {
       setWaterBalance(model.getWaterRepository().getCurrentWaterBalance())
        if(model.getWaterRepository().isWaterLevelReached()){
            Toast.makeText(App.getContext(), "Glückwunsch \n Du hast dein Tagesziel erreicht!!", Toast.LENGTH_LONG).show()
        }
    }

}