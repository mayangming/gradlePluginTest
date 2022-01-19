package com.example.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import com.hjq.permissions.XXPermissions

import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.ActivityManager.RunningTaskInfo

import android.app.usage.UsageStats

import android.app.usage.UsageStatsManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo

import com.jaredrummler.android.processes.models.Statm

import com.jaredrummler.android.processes.models.Stat

import com.jaredrummler.android.processes.models.AndroidAppProcess

import com.jaredrummler.android.processes.AndroidProcesses
import java.util.*
import java.util.logging.Logger
import android.content.pm.ResolveInfo

import java.util.HashSet

import java.util.ArrayList

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.viewModels
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.util.SAFManager
import com.example.myapplication.util.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

import java.io.InputStreamReader

import java.io.BufferedReader

class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    //com.qiyi.video
//    /storage/emulated/0/Android/data/com.example.myapplication/cache-->cache:/data/user/0/com.example.myapplication/cache


    private val viewModel: TestAndroidViewModel by viewModels()

    private val viewModel1: TestAndroidViewModel by applicationViewModels()

    private val viewModel2: TestAndroidViewModel by applicationViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    private val saveState: SaveStateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        isGetExternalStoragePermission()
//        if (!hasPermission()) {
//            //若用户未开启权限，则引导用户开启“Apps with usage access”权限
//            startActivityForResult(
//                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
//                MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS
//            );
//        }
//        getFolderSize(context.externalCacheDir!!) + getFolderSize(context.cacheDir)
//        val externalCacheDir = externalCacheDir!!.absolutePath
//        val cacheDir = cacheDir!!.absolutePath
//        Log.e("YM-->", "externalCacheDir:${externalCacheDir}-->cache:${cacheDir}")
//        viewModel1.add()
//        saveState.putValue("你好啊")
//        saveState.getValue().observe(this){
//            Log.e("YM--->","获取的值:$it")
//        }
//        val androidRootPath = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia/document/primary%3AAndroid%2Fmedia"
//        val androidDocument = DocumentFile.fromTreeUri(this, Uri.parse(androidRootPath))
//        val isExists = androidDocument?.exists()
//        Log.e("YM-->","android-DocumentFile文件夹是否存在:${isExists}")
//        Thread{
//            val result = Shell.sendCmd("ls")
//            Log.e("YM-->result","result-->${result}")
//        }.start()
////        getRunningProgressInfos()
//        execShell("ls")
//            lifecycle.addObserver(MyObserver())
        viewModel.changeValue("11")
        viewModel.userName.observe(this){
            Log.e("YM-->change","--->newValue:$it")
        }
        lifecycleScope.launch(context = Dispatchers.IO){
            delay(1000)
            viewModel.changeValue("3")
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel1.show()
    }
    /**
     * 卸载应用
     */
    fun uninstallApp(context: Context, pkg: String){
        val intent = Intent(Intent.ACTION_DELETE)
        with(intent){
            data = Uri.parse("package:$pkg")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
    private fun initView() {




        initPermission()
        binding.uninstallPackage.setOnClickListener {
            uninstallApp(it.context,"com.example.myapplication")
        }
        binding.runningProgress.setOnClickListener {
            getForegroundApp()
        }
        binding.clearCache.setOnClickListener {
//            /storage/emulated/0/Android/data/com.qiyi.video/cache
//            /data/user/0/com.qiyi.video/cache
            val exterCache = "/storage/emulated/0/Android/data/com.qiyi.video/cache"
            val innerCache = "/data/user/0/com.qiyi.video/cache"
            SystemUtils.deleteFolderFile(exterCache, false)
            SystemUtils.deleteFolderFile(innerCache, false)
        }
        binding.allPackage.setOnClickListener {
            val packages = SystemUtils.getAllPackage(it.context)
            packages.forEach { packageName ->
                Log.e("YM-->", "程序包名:$packageName")
            }
        }
        binding.filePermissionBtn.setOnClickListener {
            val version = Integer.valueOf(android.os.Build.VERSION.SDK_INT)
            Log.e("YM-->", "系统版本:$version")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) { //版本小于30 采用29以下的方式进行处理
                initPermission()
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) { //版本等于30 ，采用30的处理方式

            } else { //版本等于31，采用31的版本处理

            }
            //判断是否获取MANAGE_EXTERNAL_STORAGE权限：
//            val isHasStoragePermission= Environment.isExternalStorageManager()
//            if (!isHasStoragePermission){
//                initPermission()
//            }else{
//                Toast.makeText(it.context,"获取了权限",Toast.LENGTH_SHORT).show()
//            }
        }
        binding.goSecond.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SecondActivity2::class.java
                )
            )
        }
    }

    private fun initPermission() {
//        val intent = Intent()
//        intent.action= Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//        startActivity(intent)
        XXPermissions.with(this) // 申请单个权限
            .permission(Permission.WRITE_EXTERNAL_STORAGE) // 申请多个权限
//            .permission(Settings.ACTION_USAGE_ACCESS_SETTINGS) // 申请多个权限
            //.permission(Permission.Group.CALENDAR)
            // 申请安装包权限
            //.permission(Permission.REQUEST_INSTALL_PACKAGES)
            // 申请悬浮窗权限
            //.permission(Permission.SYSTEM_ALERT_WINDOW)
            // 申请通知栏权限
            //.permission(Permission.NOTIFICATION_SERVICE)
            // 申请系统设置权限
            //.permission(Permission.WRITE_SETTINGS)
            // 设置权限请求拦截器
            //.interceptor(new PermissionInterceptor())
            // 设置不触发错误检测机制
            //.unchecked()
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, all: Boolean) {
                    if (all) {
                        toast("获取读写权限成功")
//                        SAFManager.onGotAccess(this@MainActivity,contentResolver)
                    } else {
                        toast("获取部分权限成功，但部分权限未正常授予")
                    }
                }

                override fun onDenied(permissions: List<String>, never: Boolean) {
                    if (never) {
                        toast("被永久拒绝授权，请手动授予录音和日历权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(this@MainActivity, permissions)
                    } else {
                        toast("获取读写权限失败")
                    }
                }
            })

    }

    private fun toast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show()
    }

    private fun getRunningProgressInfos() {
//        get_uss_info()
        ProcessChecker.getForegroundApp(this)
    }

    //检测用户是否对本app开启了“Apps with usage access”权限
    private fun hasPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = 0
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun getForegroundApp(): String? {
        val files = File("/proc").listFiles()
        var foregroundProcess = ""
        var i = 0
        for (file in files) {
            i++
            Log.d(
                "brycegao", "proc file:" + file.name
                        + ", loop:" + i
            )
            if (file.isFile) {
                continue
            }
            var pid: Int
            Log.d("brycegao", "proc filename:" + file.name)
            pid = try {
                file.name.toInt()
            } catch (e: NumberFormatException) {
                continue
            }
            try {

//读取进程名称
                val cmdline: String = execShell(String.format("cat /proc/%d/cmdline", pid))
                val oomAdj: String = execShell(String.format("cat /proc/%d/oom_adj", pid))
                Log.d("brycegao", "adj1111:$oomAdj,pkg:$cmdline")
                if (oomAdj.equals("0", ignoreCase = true)) {

//前台进程
                    Log.d("brycegao", "adj:$oomAdj,pkg:$cmdline")
                } else {
                    continue
                }
                if (cmdline.contains("systemui")
                    || cmdline.contains("/")
                ) {
                    continue
                }
                foregroundProcess = cmdline
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.d("brycegao", "forgroud process:$foregroundProcess")
        return foregroundProcess
    }
    private fun execShell(cmd: String): String {
        var result = ""
        try {
            val process = Runtime.getRuntime().exec(cmd)
            val mReader = InputStreamReader(process.inputStream)
            println("YM========START==========")

            result = mReader.readText()
            println("YM==========${result}")
            mReader.close()
            process.destroy()
            println("YM========END==========")
        } catch (e: Exception) {
            println("YM========执行Linux命令异常==========${e.message}")
        }
        return result
    }

    private fun isGetExternalStoragePermission(){
        //判断是否需要所有文件权限
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager())) {
            //表明已经有这个权限了
            Toast.makeText(this,"---->获取到文件读写权限",Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        }

    }

}