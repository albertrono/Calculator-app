package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var operation_add = false
    private var decimal = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun action_num(view: View) {
        if (view is Button)
        {
            if (view.text == "." ){
                if (decimal)
                    workingtv.append(view.text)
                decimal = false
            }
            else
            workingtv.append(view.text)
            operation_add = true
        }
    }
    fun action_operator(view: View) {
        if (view is Button && operation_add){
            workingtv.append(view.text)
            operation_add = false
            decimal = true
        }
    }
    fun action_clear_all(view: View) {
        workingtv.text = ""
        resultstv.text = ""
    }
    fun action_delete(view: View) {
        var length = workingtv.length()
        if (length > 0)
            workingtv.text = workingtv.text.subSequence(0, length - 1)
    }
    fun action_equals(view: View) {
        resultstv.text = calculateresult()
    }

    private fun calculateresult(): String {
        val digits_operators = digits_operator()
        if (digits_operators.isEmpty()) return ""

        val times_division = times_division_calc(digits_operators)
        if (times_division.isEmpty()) return ""

        val result = add_subtract_calc(times_division)
        return result.toString()
    }

    private fun add_subtract_calc(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices){
            if (passedList[i] is Char && i !=passedList.lastIndex){
                val operator = passedList[i]
                val next_digit = passedList[i + i]as Float
                if (operator == '+')
                    result += next_digit
                if (operator == '-')
                    result -= next_digit
            }
        }

        return result
    }

    private fun times_division_calc(passedList: MutableList<Any>): MutableList<Any>{
        var list = passedList
        while (list.contains('x') || list.contains('/')){
            list = calc_times_division(list)
        }
        return list
    }

    private fun calc_times_division(passedList: MutableList<Any>): MutableList<Any> {
        val new_list = mutableListOf<Any>()
        var restart_index = passedList.size

        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex && i < restart_index)
            {
                val operator = passedList[i]
                val prev_digit = passedList[i - i] as Float
                val next_digit = passedList[i + i] as Float
                when (operator) {
                    'x' -> {
                        new_list.add(prev_digit * next_digit)
                        restart_index = i + i
                    }
                    '/' -> {
                        new_list.add(prev_digit / next_digit)
                        restart_index = i + i
                    }
                    else -> {
                        new_list.add(prev_digit)
                        new_list.add(operator)
                    }
                }

            }

            if (i >restart_index)
                new_list.add(passedList[i])
        }

        return new_list
    }

    private fun digits_operator(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in workingtv.text){
            if (character.isDigit() || character == '.')
                currentDigit += character
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
}