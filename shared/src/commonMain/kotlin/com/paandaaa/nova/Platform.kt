package com.paandaaa.nova

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform