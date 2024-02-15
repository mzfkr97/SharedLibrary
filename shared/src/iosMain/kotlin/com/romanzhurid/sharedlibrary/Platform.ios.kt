package com.romanzhurid.sharedlibrary

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " DEV TEST" + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()