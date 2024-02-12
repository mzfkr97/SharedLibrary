package com.romanzhurid.sharedlibrary

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform