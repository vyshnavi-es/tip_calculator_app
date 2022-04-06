package com.example.tippsii

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.tippsii.R.color.besttipcolor
import com.example.tippsii.R.color.worsttipcolor
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvtippercentage.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "On Progress changed $progress")
                tvtippercentage.text = "$progress"
                updateTipDescription(progress)
                computeTipandTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBase.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "After text changed : $s")
                computeTipandTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

    }

    private fun updateTipDescription(progress: Int) {

        val tipDescription : String

        when(progress) {
            in 0..9 -> tipDescription = "Poor"
            in 10..14 -> tipDescription = "Acceptable"
            in 15..19 -> tipDescription = "Good"
            in 20..24 -> tipDescription = "Great"
            else -> tipDescription = "Amazing"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            progress.toFloat() / seekBarTip.max ,
            ContextCompat.getColor(this, worsttipcolor),
            ContextCompat.getColor(this, besttipcolor)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipandTotal() {
        if(etBase.text.isEmpty())
        {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        val BaseAmount = etBase.text.toString().toDouble()
        val TipPercent = seekBarTip.progress
        val TipAmount = BaseAmount * TipPercent / 100
        val TotalAmount = BaseAmount + TipAmount

        tvTipAmount.text = "%.2f".format(TipAmount)
        tvTotalAmount.text = "%.2f".format(TotalAmount)

    }
}