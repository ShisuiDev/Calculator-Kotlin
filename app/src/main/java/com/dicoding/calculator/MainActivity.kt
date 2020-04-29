package com.dicoding.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

//mendefinsikan variabel yang digunakan untuk meresume aplikasi ketika android dimiringkan
private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {
    //melakukan pengenalan variabel dengan jenis material android lateinit digunakan ketika kita ingin membuat non-null type
    private lateinit var result: EditText
    private lateinit var newNumber: EditText

    //Operasi yang kita buat tidak dapat diubah nilainya dan kita ingin membuat variabel yang tidak dapat diganti dan non-null type
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    //variabel to hold and the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)

        //Data input button
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.buttonDot)

        //Operations button
        val buttonEquals = findViewById<Button>(R.id.buttonEquals)
        val buttonPlus = findViewById<Button>(R.id.buttonPlus)
        val buttonMinus = findViewById<Button>(R.id.buttonMinus)
        val buttonMultiply = findViewById<Button>(R.id.buttonMultiply)
        val buttonDivide = findViewById<Button>(R.id.buttonDivide)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            displayOperation.text = pendingOperation
        }
        buttonEquals.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN //handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "+" -> operand1 = operand1!! + value
                "-" -> operand1 = operand1!! - value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        when {
            operand1 != null -> {
                outState.putDouble(STATE_OPERAND1, operand1!!)
                outState.putBoolean(STATE_OPERAND1_STORED, true)
            }
        }
            outState.putString(STATE_PENDING_OPERATION, pendingOperation)
        }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        when {
            savedInstanceState.getBoolean(STATE_OPERAND1_STORED,false) -> {
                this.operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
            }
            else -> {
                this.operand1 = null
            }
        }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        displayOperation.text = pendingOperation
    }
}


