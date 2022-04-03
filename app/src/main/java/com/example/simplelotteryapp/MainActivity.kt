package com.example.simplelotteryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.simplelotteryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.textView1),
            findViewById(R.id.textView2),
            findViewById(R.id.textView3),
            findViewById(R.id.textView4),
            findViewById(R.id.textView5),
            findViewById(R.id.textView6),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        val numberPicker:NumberPicker?=binding?.numberPicker
        numberPicker?.minValue = 1
        numberPicker?.maxValue = 100
        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initRunButton() {
        val runButton: Button? = binding?.runButton
        runButton?.setOnClickListener{
            val list = getRandomNumber()
            didRun = true
            list.forEachIndexed{ index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumberBackground(number, textView)
            }
        }
    }
    private fun getRandomNumber(): List<Int>{
        val numberList = mutableListOf<Int>()
            .apply {
                for(i in 1.. 45){
                    if(pickNumberSet.contains(i)){
                        continue
                    }
                    this.add(i)
                }
            }
        numberList.shuffle()
        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)
        return newList.sorted()
    }

    private fun initAddButton(){
        val addButton: Button? = binding?.addButton
        val numberPicker:NumberPicker?=binding?.numberPicker
        addButton?.setOnClickListener{
            if(didRun) {
                Toast.makeText(this, "Please clear the line first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pickNumberSet.size >= 6) {
                Toast.makeText(this, "You've reached the limit", Toast.LENGTH_SHORT).show()
                binding?.runButton?.text = "Change the color!"
                return@setOnClickListener
            }
            if(pickNumberSet.contains(numberPicker?.value)){
                Toast.makeText(this, "Can't add same value", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker?.value.toString()

            numberPicker?.value?.let { it1 -> setNumberBackground(it1, textView) }
            numberPicker?.let { it1 -> pickNumberSet.add(it1.value) }

        }
    }
    private fun setNumberBackground(number:Int, textView: TextView){
        val colors = arrayOf(R.drawable.circle_yellow,
            R.drawable.circle_blue,
            R.drawable.circle_red,
            R.drawable.circle_pink,
            R.drawable.circle_orange,
            R.drawable.circle_purple)
        val randomColor = (0..5).random()
        textView.background = ContextCompat.getDrawable(this, colors[randomColor])
    }

    private fun initClearButton() {
        val clearButton: Button? = binding?.clearButton
        clearButton?.setOnClickListener{
            binding?.runButton?.text = "Start Lottery"
            pickNumberSet.clear()
            numberTextViewList.forEach{
                it.isVisible = false
            }
            didRun = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}