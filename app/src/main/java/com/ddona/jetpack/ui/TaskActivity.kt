package com.ddona.jetpack.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ddona.jetpack.adapter.TaskAdapter
import com.ddona.jetpack.databinding.ActivityTaskBinding
import com.ddona.jetpack.model.Task
import com.ddona.jetpack.viewmodel.TaskViewModel
import com.ddona.jetpack.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var date: Calendar
    private val viewModel: TaskViewModel by viewModels() {
        TaskViewModelFactory(application)
    }

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
            lifecycleScope.launch {
                viewModel.insertTask(
                    Task(
                        title = binding.edtTitle.text.toString(),
                        description = binding.edtDescription.text.toString(),
                        deadline = format.parse(binding.tvDate.text.toString()).time
                    )
                )
            }

            binding.rvTasks.adapter?.notifyDataSetChanged()
        }
        binding.btnDate.setOnClickListener {
            showDateTimePicker()
        }
        val taskAdapter = TaskAdapter()

        binding.rvTasks.adapter = taskAdapter
        viewModel.task.observe(this, {
            taskAdapter.submitList(it)
        })
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