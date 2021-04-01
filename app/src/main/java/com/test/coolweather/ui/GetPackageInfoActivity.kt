package com.test.coolweather.ui

import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.test.coolweather.R
import kotlinx.android.synthetic.main.activity_get_package_info.*

class GetPackageInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_package_info)
        collectPackageInfos()
    }

    private fun collectPackageInfos() {
        val packageInfos: MutableList<PackageInfo> = ArrayList()
        val packages = packageManager.getInstalledPackages(0)
        packages.forEach {
            val appName = it.applicationInfo.loadLabel(packageManager).toString()
            val isSystemApp = (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            val packageInfo = PackageInfo(
                    appName,
                    it.packageName,
                    it.versionName,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) it.longVersionCode else it.versionCode.toLong(),
                    isSystemApp
            )
            packageInfos.add(packageInfo)
        }
        text_display.text = packageInfos.toString()
        text_display.movementMethod = ScrollingMovementMethod()
    }

    data class PackageInfo(
            val appName: String,
            val packageName: String,
            val versionName: String?,
            val versionCode: Long?,
            val systemApplication: Boolean
    )
}