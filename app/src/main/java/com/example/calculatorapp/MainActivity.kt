package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    var canAddDecimal=true
     var canAddOperation=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var workingtv=findViewById<TextView>(R.id.workingtv)
        val resulttv=findViewById<TextView>(R.id.resulttv)
        fun numberAction(view: View)
        {
            if(view is Button)
            {

                if(view.text == ".")
                {
                    if(canAddDecimal)
                        workingtv.append(view.text)
                    canAddDecimal=false
                }
                else
                    workingtv.append(view.text)
                canAddOperation=true
            }
        }
        fun operationAction(view:View)
        {
            if(view is Button && canAddOperation)
            {
                workingtv.append(view.text)
                canAddOperation=false
                canAddDecimal=true
            }
        }
        fun allclearAction(view:View)
        {
            resulttv.text=""
            workingtv.text=""
        }

         fun addSubtractCalculate(passedList: MutableList<Any>): Float
        {
            var result = passedList[0] as Float

            for(i in passedList.indices)
            {
                if(passedList[i] is Char && i != passedList.lastIndex)
                {
                    val operator = passedList[i]
                    val nextDigit = passedList[i + 1] as Float
                    if (operator == '+')
                        result += nextDigit
                    if (operator == '-')
                        result -= nextDigit
                }
            }

            return result
        }



         fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
        {
            val newList = mutableListOf<Any>()
            var restartIndex = passedList.size

            for(i in passedList.indices)
            {
                if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
                {
                    val operator = passedList[i]
                    val prevDigit = passedList[i - 1] as Float
                    val nextDigit = passedList[i + 1] as Float
                    when(operator)
                    {
                        'x' ->
                        {
                            newList.add(prevDigit * nextDigit)
                            restartIndex = i + 1
                        }
                        '/' ->
                        {
                            newList.add(prevDigit / nextDigit)
                            restartIndex = i + 1
                        }
                        else ->
                        {
                            newList.add(prevDigit)
                            newList.add(operator)
                        }
                    }
                }

                if(i > restartIndex)
                    newList.add(passedList[i])
            }

            return newList
        }
        fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
        {
            var list = passedList
            while (list.contains('x') || list.contains('/'))
            {
                list = calcTimesDiv(list)
            }
            return list
        }

         fun digitsOperators():MutableList<Any>
        {
            val list = mutableListOf<Any>()
            var currentDigit=""
            for(character in workingtv.text)
            {
                if(character.isDigit() || character== '.')
                {
                    currentDigit+=character

                }
                else
                {
                    list.add(currentDigit.toFloat())
                    currentDigit=""
                    list.add(character)
                }
            }
            if(currentDigit!="")
            {
                list.add(currentDigit.toFloat())
            }
            return list
        }
        fun calculateResult(): String
        {
            val digitsOperators = digitsOperators()
            if(digitsOperators.isEmpty()) return ""

            val timesDivision = timesDivisionCalculate(digitsOperators)
            if(timesDivision.isEmpty()) return ""

            val result = addSubtractCalculate(timesDivision)
            return result.toString()
        }
    }
}
