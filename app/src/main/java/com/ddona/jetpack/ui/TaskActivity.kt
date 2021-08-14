package com.ddona.jetpack.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ddona.jetpack.adapter.TaskAdapter
import com.ddona.jetpack.databinding.ActivityTaskBinding
import com.ddona.jetpack.model.Task
import java.text.SimpleDateFormat
import java.util.*


class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var date: Calendar
    private val tasks =
        mutableListOf(
            Task(1, "Task 1", "Description 1", System.currentTimeMillis()),
            Task(2, "Task 2", "Description 2", System.currentTimeMillis()),
            Task(3, "Task 3", "Description 3", System.currentTimeMillis()),
        )

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAdd.setOnClickListener {
            val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            if (!this::date.isInitialized) {
                Toast.makeText(this, "Please set a deadline!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.tvDate.text.isEmpty()) {
                Toast.makeText(this, "Please set a deadline!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            tasks.add(
                Task(
                    tasks.size + 1,
                    binding.edtTitle.text.toString(),
                    binding.edtDescription.text.toString(),
                    format.parse(binding.tvDate.text.toString()).time
                )
            )
            binding.rvTasks.adapter?.notifyDataSetChanged()
        }
        binding.btnDate.setOnClickListener {
            showDateTimePicker()
        }
        binding.rvTasks.adapter = TaskAdapter(tasks)
    }

    @SuppressLint("SimpleDateFormat")
    fun showDateTimePicker() {
        val currentDate = Calendar.getInstance()
        date = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        Log.v("doanpt", "The choose one " + date.getTime())
                        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        binding.tvDate.text = format.format(date.time)
                    }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false
                ).show()
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        ).show()
    }
}