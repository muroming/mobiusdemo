package com.example.mobiusdemoapplication.data

import com.example.mobiusdemoapplication.domain.Scooter
import kotlinx.coroutines.delay
import kotlin.random.Random

class ScootersRepository {
    suspend fun loadUserScooters(): List<Scooter> {
        delay(300L)
        return listOf(
            Scooter(
                "1",
                "Ninebot Max",
                "https://img.gkbcdn.com/p/2019-10-10/xiaomi-ninebot-max-g30-electric-scooter-350w-motor-max-30km-h-black-1574132374489._w500_.jpg",
                10.4f,
                45,
                79.3f
            ),
            Scooter(
                "2",
                "Xiaomi Electric 15",
                "https://avatars.mds.yandex.net/get-mpic/4577446/img_id1130306737394190487.jpeg/13hq",
                15.2f,
                78,
                103.7f
            )
        )
    }

    suspend fun connectToScooter(scooterId: String): Boolean {
        delay(500L)
        return Random.nextBoolean()
    }
}