package com.ddona.jetpack.ui.paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddona.jetpack.R
import com.ddona.jetpack.adapter.PassengersAdapter
import com.ddona.jetpack.databinding.ActivityPagingBinding
import com.ddona.jetpack.model.Airline
import com.ddona.jetpack.model.Passenger

class PagingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagingBinding
    private val passengers = listOf(
        Passenger(
            0,
            "5f7c5379e0f93ef0b32fd4f3",
            Airline(
                "Taiwan",
                "1989",
                "76, Hsin-Nan Rd., Sec. 1, Luzhu, Taoyuan City, Taiwan",
                1,
                "https://upload.wikimedia.org/wikipedia/en/thumb/e/ed/EVA_Air_logo.svg/250px-EVA_Air_logo.svg.png",
                "fasdfasdfasdf",
                "Abcasdfahskdj",
                "www.evaair.com"
            ),
            "esing funda5292",
            250
        ),
        Passenger(
            0,
            "5f7c5379e0f93ef0b32fd4f3",
            Airline(
                "Taiwan",
                "1989",
                "76, Hsin-Nan Rd., Sec. 1, Luzhu, Taoyuan City, Taiwan",
                1,
                "https://upload.wikimedia.org/wikipedia/en/thumb/e/ed/EVA_Air_logo.svg/250px-EVA_Air_logo.svg.png",
                "fasdfasdfasdf",
                "Abcasdfahskdj",
                "www.evaair.com"
            ),
            "esing funda5292",
            250
        ),
        Passenger(
            0,
            "5f7c5379e0f93ef0b32fd4f3",
            Airline(
                "Taiwan",
                "1989",
                "76, Hsin-Nan Rd., Sec. 1, Luzhu, Taoyuan City, Taiwan",
                1,
                "https://upload.wikimedia.org/wikipedia/en/thumb/e/ed/EVA_Air_logo.svg/250px-EVA_Air_logo.svg.png",
                "fasdfasdfasdf",
                "Abcasdfahskdj",
                "www.evaair.com"
            ),
            "esing funda5292",
            250
        ),
        Passenger(
            0,
            "5f7c5379e0f93ef0b32fd4f3",
            Airline(
                "Taiwan",
                "1989",
                "76, Hsin-Nan Rd., Sec. 1, Luzhu, Taoyuan City, Taiwan",
                1,
                "https://upload.wikimedia.org/wikipedia/en/thumb/e/ed/EVA_Air_logo.svg/250px-EVA_Air_logo.svg.png",
                "fasdfasdfasdf",
                "Abcasdfahskdj",
                "www.evaair.com"
            ),
            "esing funda5292",
            250
        ),
        Passenger(
            0,
            "5f7c5379e0f93ef0b32fd4f3",
            Airline(
                "Taiwan",
                "1989",
                "76, Hsin-Nan Rd., Sec. 1, Luzhu, Taoyuan City, Taiwan",
                1,
                "https://upload.wikimedia.org/wikipedia/en/thumb/e/ed/EVA_Air_logo.svg/250px-EVA_Air_logo.svg.png",
                "fasdfasdfasdf",
                "Abcasdfahskdj",
                "www.evaair.com"
            ),
            "esing funda5292",
            250
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = PassengersAdapter(passengers)

    }
}